package com.example.account.service;

import com.example.account.entity.Account;
import com.example.account.model.AccountRequest;
import com.example.account.model.AccountResponse;
import com.example.account.model.IdGame;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.List;

public interface AccountService {

    void createAccount(AccountRequest accountRequest);
    List<AccountResponse> getAllAccounts();
    AccountResponse getAccountById(Long id);
    String getUsername(Long id);
    void deleteAccount(Long id);
    void updateAccountById(AccountRequest accountRequest, Long id);
    void updateAccountByUsername(AccountRequest accountRequest, String username);
    List<HashMap<String, String>>getAllGamesTheAnAccount(Long id) throws JsonProcessingException;
    void linkAGameWithAnAccount(Long id, IdGame idGame) throws JsonProcessingException;


}
