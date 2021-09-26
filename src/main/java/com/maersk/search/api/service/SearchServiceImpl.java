package com.maersk.search.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.maersk.search.api.entity.Data;
import com.maersk.search.api.repository.DataElasticRepository;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private DataElasticRepository dataElasticRepository;

	/**
	 * Returns a paginated response
	 */
	@Override
	public List<Data> search(String attributeName, String attributeValue, int page, int size) {
		var pageable = PageRequest.of(page, size);
		return dataElasticRepository.findByAttributeValue(attributeName, attributeValue, pageable)
				.getContent();
	}

}
