package com.int492.cifarclssfr.imageclassfr;

import com.int492.cifarclssfr.imageclassfr.core.ClassificationCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Cifar10ClassifierServiceApplication {

	@Autowired
	private static ClassificationCore cService;

	private static final Executor EXC = Executors.newSingleThreadExecutor();
	private static final Runnable R = new Runnable() {

		private final long SLEEP = 1000l; /* 1 Second */

		@Override
		public void run() {
			while(true){
				try {
					System.out.println(ClassificationCore.identifyByFile(new File("testimg/ship1.jpg")));
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

	public static void main(String[] args) {
		SpringApplication.run(Cifar10ClassifierServiceApplication.class, args);
		EXC.execute(R);
	}
}
