package com.abde.customer;

public record CustomerRegestrationRequest(
        String name,
        String email,
        Integer age,
        Gender gender
) {
}
