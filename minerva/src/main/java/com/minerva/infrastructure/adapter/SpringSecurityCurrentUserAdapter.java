package com.minerva.infrastructure.adapter;

import com.minerva.application.port.driven.CurrentUserProvider;
import com.minerva.application.port.driven.UserContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityCurrentUserAdapter implements CurrentUserProvider {

    @Override
    public UserContext currentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new IllegalStateException("No authentication found");
        }

        if (!(auth instanceof UsernamePasswordAuthenticationToken authToken)) {
            throw new IllegalStateException("Unsupported authentication type");
        }

        UserDetails user =
                (UserDetails) authToken.getPrincipal();

        if (user == null) {
            throw new IllegalStateException("User not found in authentication");
        }

        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No role found"));

        return new UserContext(
                user.getUsername(),
                role
        );
    }
}
