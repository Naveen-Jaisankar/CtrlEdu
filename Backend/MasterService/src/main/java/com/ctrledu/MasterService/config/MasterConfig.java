package com.ctrledu.MasterService.config;

import com.ctrledu.MasterService.interceptor.RequestFilter;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MasterConfig {

    private final String jwkSetUri = "http://localhost:8080/realms/CtrlEdu/protocol/openid-connect/certs";

    @Bean(name = "masterSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Qualifier("masterJwtDecoder") JwtDecoder jwtDecoder,
                                                   @Qualifier("masterJwtAuthenticationConverter") JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .securityMatcher("/api/client/**")
                .csrf().disable()
                .addFilterAfter(new RequestFilter(), BearerTokenAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/api/client/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter); // Explicitly using the qualified bean

        return http.build();
    }

    @Bean(name = "masterJwtDecoder")
    public JwtDecoder masterJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean(name = "masterJwtAuthenticationConverter")
    public JwtAuthenticationConverter masterJwtAuthenticationConverter() {
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

    @Bean(name = "customRequestFilter")
    public RequestFilter requestFilter() {
        return new RequestFilter();
    }
}
