package com.abde.customer;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();

    Optional<Customer> selectCustomerById(Long id);

    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);


    boolean existsPersonWithId(Long id);

    void deleteCustomerById(Long id);


    void updateCustomer(Customer customer);
}
