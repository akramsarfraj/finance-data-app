package com.zorvyn.financedata.service;

import com.zorvyn.financedata.dto.ResponseBody;
import com.zorvyn.financedata.dto.TransactionDto;
import com.zorvyn.financedata.exception.NoTransactionRecordFoundException;
import com.zorvyn.financedata.model.Transactions;
import com.zorvyn.financedata.repository.TransactionRepository;
import com.zorvyn.financedata.util.Category;
import com.zorvyn.financedata.util.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    public ResponseEntity<ResponseBody<Transactions>> createTransactionRecord(TransactionDto dto){

        Transactions transaction = new Transactions();
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setType(dto.getType());
        transaction.setTypeLabel(TransactionType.getTransactionLabel(dto.getType()));

        transaction.setCategory(dto.getCategory());
        transaction.setCategoryLabel(Category.getCategoryLabelByValue(dto.getCategory()));

        transaction.setNote(dto.getNote());

        Transactions saveTransactions = transactionRepository.save(transaction);

        ResponseBody<Transactions> responseBody = new ResponseBody<>();
        responseBody.setStatus(HttpStatus.OK.getReasonPhrase());
        responseBody.setMessage("Transaction record created.");
        responseBody.setData(saveTransactions);
        responseBody.setCurrentTimestamp(new Date());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    public ResponseEntity<ResponseBody<Transactions>> updateTransactionRecord(TransactionDto dto){
        Optional<Transactions> transactionsOptional = transactionRepository.findById(dto.getId());

        if(transactionsOptional.isPresent()){
            Transactions transactions = transactionsOptional.get();

            transactions.setAmount(dto.getAmount());
            transactions.setDate(dto.getDate());
            transactions.setType(dto.getType());
            transactions.setTypeLabel(TransactionType.getTransactionLabel(dto.getType()));

            transactions.setCategory(dto.getCategory());
            transactions.setCategoryLabel(Category.getCategoryLabelByValue(dto.getCategory()));

            transactions.setNote(dto.getNote());

            Transactions updatedTransaction = transactionRepository.save(transactions);

            ResponseBody<Transactions> responseBody = new ResponseBody<>();
            responseBody.setStatus(HttpStatus.OK.getReasonPhrase());
            responseBody.setMessage("Transaction record updated.");
            responseBody.setData(updatedTransaction);
            responseBody.setCurrentTimestamp(new Date());

            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }

        throw new NoTransactionRecordFoundException("No transaction record found for the Id - "+dto.getId());
    }

    public ResponseEntity<ResponseBody> deleteTransactionRecord(Long id){
        Optional<Transactions> optional = transactionRepository.findById(id);
        if (optional.isPresent()){

            transactionRepository.delete(optional.get());

            ResponseBody responseBody = new ResponseBody();
            responseBody.setStatus(HttpStatus.OK.getReasonPhrase());
            responseBody.setMessage("Transaction record deleted.");
            responseBody.setData(null);
            responseBody.setCurrentTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
        throw new NoTransactionRecordFoundException("No transaction record found for the Id - "+id);
    }

    public ResponseEntity<ResponseBody<?>> getAllTransactionRecords(int pageNumber, int pageSize){

        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Transactions> transactions = transactionRepository.findAll(pageable);

        ResponseBody<Page<Transactions>> responseBody = new ResponseBody<>();
        responseBody.setStatus(HttpStatus.OK.getReasonPhrase());
        responseBody.setMessage("Transaction records fetch.");
        responseBody.setData(transactions);
        responseBody.setCurrentTimestamp(new Date());

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);

    }

    public ResponseEntity<ResponseBody<Transactions>> getTransactionRecordById(Long id){

        Optional<Transactions> optional = transactionRepository.findById(id);
        if (optional.isPresent()){

            ResponseBody<Transactions> responseBody = new ResponseBody<>();
            responseBody.setStatus(HttpStatus.OK.getReasonPhrase());
            responseBody.setMessage("Transaction record deleted.");
            responseBody.setData(optional.get());
            responseBody.setCurrentTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }
        throw new NoTransactionRecordFoundException("No transaction record found for the Id - "+id);

    }
}
