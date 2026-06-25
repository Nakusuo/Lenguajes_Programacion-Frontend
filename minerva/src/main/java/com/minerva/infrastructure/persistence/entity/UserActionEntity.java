package com.minerva.infrastructure.persistence.entity;

import com.minerva.domain.constants.Permission;
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
@Table(name = "userAction")
public class UserActionEntity {

    @Id
    @Column(name = "userActionId", length = 36, nullable = false)
    private String userActionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userName", referencedColumnName = "userName")
    private UserEntity userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", length = 50, nullable = false)
    private Permission permission;

    @Column(name = "entityId", length = 100, nullable = false)
    private String entityId;

    @Column(name = "registrationDate", nullable = false)
    private LocalDateTime registrationDate;

}
