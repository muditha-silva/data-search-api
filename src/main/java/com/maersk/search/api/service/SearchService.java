package com.maersk.search.api.service;

import java.util.List;

import com.maersk.search.api.entity.Data;

public interface SearchService {
	public List<Data> search(String attributeName, String attributeValue, int page, int size);
}