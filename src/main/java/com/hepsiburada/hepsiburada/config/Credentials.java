package com.hepsiburada.hepsiburada.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    public String username;
    public String password;
    public String merchantId;
}
