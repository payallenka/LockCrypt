package com.lockcrypt.mysqlhandle.model;

import jakarta.validation.constraints.NotBlank;

public record PasswordKeyRequest(@NotBlank String owner, @NotBlank String passwordKey) {
}
