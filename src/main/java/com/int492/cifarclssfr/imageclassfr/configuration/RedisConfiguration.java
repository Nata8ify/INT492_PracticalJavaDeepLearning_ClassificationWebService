package com.int492.cifarclssfr.imageclassfr.configuration;

import com.int492.cifarclssfr.imageclassfr.controller.InitializeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    @Bean
    public Jedis getJedis(){
        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost", 6379);
            logger.info(jedis.info());
        } catch (Exception e){
            logger.info("Unsuccessful to connect... "+e.getMessage());
        }
        return jedis;
    }

}
