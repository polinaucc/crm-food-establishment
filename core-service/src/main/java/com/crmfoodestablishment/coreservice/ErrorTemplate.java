package com.crmfoodestablishment.coreservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ErrorTemplate {
    private String title;
    private List<String> errors;
}
