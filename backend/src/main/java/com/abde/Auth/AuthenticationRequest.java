package com.abde.Auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
