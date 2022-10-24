package com.ascendant76.redis;

import com.ascendant76.redis.data.AccountsSampleData;
import com.ascendant76.redis.data.OrdersSampleData;
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
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class RedisApplication implements CommandLineRunner {

    private AccountRepository accountRepository;
    private OrderRepository orderRepository;
    private RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        redisRepositoryTest(AccountsSampleData.accounts(5), OrdersSampleData.orders(10));
        redisTemplateTest();
    }

    private void redisRepositoryTest(Map<String, Account> accounts, Map<String, Order> orders) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        accounts.values().parallelStream().forEach(account -> accountRepository.save(account));
        stopwatch.stop();
        LOG.info("Account save all elapsed time {}", stopwatch.elapsed());

        stopwatch = Stopwatch.createStarted();
        accountRepository.findAll().forEach(account -> LOG.info("Got account:{}", account));
        stopwatch.stop();
        LOG.info("Get all Accounts elapsed time {}", stopwatch.elapsed());

        stopwatch = Stopwatch.createStarted();
        orders.values().parallelStream().forEach(order -> orderRepository.save(order));
        stopwatch.stop();
        LOG.info("Order save all elapsed time {}", stopwatch.elapsed());

        stopwatch = Stopwatch.createStarted();
        orderRepository.findAll().forEach(order -> LOG.info("Got order:{}", order));
        stopwatch.stop();
        LOG.info("Get all Orders elapsed time {}", stopwatch.elapsed());
    }

    private void redisTemplateTest() {
        // Value
        redisTemplate.opsForValue().set("test:set1", "testValue1");
        LOG.info("Value:{}", redisTemplate.opsForValue().get("test:set1"));

        // Set
        redisTemplate.opsForSet().add("test:vowelsSet", "A", "E", "I", "O", "U");
        LOG.info("A in set?:{}", redisTemplate.opsForSet().isMember("test:vowelsSet", "A"));

        // Hash
        redisTemplate.opsForHash().put("hash1", "name1", "lms1");
        redisTemplate.opsForHash().put("hash1", "name2", "lms2");
        redisTemplate.opsForHash().put("hash1", "name3", "lms3");
        LOG.info("hash1 key name1 is {}", redisTemplate.opsForHash().get("hash1", "name1"));
    }
}
