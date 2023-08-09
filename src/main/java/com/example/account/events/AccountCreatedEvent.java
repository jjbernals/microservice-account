package com.example.account.events;

import com.example.account.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountCreatedEvent extends Event<Account> {

}
