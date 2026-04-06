package com.zorvyn.financedata.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminDto {
    @NotNull(message = "user-name is required")
    private String userName;

    @NotNull(message = "email is required")
    private String email;

    @NotNull(message = "password is required")
    private String password;
}
