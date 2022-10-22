package com.ascendant76.redis;

import com.ascendant76.redis.entities.Account;
import com.ascendant76.redis.entities.Order;
import com.ascendant76.redis.respository.AccountRepository;
import com.ascendant76.redis.respository.OrderRepository;
import com.google.common.base.Stopwatch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class RedisApplication implements CommandLineRunner {

    private AccountRepository accountRepository;
    private OrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(String... args) {

        int totalAccounts = 5;
        Map<String, Account> accounts = IntStream.range(0, totalAccounts)
                .mapToObj(id -> Account.builder()
                        .accountType(Account.AccountType.PERSONAL)
                        .id(String.valueOf(id))
                        .name("Name " + id)
                        .build()
                ).collect(Collectors.toMap(Account::getId, Function.identity()));

        int totalOrders = 500_000;
        Map<String, Order> orders = IntStream.range(0, totalOrders)
                .mapToObj(id -> Order.builder()
                        .id(String.valueOf(id))
                        .account(accounts.get(String.valueOf(id % 5)))
                        .currency("USD")
                        .build()
                ).collect(Collectors.toMap(Order::getId, Function.identity()));

        LOG.info("Prepared the test data");

        Stopwatch stopwatch = Stopwatch.createStarted();
        accounts.values().parallelStream().forEach(account -> accountRepository.save(account));
        stopwatch.stop();
        LOG.info("Account save all elapsed time {}", stopwatch.elapsed());


        stopwatch = Stopwatch.createStarted();
        accountRepository.findAll();
        stopwatch.stop();
        LOG.info("Get all Accounts elapsed time {}", stopwatch.elapsed());


        stopwatch = Stopwatch.createStarted();
        orders.values().parallelStream().forEach(order -> orderRepository.save(order));
        stopwatch.stop();
        LOG.info("Order save all elapsed time {}", stopwatch.elapsed());

        stopwatch = Stopwatch.createStarted();
        orderRepository.findAll().forEach(item -> {
        });
        stopwatch.stop();
        LOG.info("Get all Orders elapsed time {}", stopwatch.elapsed());
    }
}
