package com.crmfoodestablishment.auth.service.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;

public record RefreshToken(Header header, Claims claims) {}
