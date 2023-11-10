package com.crmfoodestablishment.userauthservice.usermanager.authorization.config;

import com.crmfoodestablishment.userauthservice.usermanager.authorization.filter.JwtAuthenticationFilter;
import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.crmfoodestablishment.userauthservice.usermanager.controller.UserController.USER_PATH;
import static com.crmfoodestablishment.userauthservice.usermanager.controller.UserController.USER_PATH_ID;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthzConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                HttpMethod.POST,
                                USER_PATH + "/customer"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                USER_PATH + "/**"
                        ).hasRole(Role.ADMIN.name())
                        .requestMatchers(
                                HttpMethod.PUT,
                                USER_PATH_ID + "/role"
                        ).hasRole(Role.ADMIN.name())
                        .requestMatchers(
                                HttpMethod.GET,
                                USER_PATH
                        ).hasRole(Role.ADMIN.name())
                        .requestMatchers(
                                USER_PATH_ID
                        ).authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
