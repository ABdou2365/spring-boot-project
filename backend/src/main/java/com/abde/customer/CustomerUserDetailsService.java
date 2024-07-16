package com.abde.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;

    public CustomerUserDetailsService(@Qualifier("jpa") CustomerDAO customerDAO, PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerDAO.selectCustomerByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("Username : " + username + " Not found")
        );
    }
}
