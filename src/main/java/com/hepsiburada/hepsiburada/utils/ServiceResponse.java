package com.hepsiburada.hepsiburada.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private String message;
    private Object data;
    private Boolean status;
    private String statusCode;

}
