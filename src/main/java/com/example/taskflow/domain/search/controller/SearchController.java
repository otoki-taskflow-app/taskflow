package com.example.taskflow.domain.search.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.search.dto.SearchResult;
import com.example.taskflow.domain.search.exception.SearchErrorCode;
import com.example.taskflow.domain.search.exception.SearchException;
import com.example.taskflow.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    /*
     1. 통합 검색 API
       - 프론트에서 ?q=검색어 형식으로 노출
       - 검색어(q)를 기준으로 관련된 항목을 찾아 반환
     */
    @GetMapping
    public ResponseEntity<ApiResponse<SearchResult>> search(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new SearchException(SearchErrorCode.EMPTY_QUERY);
        }

        SearchResult result = searchService.searchAll(query);
        return ApiResponse.success(result, "검색 완료");
    }
}

