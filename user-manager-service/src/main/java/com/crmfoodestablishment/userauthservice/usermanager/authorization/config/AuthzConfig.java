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

import static com.crmfoodestablishment.userauthservice.usermanager.controller.AdminController.ADMIN_PATH;
import static com.crmfoodestablishment.userauthservice.usermanager.controller.ClientController.CLIENT_PATH;
import static com.crmfoodestablishment.userauthservice.usermanager.controller.ClientController.CLIENT_PATH_ID;
import static com.crmfoodestablishment.userauthservice.usermanager.controller.EmployeeController.EMPLOYEE_PATH;
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
                                ADMIN_PATH + "/**"
                        ).hasRole(Role.OWNER.name())
                        .requestMatchers(
                                EMPLOYEE_PATH + "/**"
                        ).access()
                        .hasRole(Role.ADMIN.name())
                        .requestMatchers(
                                HttpMethod.POST,
                                CLIENT_PATH
                        ).permitAll()
                        .requestMatchers(
                                CLIENT_PATH_ID
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.GET,
                                USER_PATH_ID
                        ).authenticated()
                        .requestMatchers(
                                HttpMethod.GET,
                                USER_PATH
                        ).hasRole(Role.EMPLOYEE.name())
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
