package com.ascendant76.redis.data;

import com.ascendant76.redis.entities.Account;
import com.ascendant76.redis.entities.Order;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class OrdersSampleData {

    public static Map<String, Order> orders(int count) {
        Map<String, Account> sampleAccounts = AccountsSampleData.accounts(5);
        return orders(count, sampleAccounts);
    }

    public static Map<String, Order> orders(int count, Map<String, Account> accounts) {
        return IntStream.range(0, count)
                .mapToObj(id -> order(id, accounts.get(String.valueOf(id % 5)))
                ).collect(Collectors.toMap(Order::getId, Function.identity()));
    }

    public static Order order(int id, Account account) {
        return Order.builder()
                .id(String.valueOf(id))
                .ticker("TEAM")
                .quantity(BigDecimal.TEN)
                .currency("USD")
                .price(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .tradeDate(LocalDate.now().minusDays(id % 10))
                .account(account)
                .build();
    }
}
