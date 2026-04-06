package com.zorvyn.financedata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBody<T> {
    private Date currentTimestamp;
    private String message;
    private T data;

    private String status;


}
