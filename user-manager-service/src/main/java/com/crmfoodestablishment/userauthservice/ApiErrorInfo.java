package com.crmfoodestablishment.userauthservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ApiErrorInfo {

    private String title;

    private HttpStatus status;

    private String description;
}
