package org.example.repository;

import org.example.model.Currency;
import org.example.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void findByIdWhenOrderExistTest() {
        Currency currency = new Currency("USD");
        Order order = new Order("Test Order", "100.00", currency);

        Order savedOrder = orderRepository.save(order);

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getName()).isEqualTo("Test Order");
        assertThat(foundOrder.get().getAmount()).isEqualTo("100.00");
        assertThat(foundOrder.get().getCurrency().getCurrency()).isEqualTo("USD");
    }

    @Test
    public void findByIdWhenOrderNotExistsTest() {
        Optional<Order> foundOrder = orderRepository.findById(999L);

        assertThat(foundOrder).isEmpty();
    }

    @Test
    public void findByNameWhenOrdersExistTest() {
        Currency currency1 = new Currency("USD");
        Currency currency2 = new Currency("EUR");

        Order order1 = new Order("Just Name", "100.00", currency1);
        Order order2 = new Order("Just Name", "200.00", currency2);
        Order order3 = new Order("Another Name", "300.00", currency1);

        orderRepository.saveAll(List.of(order1, order2, order3));

        List<Order> foundOrders = orderRepository.findByName("Just Name");

        assertThat(foundOrders).hasSize(2);
    }

    @Test
    public void findByNameWhenNoOrdersExistTest() {
        Currency currency = new Currency("USD");
        Order order = new Order("Existing Order", "100.00", currency);
        orderRepository.save(order);

        List<Order> foundOrders = orderRepository.findByName("Not Existing Name");

        assertThat(foundOrders).isEmpty();
    }
}