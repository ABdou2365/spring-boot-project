package com.abde.customer;

import com.abde.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJdbcDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(
                getJDBCtemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
            FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "."+ UUID.randomUUID(),
                21,
                Gender.MALE);
        underTest.insertCustomer(customer);

        List<Customer> actual = underTest.selectAllCustomers();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                21,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(email);
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        Long id = -1L;

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isTrue();

    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenEmailDoesNotExists() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                21,
                Gender.MALE);
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdDoesNotExists() {
        Long id = -1L;

        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                21,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var NewName = "abde";
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName(NewName);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(NewName);
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void updateCustomerEmail() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var NewEmail = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setEmail(NewEmail);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(NewEmail);
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void updateCustomerAge() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var NewAge = 22;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setAge(NewAge);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(NewAge);
                }
        );
    }

    @Test
    void willUpdateAllCustomersProperties() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName("abdellah");
        updatedCustomer.setEmail(FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID());
        updatedCustomer.setAge(30);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(updatedCustomer);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        String email = FAKER.internet().safeEmailAddress() + "." + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );


    }
}