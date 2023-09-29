package com.crmfoodestablishment.user_auth_service.auth_sevice.service.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;

public record RefreshToken(Header header, Claims claims) {}
