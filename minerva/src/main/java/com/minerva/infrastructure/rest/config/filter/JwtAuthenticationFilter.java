package com.minerva.infrastructure.rest.config.filter;

import com.minerva.infrastructure.rest.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.debug(" Procesando solicitud: {} {}",
                request.getMethod(), request.getRequestURI());

        final String authHeader = request.getHeader(AUTH_HEADER);

        //bearer
        // Si no se encunetra el Authorization entonces pasa como anonimo
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            log.debug("No se encontro token");
            filterChain.doFilter(request, response);
            return;
        }


        try {
            final String jwt = authHeader.substring(BEARER_PREFIX.length());
            final String username = jwtService.extractUsername(jwt);

            // Aqui evito autenticar dos veces
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                log.debug("extrayendo usuario del token: {}", username);

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (!userDetails.isEnabled() || !jwtService.isTokenValid(jwt, userDetails)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                log.debug("token valido para: {}", username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.info("User logeado: {} - Ruta: {}", username, request.getRequestURI());
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("Error en el filtro: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);

    }

}
