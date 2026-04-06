package com.zorvyn.financedata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zorvyn.financedata.util.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "user-detail")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String roleLabel;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles> role;

    private boolean isActive;

    private boolean isDelete = false; // for soft delete

    private LocalDateTime deleteAt;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
