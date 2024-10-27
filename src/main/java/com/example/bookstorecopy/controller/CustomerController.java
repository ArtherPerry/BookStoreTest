package com.example.bookstorecopy.controller;

import com.example.bookstorecopy.entities.BookDto;
import com.example.bookstorecopy.entities.Customer;
import com.example.bookstorecopy.service.*;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private EmailService emailService;

    private static final String MESSAGE_ATTR = "message";
    private static final String ERROR_ATTR = "error";
    private static final String CHECKOUT_VIEW = "checkOut";
    private static final String SUCCESS_VIEW = "Success";

    @PostMapping("/customer/save")
    public String saveCustomerOrder(@ModelAttribute("customer") Customer customer,
                                    @RequestParam("receiptFile") MultipartFile receiptFile,
                                    BindingResult bindingResult,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            return reloadCheckoutPage(model, "Please fix the validation errors.");
        }

        List<BookDto> cartItems = cartService.listCart();  // Get cart items

        if (!handleReceiptUpload(customer, receiptFile, model)) {
            return reloadCheckoutPage(model, "Please upload a receipt for QR code payment.");
        }

        if (!handlePaymentMethod(customer, cartItems, model)) {
            return reloadCheckoutPage(model, "Please select a valid payment method.");
        }

        saveOrderAndSendEmail(customer, cartItems);
        return finalizeOrder(model);
    }

    // Reload checkout page with an error message
    private String reloadCheckoutPage(Model model, String errorMessage) {
        model.addAttribute(ERROR_ATTR, errorMessage);
        return CHECKOUT_VIEW;
    }

    // Handle QR Code receipt upload
    private boolean handleReceiptUpload(Customer customer, MultipartFile receiptFile, Model model) {
        if ("QR Code".equals(customer.getPaymentMethod())) {
            if (!receiptFile.isEmpty()) {
                try {
                    String receiptPath = fileUploadService.uploadFile(receiptFile);
                    customer.setPaymentReceiptPath(receiptPath);
                } catch (IOException e) {
                    model.addAttribute(ERROR_ATTR, "Failed to upload receipt. Please try again.");
                    return false;
                }
            } else {
                model.addAttribute(ERROR_ATTR, "Please upload a receipt for QR code payment.");
                return false;
            }
        }
        return true;
    }

    // Handle different payment methods
    private boolean handlePaymentMethod(Customer customer, List<BookDto> cartItems, Model model) {
        String paymentMethod = customer.getPaymentMethod();
        switch (paymentMethod) {
            case "QR Code":
                model.addAttribute(MESSAGE_ATTR, "Please scan the QR code to complete your payment.");
                break;
            case "Card":
                return handleCardPayment(cartItems, model);
            case "Cash on Delivery":
                model.addAttribute(MESSAGE_ATTR, "Your order will be paid upon delivery.");
                break;
            default:
                model.addAttribute(ERROR_ATTR, "Invalid payment method.");
                return false;
        }
        return true;
    }

    // Handle card payments
    private boolean handleCardPayment(List<BookDto> cartItems, Model model) {
        int totalAmount = customerService.totalPricesInCents(cartItems);
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(totalAmount);
            model.addAttribute("paymentIntentClientSecret", paymentIntent.getClientSecret());
            model.addAttribute(MESSAGE_ATTR, "Please confirm your card payment.");
        } catch (Exception e) {
            model.addAttribute(ERROR_ATTR, "Card payment processing failed. Please try again.");
            return false;
        }
        return true;
    }

    // Save order and send confirmation email
    private void saveOrderAndSendEmail(Customer customer, List<BookDto> cartItems) {
        customerService.saveCustomerBookOrder(customer, cartItems);

        String emailSubject = "Order Confirmation - Thank You for Your Purchase!";
        String emailBody = String.format(
                "Dear %s,\n\n" +
                        "Thank you for your purchase. Here are your order details:\n\n" +
                        "Name: %s\n" +
                        "Phone: %s\n" +
                        "Address: %s\n" +
                        "Email: %s\n\n" +
                        "We will send you a confirmation email soon.\n\n" +
                        "Best regards,\n" +
                        "Your Store",
                customer.getName(), customer.getName(), customer.getPhoneNumber(),
                customer.getAddress(), customer.getEmail());

        emailService.sendConfirmationEmail(customer.getEmail(), emailSubject, emailBody);
    }

    // Clear cart and return success message
    private String finalizeOrder(Model model) {
        cartService.clearCart();
        model.addAttribute("success", "Your order has been successfully placed.");
        return SUCCESS_VIEW;
    }

    @GetMapping("customer/checkout")
    public String checkOut(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("cartItems", cartService.listCart());

        try {
            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount(1000L)  // Replace with the total amount of the cart in cents
                    .setCurrency("usd")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            String clientSecret = paymentIntent.getClientSecret();
            model.addAttribute("paymentIntentClientSecret", clientSecret);
        } catch (StripeException e) {
            model.addAttribute(ERROR_ATTR, "Error creating payment intent: " + e.getMessage());
        }

        return CHECKOUT_VIEW;
    }


}