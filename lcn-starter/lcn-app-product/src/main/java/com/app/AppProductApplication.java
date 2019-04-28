package com.app;

import org.lcn.core.anno.EnableLCNClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.app.mapper")
@EnableAspectJAutoProxy
@EnableLCNClient
public class AppProductApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppProductApplication.class, args);
	}
}

