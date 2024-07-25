package com.abde.Auth;

import com.abde.customer.Customer;
import com.abde.customer.CustomerDTO;
import com.abde.customer.CustomerDTOMapper;
import com.abde.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, CustomerDTOMapper customerDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        Customer customer = (Customer) authentication.getPrincipal();
        CustomerDTO customerDto = customerDTOMapper.apply(customer);
        String token = jwtUtil.issueToken(customerDto.username(), customerDto.roles());

        return new AuthenticationResponse(token,customerDto);
    }
}
