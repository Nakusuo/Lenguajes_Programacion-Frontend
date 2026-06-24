package com.minerva.infrastructure.rest.config;

import com.minerva.infrastructure.rest.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        return httpSecurity
                // API stateless, no cookies
                .csrf(AbstractHttpConfigurer::disable)

                // permite llamadas desde frontend
                .cors(Customizer.withDefaults())

                // no guardar sesión
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(
//                List.of("http://localhost:3000")
//        );
//
//        config.setAllowedMethods(
//                List.of("GET", "POST", "PUT", "DELETE")
//        );
//
//        config.setAllowedHeaders(
//                List.of("*")
//        );
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration(
//                "/**",
//                config
//        );
//
//        return source;
//    }
}
