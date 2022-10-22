package com.ascendant76.redis;


import redis.embedded.RedisServer;

public class RedisLauncher {
    public static void main(String[] args) {
        RedisServer.builder().port(6379).build().start();
    }
}
