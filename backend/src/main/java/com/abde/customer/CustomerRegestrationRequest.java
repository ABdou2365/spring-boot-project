package com.abde.customer;

public record CustomerRegestrationRequest(
        String name,
        String email,
        String password,
        Integer age,
        Gender gender
) {
}
