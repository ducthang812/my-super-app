package com.example.my_super_app.auth.mapper;

import com.example.my_super_app.auth.dto.response.AccountResponse;
import com.example.my_super_app.auth.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getId(), account.getUsername(), account.getEmail());
    }
}
