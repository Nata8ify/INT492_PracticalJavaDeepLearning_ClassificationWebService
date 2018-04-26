package com.int492.cifarclssfr.imageclassfr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfiguration {

    @Bean
    public Jedis getJedis(){
        Jedis jedis = new Jedis("localhost", 6379);
        return jedis;
    }

}
