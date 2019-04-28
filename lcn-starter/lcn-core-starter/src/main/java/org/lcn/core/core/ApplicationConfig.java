package org.lcn.core.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public HttpClient httpClient() {
		return new HttpClient();
	}
}
