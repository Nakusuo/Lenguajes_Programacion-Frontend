package com.minerva.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale")
public class SaleEntity {

    @Id
    @Column(name = "saleId")
    private UUID saleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customerNameId", nullable = false)
    private CustomerEntity customerEntity;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}

