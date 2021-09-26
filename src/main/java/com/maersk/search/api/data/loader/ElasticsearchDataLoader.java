package com.maersk.search.api.data.loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maersk.search.api.entity.Data;
import com.maersk.search.api.repository.DataElasticRepository;

/**
 * 
 * Purpose of this component is to load a data set to Elasticsearch index
 * 'incident-data' This is done for the demonstration purpose
 * 
 */
@Component
public class ElasticsearchDataLoader {

	@Autowired
	private DataElasticRepository dataElasticRepository;

	/**
	 * Injecting resource
	 */
	@Value("classpath:data/data-set.json")
	private Resource resourceFile;

	private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchDataLoader.class);

	private static ObjectMapper objectMapper = new ObjectMapper();

	@EventListener(ApplicationReadyEvent.class)
	public void loadData() {

		try {
			var dataSet = objectMapper.readValue(
					IOUtils.toString(resourceFile.getInputStream(), StandardCharsets.UTF_8),
					new TypeReference<List<Data>>() {
					});

			dataElasticRepository.deleteAll(dataSet);

			dataElasticRepository.saveAll(dataSet);

			LOG.info("Saved dummy data set : {}", dataElasticRepository.count());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
