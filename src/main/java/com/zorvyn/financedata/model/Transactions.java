package com.zorvyn.financedata.model;

import com.zorvyn.financedata.util.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Setter
@Getter
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private String typeLabel;

    @Column(nullable = false)
    private int type;

    private int category;

    private String categoryLabel;

    private LocalDate date;

    @Column(length = 500)
    private String note;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

}
