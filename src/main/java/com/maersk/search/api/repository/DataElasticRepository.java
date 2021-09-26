package com.maersk.search.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.maersk.search.api.entity.Data;

@Repository
public interface DataElasticRepository extends ElasticsearchRepository<Data, String> {

	@Query("{\"match_phrase\": {\"?0\": \"?1\"}}")
	public Page<Data> findByAttributeValue(String attribute, String value, Pageable pageable);

}