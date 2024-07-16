package com.abde.customer;

import com.abde.AbstractTestContainers;
import com.abde.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 21,
                Gender.MALE);
        underTest.save(customer);


        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailDoesNotExist() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }



    @Test
    void existsCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 21,
                Gender.MALE);
        underTest.save(customer);

        Long id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdDoesNotExist() {

        Long id = -1L;

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }

}