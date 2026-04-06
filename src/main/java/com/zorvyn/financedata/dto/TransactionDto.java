package com.zorvyn.financedata.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class TransactionDto {

    private Long id;

    @Min(value = 0,message = "amount must be either 0 or greater than 0")
    private double amount;

    @Min(value = 0 , message = "type must either 0(income) and 1(expense)")
    @Max(value = 1,message = "type must either 0(income) or 1(expense)")
    private int type;

    private int category;

    @NotNull(message = "date is required")
    @PastOrPresent(message = "date cannot be in the future")
    private LocalDate date;

    @Size(max = 500, message = "note cannot exceed 500 characters")
    private String note;
}
