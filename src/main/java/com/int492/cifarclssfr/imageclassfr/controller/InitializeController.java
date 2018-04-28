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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Controller
public class InitializeController {

    private static final Logger logger = LoggerFactory.getLogger(InitializeController.class);

    @Autowired
    Jedis jedis;

    private final Executor EXC = Executors.newSingleThreadExecutor();
    private final Runnable R = new Runnable() {

        private final long SLEEP = 3000l; /* 3 Second */
        private LinkedHashMap<String, String> toPredictsMap;

        @Override
        public void run() {
            toPredictsMap = new LinkedHashMap<>();
            while (true) {
                //jedis.set("@"+String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()/100 ));
                try {
                    for (String key : jedis.keys("*@*")) {
                        toPredictsMap.put(key.substring(1), jedis.get(key));
                        jedis.del(key);
                        ClassificationCore.identifyByFilePath(toPredictsMap);
                    }
                    logger.info("Continue... "+System.currentTimeMillis());
                    /* TODO Redis work logic here*/
                    Thread.sleep(SLEEP);
                } catch (InterruptedException e) {
                    logger.info("Serious exception caused thread to be stopped. " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e){
                    logger.info("Another serious exception caused thread to be stopped. " + e.getMessage());
                }
                toPredictsMap.clear();
            }
        }
    };

    @PostMapping("/init")
    public String initilaize() {
        EXC.execute(R);
        return "home";
    }

}
