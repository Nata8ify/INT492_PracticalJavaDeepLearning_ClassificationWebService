package com.int492.cifarclssfr.imageclassfr.controller;

import com.int492.cifarclssfr.imageclassfr.Cifar10ClassifierServiceApplication;
import com.int492.cifarclssfr.imageclassfr.core.ClassificationCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class InitializeController {

    private static final Logger logger = LoggerFactory.getLogger(InitializeController.class);

    @Autowired
    Jedis jedis;

    private final Executor EXC = Executors.newSingleThreadExecutor();
    private final Runnable R = new Runnable() {

        private final long SLEEP = 10000l; /* 3 Second */
        private Set<String> valueKeySet;
        @Override
        public void run() {
            while (true) {
                //jedis.set("@"+String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()/100 ));
                try {
                    Thread.sleep(SLEEP); /* Sleep for 3 secs. */
                    valueKeySet = jedis.keys("*@*"); /* Let's get any key which contains '@' at first index. */
                    if(valueKeySet.isEmpty()){logger.info("Waiting new coming task...");continue;} /* No '@'? then no task... do skip. */
                    else {logger.info("New coming task ["+valueKeySet.size()+"] <= "+valueKeySet.toString());}
                    for (String key : valueKeySet) { /* Have '@' the do a tasks for many as possible. */
                        jedis.set(key.substring(1), ClassificationCore.identifyByFilePath(jedis.get(key)).toString()); /* GET FILE BY FILE PATH TO PREDICT AND RETURN THE PREDICTION MODEL... */
                        jedis.del(key); /* Delete '@' key... it's done.*/
                    }
                    logger.info("Return... ["+valueKeySet.size()+"] => "+jedis.keys("*").toString()); /* Print returned result. */
                } catch (InterruptedException e) {
                    logger.info("Serious exception caused thread to be stopped. " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e){
                    logger.info("Another serious exception caused thread to be stopped. " + e.getMessage());
                }
                valueKeySet.clear();
            }
        }
    };

    @PostMapping("/init")
    public String initilaize() {
        EXC.execute(R);
        return "home";
    }

}
