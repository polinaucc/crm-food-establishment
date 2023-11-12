package com.crmfoodestablishment.coreservice.config;

import com.crmfoodestablishment.coreservice.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] roles = Arrays.stream(Role.values())
                .map(Enum::name)
                .toArray(String[]::new);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, "/api/v1/menu/**", "/api/v1/menu").hasAnyAuthority(roles)
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/menu/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu/**").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}