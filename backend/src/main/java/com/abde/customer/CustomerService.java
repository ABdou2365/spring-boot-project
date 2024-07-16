package com.abde.customer;

import com.abde.error.BadRequestException;
import com.abde.error.DuplicatedRessourceException;
import com.abde.error.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {


    private final CustomerDAO customerDAO;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;


    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {

        this.customerDAO = customerDAO;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getCustomers() {
        return customerDAO.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long id) {
        return customerDAO.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(
                () -> new ResourceNotFoundException("Customer with id = [%s] Not found".formatted(id))
        );
    }

    public void createCustomer(CustomerRegestrationRequest customerRegestrationRequest) {
        //Check if the email is already there
        if(customerDAO.existsPersonWithEmail(customerRegestrationRequest.email())){
            throw new DuplicatedRessourceException("email already taken");
        }

        Customer customer = new Customer(
                customerRegestrationRequest.name(),
                customerRegestrationRequest.email(),
                passwordEncoder.encode(customerRegestrationRequest.password()),
                customerRegestrationRequest.age(),
                customerRegestrationRequest.gender());

        //save

         customerDAO.insertCustomer(customer);

    }

    public void updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest) {


        Customer customer = customerDAO.selectCustomerById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer with id = [%s] Not found".formatted(id)));

        boolean changes = false;

        if(customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())){
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }
        if(customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())){
            if(customerDAO.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicatedRessourceException("email already taken");
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if(customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())){
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if(!changes){
            throw new BadRequestException("No changes made");
        }

        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer(Long id) {
        //Check if the customer exists

        if(!customerDAO.existsPersonWithId(id)){
            throw new ResourceNotFoundException("There's no Customer with id = [%s]".formatted(id));
        }

        //Delete the customer
        customerDAO.deleteCustomerById(id);
    }

}
