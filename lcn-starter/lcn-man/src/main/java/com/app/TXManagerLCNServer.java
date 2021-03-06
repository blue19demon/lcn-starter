package com.app;

import org.lcn.man.anno.EnableLCNServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLCNServer
@SpringBootApplication
public class TXManagerLCNServer {

	public static void main(String[] args) {
		SpringApplication.run(TXManagerLCNServer.class, args);
	}
}
