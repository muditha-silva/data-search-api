package com.maersk.search.api.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maersk.search.api.entity.Data;
import com.maersk.search.api.service.SearchService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TextSearchControllerTest {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("classpath:test-data-set/test-data-set.json")
	private Resource resourceFile;

	@Autowired
	private WebTestClient webTestClient;

	@Mock
	private SearchService searchService;	
	
	@Test
	void testSuccessfulExecution() throws Exception {

		var expectedDocs = objectMapper.readValue(
				IOUtils.toString(resourceFile.getInputStream(), StandardCharsets.UTF_8),
				new TypeReference<List<Data>>() {
				});

		Mockito.when(searchService.search(anyString(), anyString(), anyInt(), anyInt()))
				.thenReturn(expectedDocs);

		webTestClient.get().uri("/v1/search?attribute=test&value=test").exchange()
				.expectBodyList(Data.class).value(docs -> {
					assertNotNull(docs);
					assertTrue(docs.size() == 1);
				});

	}

	@Test
	void testIllegalRequestParam() throws Exception {

		webTestClient.get().uri("/v1/search?attribute=123&value=test").exchange().expectStatus()
				.isEqualTo(HttpStatus.SC_BAD_REQUEST);

	}

	@Test
	void testSearchFailure() throws Exception {

		Mockito.when(searchService.search(anyString(), anyString(), anyInt(), anyInt()))
				.thenReturn(Collections.emptyList());

		webTestClient.get().uri("/v1/search?attribute=test&value=test").exchange().expectStatus()
				.isEqualTo(HttpStatus.SC_NOT_FOUND);
	}

}
