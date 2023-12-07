package com.crmfoodestablishment.paymentservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
public class ErrorTemplate {

    private LocalDateTime currentTime;
    private int status;
    private String error;
    private Map<String, String> errors;
}
