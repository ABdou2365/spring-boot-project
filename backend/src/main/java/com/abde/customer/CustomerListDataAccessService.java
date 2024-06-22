package com.abde.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{

    static List<Customer> customers;

    static{
        customers = new ArrayList<>();

        Customer abde = new Customer(1L, "Abde", "abde@gmail.com", 21, Gender.MALE);

        customers.add(abde);

        Customer nabil = new Customer(2L, "Nabil", "nabil@gmail.com", 99, Gender.MALE);

        customers.add(nabil);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return  customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }



    @Override
    public void insertCustomer(Customer customer) {
            customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }


    @Override
    public boolean existsPersonWithId(Long id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }
}
