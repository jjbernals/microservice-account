package com.example.account.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRequest {

    private String name;
    private String username;
    private String password;

}
