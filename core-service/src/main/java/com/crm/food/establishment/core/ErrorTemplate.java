package com.crm.food.establishment.core;

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
