package com.maersk.search.api.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maersk.search.api.entity.Data;
import com.maersk.search.api.repository.DataElasticRepository;

/**
 * This test requires: Elasticsearch instance running on localhost:9200.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
class TextSearchControllerIntegrationTest {

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Value("classpath:test-data-set/test-data-set.json")
	private Resource resourceFile;

	@Autowired
	private DataElasticRepository dataElasticRepository;

	@Autowired
	private WebTestClient webTestClient;

	private List<Data> dataset;

	/**
	 * 
	 * Ingest test data set to Elasticsearch index
	 */
	@BeforeAll
	private void beforeAll() throws IOException {
		dataset = objectMapper.readValue(
				IOUtils.toString(resourceFile.getInputStream(), StandardCharsets.UTF_8),
				new TypeReference<List<Data>>() {
				});

		dataElasticRepository.saveAll(dataset);
	}

	/**
	 * 
	 * Delete data set from index after the tests run
	 */
	@AfterAll
	private void afterAll() {
		dataElasticRepository.deleteAll(dataset);
	}

	@Test
	void testSearchAPI() throws Exception {

		webTestClient.get().uri("/v1/search?attribute=subject&value=Cargo Missing").exchange()
				.expectBodyList(Data.class).value(docs -> {
					assertNotNull(docs);
					assertTrue(docs.get(0).getId().equals("1"));
					assertTrue(docs.get(0).getType().equals("incident"));
					assertTrue(docs.get(0).getSubject().equals("Cargo Missing"));
					assertTrue(docs.get(0).getDescription().equals(
							"Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris ex exercitation amet et proident. Ipsum fugiat aute dolore tempor nostrud velit ipsum."));
					assertTrue(docs.get(0).getPriority().equals("high"));
					assertTrue(docs.get(0).getStatus().equals("pending"));
					assertTrue(docs.size() == 1);

				});

	}

	@Test
	void testRandom() throws Exception {
		List<String> PRIORITY = List.of("high", "low");
		int size = 10;
		for (var priority : PRIORITY) {

			webTestClient.get()
					.uri(uriBuilder -> uriBuilder.path("/v1/search")
							.queryParam("attribute", "priority").queryParam("value", priority)
							.queryParam("page", 0).queryParam("size", size).build())
					.exchange().expectBodyList(Data.class).value(docs -> {
						assertNotNull(docs);
						assertTrue(docs.size() <= size);
					});

		}

	}

}