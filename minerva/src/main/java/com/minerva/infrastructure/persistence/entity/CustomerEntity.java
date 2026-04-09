package com.minerva.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @Column(name = "customerNameId", length = 100)
    private String customerNameId;

    @Column(name = "phoneNumber", columnDefinition = "CHAR(9)", unique = true)
    private String phoneNumber;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}