package com.maersk.search.api.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maersk.search.api.exception.ResourceNotFoundException;
import com.maersk.search.api.service.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(value = "/v1")
@RestController
@Tag(name = "Maersk Search API", description = "Documentation for Maersk Search API")
public class TextSearchController {

	@Autowired
	private SearchService searchService;

	@GetMapping(value = "/search")
	@Operation(summary = "Search by attribute value", description = "Search by attribute value")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request "),
			@ApiResponse(responseCode = "404", description = "No data found") })
	public ResponseEntity<Object> search(
			@Parameter(description = "Attribute to be find") @RequestParam(name = "attribute") String attribute,
			@Parameter(description = "Value to be find") @RequestParam(name = "value") String value,
			@Parameter(description = "Page number (for pagination)") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Number of items per page (for pagination)") @RequestParam(defaultValue = "10") int size) {
		var headers = new HttpHeaders();
		headers.add(HttpHeaders.SERVER, "Spring");

		if (StringUtils.isEmpty(attribute) || StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException("Eather 'attribute' or 'value' must be set");
		}

		if (StringUtils.isNumeric(attribute)) {
			throw new IllegalArgumentException("Numeric vaslues not acceptable for 'attribute'");
		}

		var documents = searchService.search(attribute, value, page, size);

		if (CollectionUtils.isEmpty(documents)) {
			throw new ResourceNotFoundException(
					String.format("No Data Found for 'attribute' %s 'value' %s", attribute, value));
		}

		return ResponseEntity.ok().headers(headers).body(documents);
	}

}
