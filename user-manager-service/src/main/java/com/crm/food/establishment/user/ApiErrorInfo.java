package com.crm.food.establishment.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ApiErrorInfo {

    private String code;

    private String description;
}
