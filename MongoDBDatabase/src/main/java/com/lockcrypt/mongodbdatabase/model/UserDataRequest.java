package com.lockcrypt.mongodbdatabase.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDataRequest(
        @NotBlank String owner,
        @NotBlank String accountFor,
        @NotBlank String username,
        @NotBlank String password,
        @NotNull Integer keyId) {
}
