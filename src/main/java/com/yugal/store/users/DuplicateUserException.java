package com.yugal.store.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(@NotBlank(message = "Email cannot be blank") @Email(message = "Email must be valid") String message) {
        super(message);
    }
}
