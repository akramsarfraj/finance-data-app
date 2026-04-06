package com.zorvyn.financedata.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @NotNull(message = "user-name is required")
    private String userName;

    @NotNull(message = "email is required")
    private String email;

    @NotNull(message = "password is required")
    private String password;

    @Min(value = 1 , message = "roles must be - 1('ADMIN'),2('ANALYST'),3('USER')  ")
    @Max(value = 3, message = "roles must be - 1('ADMIN'),2('ANALYST'),3('USER') ")
    private int role;

}
