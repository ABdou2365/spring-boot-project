package com.abde.customer;

import java.util.Optional;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age,
        Gender gender
) {
}
