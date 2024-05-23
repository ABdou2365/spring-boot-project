package com.abde.customer;

import com.abde.error.DuplicatedRessourceException;
import com.abde.error.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getCustomers() {

        underTest.getCustomers();

        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        Long id = 1L;

        Customer customer = new Customer(
                id,
                "user",
                "user@email.com",
                21
        );

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        var actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);

    }


    @Test
    void willThrowWhenCustomerReturnEmptyOptional() {
        Long id = 1L;

        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id = [%s] Not found".formatted(id));

    }



    @Test
    void createCustomer() {
        String email = "user@email.com";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegestrationRequest request = new CustomerRegestrationRequest(
                "user",
                email,
                21
        );

        underTest.createCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer captoredCustomer = customerArgumentCaptor.getValue();


        assertThat(captoredCustomer.getName()).isEqualTo(request.name());
        assertThat(captoredCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captoredCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void willThrowWhenEmailExistsWhileCreatingACustomer() {
        String email = "user@email.com";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegestrationRequest request = new CustomerRegestrationRequest(
                "user",
                email,
                21
        );

        assertThatThrownBy(()->underTest.createCustomer(request))
                .isInstanceOf(DuplicatedRessourceException.class)
                        .hasMessage("email already taken");

        verify(customerDAO,never()).insertCustomer(any());
    }

    @Test
    void canUpdateAllCustomerProperties() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                "abde", newEmail, 22
        );

        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updatedCustomerRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updatedCustomerRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updatedCustomerRequest.age());
    }

    @Test
    void canUpdateOnlyNameCustomer() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                "abde", null, null
        );

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updatedCustomerRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyEmailCustomer() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                null, newEmail, null
        );

        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updatedCustomerRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyAgeCustomer() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21
        );
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                null, null, 22
        );

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updatedCustomerRequest.age());
    }


    @Test
    void deleteCustomer() {
        Long id = 1L;

        when(customerDAO.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomer(id);

        verify(customerDAO).deleteCustomerById(id);
    }

    @Test
    void willThrowWhenIdNotExistsWhileDeletingCustomer() {
        Long id = 1L;

        when(customerDAO.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("There's no Customer with id = [%s]".formatted(id));

        verify(customerDAO,never()).deleteCustomerById(id);
    }



}