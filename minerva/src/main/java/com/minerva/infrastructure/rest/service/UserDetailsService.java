package com.minerva.infrastructure.rest.service;

import com.minerva.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final JpaUserRepository jpaUserRepository;

    public UserDetailsService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    // NOTA: EN el retrun se puede usar el Build de User.withUsername(usuario.getUsername).password(usuario.getPassowrd)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jpaUserRepository.findById(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("Usuario no encontrado");
                });
    }
}
