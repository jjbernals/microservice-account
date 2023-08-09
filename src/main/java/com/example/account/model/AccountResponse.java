package com.example.account.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Boolean status;


}
