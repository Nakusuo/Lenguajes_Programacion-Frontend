package com.minerva.infrastructure.rest.service;

import com.minerva.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private JpaUserRepository jpaUserRepository;

    public UserDetailsServiceImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jpaUserRepository.findById(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Usuario NO Encontrado!");
                });
    }
}
