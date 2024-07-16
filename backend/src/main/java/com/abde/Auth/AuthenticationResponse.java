package com.abde.Auth;

import com.abde.customer.CustomerDTO;

public record AuthenticationResponse (String token ,CustomerDTO customerDTO){
}
