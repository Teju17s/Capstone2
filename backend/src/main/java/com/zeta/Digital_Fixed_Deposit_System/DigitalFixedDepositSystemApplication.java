package com.zeta.Digital_Fixed_Deposit_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DigitalFixedDepositSystemApplication {

	public static void main(String[] args) {

        SpringApplication.run(DigitalFixedDepositSystemApplication.class, args);
        System.out.println("Welcome");
	}

}
