package org.example.controller;

import org.example.model.Currency;
import org.example.model.Order;
import org.example.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderRepository orderRepository;

    @Test
    public void getOrderByIdWhenOrderExistsTest() throws Exception {
        Currency currency = new Currency("USD");
        Order order = new Order("Test Order", "100.00", currency);
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Order"))
                .andExpect(jsonPath("$.amount").value("100.00"))
                .andExpect(jsonPath("$.currency.currency").value("USD"));
    }

    @Test
    public void getOrderByIdWhenOrderNotExistsTest() throws Exception {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOrdersByNameWhenOrdersExistTest() throws Exception {
        Currency currency1 = new Currency("USD");
        Currency currency2 = new Currency("EUR");

        Order order1 = new Order("Common Name", "100.00", currency1);
        Order order2 = new Order("Common Name", "200.00", currency2);

        when(orderRepository.findByName("Common Name"))
                .thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/api/orders/name/Common Name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Common Name"))
                .andExpect(jsonPath("$[0].amount").value("100.00"))
                .andExpect(jsonPath("$[0].currency.currency").value("USD"))
                .andExpect(jsonPath("$[1].name").value("Common Name"))
                .andExpect(jsonPath("$[1].amount").value("200.00"))
                .andExpect(jsonPath("$[1].currency.currency").value("EUR"));
    }

    @Test
    public void getOrdersByNameWhenNoOrdersExistTest() throws Exception {
        when(orderRepository.findByName("NonExistingName")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders/name/NonExistingName"))
                .andExpect(status().isNotFound());
    }
}