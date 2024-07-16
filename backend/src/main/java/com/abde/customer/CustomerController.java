package com.abde.customer;

import com.abde.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtils;

    private CustomerController(CustomerService customerService, JWTUtil jwtUtils) {
        this.customerService = customerService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("{customerId}")
    public CustomerDTO getCustomer(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRegestrationRequest request) {
        customerService.createCustomer(request);
        String token = jwtUtils.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,token).build();
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Long customerId,@RequestBody CustomerUpdateRequest request){
        customerService.updateCustomer(customerId,request);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
    }
}
