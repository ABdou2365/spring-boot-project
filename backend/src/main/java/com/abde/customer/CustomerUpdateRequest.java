package com.abde.customer;

import java.util.Optional;

public record CustomerUpdateRequest(
        String name,
        String email,
        String password,
        Integer age,
        Gender gender
) {
}
