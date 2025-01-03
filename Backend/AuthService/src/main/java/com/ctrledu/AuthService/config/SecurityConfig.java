package com.ctrledu.AuthService.config;

import com.ctrledu.AuthService.interceptor.RequestContextFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Configuration
public class SecurityConfig {

    private final String jwkSetUri = "http://keycloak:8080/realms/CtrlEdu/protocol/openid-connect/certs";

    @Bean(name = "authSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Qualifier("authJwtDecoder") JwtDecoder jwtDecoder,
                                                   @Qualifier("authJwtAuthenticationConverter") JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {

        http
                .securityMatcher("/api/auth/**")
                .securityMatcher("/api/admin/**")
                .csrf().disable()
                .addFilterAfter(new RequestContextFilter(), BearerTokenAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter); // Explicitly using the qualified bean

        return http.build();
    }

    @Bean(name = "authJwtDecoder")
    public JwtDecoder authJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean(name = "authJwtAuthenticationConverter")
    public JwtAuthenticationConverter authJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            if (realmAccess == null || realmAccess.get("roles") == null) {
                return Collections.emptyList();
            }
            List<String> roles = (List<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
        return converter;
    }

    @Bean(name = "customRequestContextFilter")
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }
}
