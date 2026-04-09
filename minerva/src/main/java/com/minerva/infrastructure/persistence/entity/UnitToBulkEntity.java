package com.minerva.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "unitToBulk")
public class UnitToBulkEntity {

    @EmbeddedId
    private UnitToBulkId id;

    @ManyToOne
    @MapsId("unitProductNameId")  // Vincula FK con la PK embebida
    @JoinColumn(name = "unitProductNameId", nullable = false)
    private ProductEntity unitProductEntity;

    @ManyToOne
    @MapsId("bulkProductNameId")  // Vincula FK con la PK embebida
    @JoinColumn(name = "bulkProductNameId", nullable = false, unique = true)
    private ProductEntity bulkProductEntity;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class UnitToBulkId implements Serializable {

        @Column(name = "unitProductNameId", length = 100, nullable = false)
        private String unitProductNameId;

        @Column(name = "bulkProductNameId", length = 100, nullable = false)
        private String bulkProductNameId;
    }
}
