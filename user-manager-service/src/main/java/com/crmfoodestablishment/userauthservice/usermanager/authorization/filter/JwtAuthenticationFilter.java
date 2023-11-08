package com.crmfoodestablishment.userauthservice.usermanager.authorization.filter;

import com.crmfoodestablishment.userauthservice.authservice.service.JwtService;
import com.crmfoodestablishment.userauthservice.authservice.token.AccessToken;
import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String AUTHZ_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authzHeader = request.getHeader("Authorization");

        if (authzHeader == null || authzHeader.startsWith(AUTHZ_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        AccessToken accessToken = jwtService.parseAccessToken(
                authzHeader.substring(AUTHZ_HEADER_PREFIX.length())
        );
        authenticateIfCurrentlyNot(accessToken, request);

        filterChain.doFilter(request, response);
    }

    private void authenticateIfCurrentlyNot(AccessToken accessToken, HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    accessToken.claims().sub(),
                    null,
                    accessToken.claims().roles().stream()
                            .map(this::convertRoleToSimpleGrantedAuthority)
                            .toList()
            );
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    private SimpleGrantedAuthority convertRoleToSimpleGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority("ROLE_" + role.name());
    }
}
