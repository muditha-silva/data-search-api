package com.maersk.search.api.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${elasticsearch.host}")
	private String elasticsearchHost;
	
	@Bean(name = "webClientElasticsearch")
	public WebClient webClientElasticsearch() {
		return WebClient.builder().baseUrl(elasticsearchHost)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

}
