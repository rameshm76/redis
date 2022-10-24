package com.ascendant76.redis.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash
@Builder
public class Account {
    @Id
    private String id;
    private String name;

    @Indexed
    private AccountType accountType;

    public enum AccountType {
        PERSONAL, MANAGED, OMNI
    }
}
