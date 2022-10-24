package com.ascendant76.redis.support;

import com.ascendant76.redis.data.AccountsSampleData;
import com.ascendant76.redis.data.OrdersSampleData;
import com.ascendant76.redis.entities.Account;
import com.ascendant76.redis.entities.Order;
import com.ascendant76.redis.support.kryo.KryoSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class KryoSupportTest {

    private static Account account;
    private static Order order;
    private static Map<String, Account> accountsMap;
    private static Map<String, Order> ordersMap;

    @BeforeAll
    public static void init() {
        account = AccountsSampleData.account(1);
        order = OrdersSampleData.order(1, account);

        accountsMap = AccountsSampleData.accounts(5);
        ordersMap = OrdersSampleData.orders(500_000);
    }

    @Test
    public void verifyKryoSerializationAccountTest() {
        Account generated = KryoSupport.deserialize(KryoSupport.serialize(account));
        assertThat(generated).isEqualTo(account);
    }

    @Test
    public void verifyKryoSerializationOrderTest() {
        Order generated = KryoSupport.deserialize(KryoSupport.serialize(order));
        assertThat(generated).isEqualTo(order);
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    public void verifyKryoSerializationAccountTimeoutTest() {
        accountsMap.values().parallelStream()
                .forEach(account -> {
                    KryoSupport.deserialize(KryoSupport.serialize(account));
                });
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    public void verifyKryoSerializationOrderTimeoutTest() {
        ordersMap.values().parallelStream()
                .forEach(order -> {
                    KryoSupport.deserialize(KryoSupport.serialize(order));
                });
    }
}
