package com.example.account.controller;

import com.example.account.entity.Account;
import com.example.account.model.AccountRequest;
import com.example.account.model.AccountResponse;
import com.example.account.model.IdGame;
import com.example.account.service.AccountServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts(){ return accountService.getAllAccounts(); }

    @GetMapping("/get/{id}")
    public AccountResponse getAccountById(@PathVariable Long id){ return accountService.getAccountById(id); }

    @GetMapping("/username/{id}")
    public String getUsername(@PathVariable Long id){ return accountService.getUsername(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountRequest accountRequest){ accountService.createAccount(accountRequest); }

    @PatchMapping("/updateById/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountById(@PathVariable Long id, @RequestBody AccountRequest accountRequest){ accountService.updateAccountById(accountRequest,id); }

    @PatchMapping("/updateByUsername/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountByUsername(@PathVariable String username, @RequestBody AccountRequest accountRequest){ accountService.updateAccountByUsername(accountRequest, username); }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountById(@PathVariable Long id){accountService.deleteAccount(id);}

    @GetMapping("/getAllGamesLinked/{id}")
    public List<HashMap<String, String>> getAllGamesLinked(@PathVariable Long id) throws JsonProcessingException {
        return accountService.getAllGamesTheAnAccount(id);}

    @PostMapping("/linkAGameWithAnAccount/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void linkAGameWithAnAccount(@PathVariable Long id, @RequestBody IdGame idGame) throws JsonProcessingException {
        accountService.linkAGameWithAnAccount(id, idGame);
    }

}
