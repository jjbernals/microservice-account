package com.example.account.service;

import com.example.account.entity.Account;
import com.example.account.events.AccountCreatedEvent;
import com.example.account.events.Event;
import com.example.account.events.EventType;
import com.example.account.exception.AccountNotFoundException;
import com.example.account.model.AccountRequest;
import com.example.account.model.AccountResponse;
import com.example.account.model.IdGame;
import com.example.account.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, Event<?>> producer;


    public AccountServiceImpl(AccountRepository accountRepository, KafkaTemplate<String, Event<?>> producer) {
        this.accountRepository = accountRepository;
        this.producer = producer;
    }

    @Value("${topic.account.name:accounts}")
    private String topicCustomer;

    @Override
    public void createAccount(AccountRequest accountRequest) {
        String game = "[]";
        accountRepository.save(Account.builder().name(accountRequest.getName()).username(accountRequest.getUsername())
                .password(accountRequest.getPassword()).status(true).idGame(game).build());

    }

    @Override
    public List<AccountResponse> getAllAccounts() {

        List<AccountResponse> list = accountRepository.findAll().stream()
                .map(account ->
                        AccountResponse.builder()
                                .id(account.getId()).name(account.getName()).username(account.getUsername()).password(account.getPassword()).build()).toList();
        return list;

    }

    @Override
    public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("Account with id: "+id+" not found"));
        return AccountResponse.builder().id(account.getId()).name(account.getName()).username(account.getUsername()).password(account.getPassword()).build();

    }

    @Override
    public String getUsername(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("Account with id: "+id+" not found"));
        return account.getUsername();
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("Account with id: "+id+" not found"));
        account.setStatus(false);

        accountRepository.save(account);

    }

    @Override
    public void updateAccountById(AccountRequest accountRequest, Long id) {
        accountRepository.save(Account.builder().id(id).name(accountRequest.getName()).password(accountRequest.getPassword())
                .username(accountRequest.getUsername()).status(true).build());
    }

    @Override
    public void updateAccountByUsername(AccountRequest accountRequest, String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(()->new AccountNotFoundException("Account with username: "+username+" not found"));

        accountRepository.save(Account.builder().id(account.getId()).name(accountRequest.getName()).password(accountRequest.getPassword())
                .username(accountRequest.getUsername()).status(true).build());    }

    @Override
    public List<HashMap<String, String>> getAllGamesTheAnAccount(Long id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account idGame = accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("Account with id: "+id+" not found"));
        List<HashMap<String, String>> map = objectMapper.readValue(idGame.getIdGame(), List.class);

        return map;
    }

    @Override
    public void linkAGameWithAnAccount(Long id, IdGame idGame) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap<String, String>> map = getAllGamesTheAnAccount(id);
        HashMap<String, String> newGame = new HashMap<>();
        newGame.put("id", idGame.getId());
        map.add(newGame);

        String newList = objectMapper.writeValueAsString(map);

        Account account = accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("Account with id: "+id+" not found"));
        account.setIdGame(newList);
        accountRepository.save(account);

        AccountCreatedEvent created = new AccountCreatedEvent();
        created.setData(account);
        created.setId(UUID.randomUUID().toString());
        created.setType(EventType.CREATED);
        created.setDate(new Date());

        this.producer.send(topicCustomer, created);
    }


}
