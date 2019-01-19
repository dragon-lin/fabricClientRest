package com.winyeahs.fabric.clientrest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@SpringBootApplication
@MapperScan("com.winyeahs.fabric.clientrest.mapper")
public class ClientrestApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ClientrestApplication.class, args);
	}
}


