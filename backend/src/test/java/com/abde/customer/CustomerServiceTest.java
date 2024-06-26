package com.abde.customer;

import com.abde.error.BadRequestException;
import com.abde.error.DuplicatedRessourceException;
import com.abde.error.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
                21,
                Gender.MALE);

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
                21,
                Gender.MALE
        );

        underTest.createCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer captoredCustomer = customerArgumentCaptor.getValue();


        assertThat(captoredCustomer.getName()).isEqualTo(request.name());
        assertThat(captoredCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captoredCustomer.getAge()).isEqualTo(request.age());
        assertThat(captoredCustomer.getGender()).isEqualTo(request.gender());

    }

    @Test
    void willThrowWhenEmailExistsWhileCreatingACustomer() {
        String email = "user@email.com";

        when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegestrationRequest request = new CustomerRegestrationRequest(
                "user",
                email,
                21,
                Gender.MALE
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
                id, "user", "user@email.com", 21,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                "abde", newEmail, 22, Gender.MALE
        );

        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updatedCustomerRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updatedCustomerRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updatedCustomerRequest.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(updatedCustomerRequest.gender());
    }

    @Test
    void canUpdateOnlyNameCustomer() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                "abde", null, null, null
        );

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updatedCustomerRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                null, newEmail, null, null
        );

        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updatedCustomerRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                null, null, 22, null
        );

        underTest.updateCustomer(id,updatedCustomerRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updatedCustomerRequest.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
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

    @Test
    void willThrowExceptionWhenTryingUpdatingWhenEmailAlreadyTaken() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                null, newEmail, null,null
        );

        when(customerDAO.existsPersonWithEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(()->underTest.updateCustomer(id,updatedCustomerRequest))
                .isInstanceOf(DuplicatedRessourceException.class)
                        .hasMessage("email already taken");


        verify(customerDAO , never()).updateCustomer(any());

    }

    @Test
    void willThrowExceptionWhenThereIsChanges() {
        Long id = 1L;

        Customer customer = new Customer(
                id, "user", "user@email.com", 21,
                Gender.MALE);
        when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "abde@email.com";
        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge(),customer.getGender()
        );

        assertThatThrownBy(()->underTest.updateCustomer(id,updatedCustomerRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No changes made");

        verify(customerDAO,never()).updateCustomer(any());

    }

}