package com.ascendant76.redis.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash
@Builder
public class Order {
    @Id
    private String id;
    private String ticker;
    private BigDecimal quantity;
    private String currency;
    private BigDecimal price;
    private LocalDate tradeDate;
    private Account account;
}
