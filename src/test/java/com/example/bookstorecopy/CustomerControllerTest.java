package com.example.bookstorecopy;

import com.example.bookstorecopy.controller.CustomerController;
import com.example.bookstorecopy.entities.BookDto;
import com.example.bookstorecopy.entities.Customer;
import com.example.bookstorecopy.service.CartService;
import com.example.bookstorecopy.service.CustomerService;
import com.example.bookstorecopy.service.FileUploadService;
import com.example.bookstorecopy.service.PaymentService;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CartService cartService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    public void testSaveCustomerOrder_SuccessfulOrderWithCardPayment() throws Exception {
        // Prepare the customer object
        Customer customer = new Customer();
        customer.setPaymentMethod("Card");

        // Mock the cart items
        List<BookDto> cartItems = new ArrayList<>();
        when(cartService.listCart()).thenReturn(cartItems);

        // Mock the payment service to return a valid PaymentIntent
        PaymentIntent paymentIntent = mock(PaymentIntent.class);
        when(paymentService.createPaymentIntent(1000)).thenReturn(paymentIntent);
        when(paymentIntent.getClientSecret()).thenReturn("testClientSecret");

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/save")
                        .flashAttr("customer", customer)
                        .param("paymentMethod", "Card"))
                .andExpect(status().isOk())
                .andExpect(view().name("Success"))
                .andExpect(model().attribute("paymentIntentClientSecret", "testClientSecret"))
                .andExpect(model().attribute("message", "Please confirm your card payment."));
    }

    @Test
    public void testSaveCustomerOrder_ErrorOnPaymentReceiptUpload() throws Exception {
        // Prepare the customer object with QR Code payment
        Customer customer = new Customer();
        customer.setPaymentMethod("QR Code");

        // Create an empty file (simulate no file uploaded)
        MockMultipartFile receiptFile = new MockMultipartFile("receiptFile", "", "text/plain", new byte[0]);

        // Mock the cart items
        List<BookDto> cartItems = new ArrayList<>();
        when(cartService.listCart()).thenReturn(cartItems);

        // Perform the POST request with an empty receipt
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/customer/save")
                        .file(receiptFile)
                        .flashAttr("customer", customer)
                        .param("paymentMethod", "QR Code"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkOut"))
                .andExpect(model().attribute("error", "Please upload a receipt for QR code payment."));
    }

    @Test
    public void testSaveCustomerOrder_MissingPaymentMethod() throws Exception {
        // Prepare the customer object with no payment method
        Customer customer = new Customer();

        // Mock the cart items
        List<BookDto> cartItems = new ArrayList<>();
        when(cartService.listCart()).thenReturn(cartItems);

        // Perform the POST request without specifying the payment method
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/save")
                        .flashAttr("customer", customer)
                        .param("paymentMethod", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("checkOut"))
                .andExpect(model().attribute("error", "Please select a valid payment method."));
    }

    @Test
    public void testSaveCustomerOrder_SuccessfulOrderWithCashOnDelivery() throws Exception {
        // Prepare the customer object
        Customer customer = new Customer();
        customer.setPaymentMethod("Cash on Delivery");

        // Mock the cart items
        List<BookDto> cartItems = new ArrayList<>();
        when(cartService.listCart()).thenReturn(cartItems);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/save")
                        .flashAttr("customer", customer)
                        .param("paymentMethod", "Cash on Delivery"))
                .andExpect(status().isOk())
                .andExpect(view().name("Success"))
                .andExpect(model().attribute("message", "Your order will be paid upon delivery."));
    }

    @Test
    public void testSaveCustomerOrder_BindingErrors() throws Exception {
        // Prepare the customer object with binding errors
        Customer customer = new Customer();
        customer.setPaymentMethod("Card");

        // Mock binding result
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/save")
                        .flashAttr("customer", customer)
                        .param("paymentMethod", "Card"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkOut"))
                .andExpect(model().attribute("error", "error"));
    }
}
