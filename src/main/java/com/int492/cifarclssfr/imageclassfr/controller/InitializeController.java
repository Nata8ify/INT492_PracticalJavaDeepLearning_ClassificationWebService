package com.int492.cifarclssfr.imageclassfr.controller;

import com.int492.cifarclssfr.imageclassfr.configuration.RedisConfiguration;
import com.int492.cifarclssfr.imageclassfr.core.ClassificationCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class InitializeController {

    @Autowired
    Jedis jedis;

    private final Executor EXC = Executors.newSingleThreadExecutor();
    private final Runnable R = new Runnable() {

        private final long SLEEP = 3000l; /* 1 Second */

        @Override
        public void run() {
            while(true){
                try {
                    /*jedis.set(String.valueOf(System.currentTimeMillis()),ClassificationCore.identifyByFile(new File("testimg/ship1.jpg")).toString());
                    System.out.println(jedis.info());
                    */System.out.println(ClassificationCore.identifyByFile(new File("testimg/ship1.jpg")).toString());
                    System.out.println(jedis.get("1524726659155"));
                    /* TODO Redis work logic here*/
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @PostMapping("/init")
    public String initilaize(){
        EXC.execute(R);
        return "home";
    }

}
