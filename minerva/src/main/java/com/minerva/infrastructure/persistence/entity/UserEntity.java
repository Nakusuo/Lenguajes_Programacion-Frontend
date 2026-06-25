package com.minerva.infrastructure.persistence.entity;

import com.minerva.domain.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "userName", length = 30, nullable = false, unique = true)
    private String userName;

    @Column(name = "DNI", columnDefinition = "CHAR(8)")
    private String dni;

    @Column(name = "names", length = 50, nullable = false)
    private String names;

    @Column(name = "lastNames", length = 50, nullable = false)
    private String lastNames;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "roleName", length = 50, nullable = false)
    private Role role;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    @Column(
            name = "registrationDate",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}