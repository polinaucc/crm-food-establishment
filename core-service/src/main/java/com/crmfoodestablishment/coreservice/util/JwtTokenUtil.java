package com.crmfoodestablishment.coreservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<Role> getRolesFromToken(String token) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> roles = getAllClaimsFromToken(token).get("roles", List.class);
        return roles.stream()
                .map(role -> mapper.convertValue(role, Role.class))
                .collect(Collectors.toList());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}