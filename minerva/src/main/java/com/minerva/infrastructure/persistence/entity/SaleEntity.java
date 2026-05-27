package com.minerva.infrastructure.persistence.entity;

import jakarta.persistence.*;
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
@Table(name = "sale")
public class SaleEntity {

    @Id
    @Column(name = "saleId")
    private String saleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customerNameId", nullable = false)
    private CustomerEntity customerEntity;

    @Column(
            name = "registrationDate",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}

