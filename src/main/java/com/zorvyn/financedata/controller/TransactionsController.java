package com.zorvyn.financedata.controller;

import com.zorvyn.financedata.dto.ResponseBody;
import com.zorvyn.financedata.dto.TransactionDto;
import com.zorvyn.financedata.model.Transactions;
import com.zorvyn.financedata.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create-record")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<Transactions>> createRecord(
            @Valid @RequestBody TransactionDto dto){
        return transactionService.createTransactionRecord(dto);
    }


    @PutMapping("/update-record")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody<Transactions>> updateRecord(@Valid @RequestBody TransactionDto dto){
        return transactionService.updateTransactionRecord(dto);
    }

    @DeleteMapping("/delete-record")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseBody> deleteRecord(@RequestParam Long id){
        return transactionService.deleteTransactionRecord(id);
    }

    @GetMapping("/get-all-record")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<ResponseBody<?>> getAllRecords(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ){
        return transactionService.getAllTransactionRecords(pageNumber,pageSize);
    }

    @GetMapping("/get-record")
    public ResponseEntity<ResponseBody<Transactions>> getRecordById(@RequestParam Long id){
        return transactionService.getTransactionRecordById(id);
    }
}
