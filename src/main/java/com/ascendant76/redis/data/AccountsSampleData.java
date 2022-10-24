package com.ascendant76.redis.data;

import com.ascendant76.redis.entities.Account;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class AccountsSampleData {

    public static Map<String, Account> accounts(int count) {
        return IntStream.range(0, count)
                .mapToObj(id -> account(id)
                ).collect(Collectors.toMap(Account::getId, Function.identity()));
    }

    public static Account account(int id) {
        return Account.builder()
                .accountType(Account.AccountType.PERSONAL)
                .id(String.valueOf(id))
                .name("Name " + id)
                .build();
    }
}
