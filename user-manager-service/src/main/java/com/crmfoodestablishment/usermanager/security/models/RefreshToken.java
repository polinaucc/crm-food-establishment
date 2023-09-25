package com.crmfoodestablishment.usermanager.security.models;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;

public record RefreshToken(Header header, Claims claims) {}
