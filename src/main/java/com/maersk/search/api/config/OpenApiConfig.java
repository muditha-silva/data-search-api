package com.maersk.search.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI practicalJavaOpenApi() {
		var info = new Info().title("Maersk dataset search API ")
				.description("OpenApi (Swagger) documentation auto generated from code")
				.version("1.0");

		return new OpenAPI().info(info);
	}

}
