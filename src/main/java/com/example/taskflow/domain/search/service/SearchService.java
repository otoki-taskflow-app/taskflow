package com.example.taskflow.domain.search.service;

import com.example.taskflow.domain.search.dto.SearchResult;
import com.example.taskflow.domain.task.service.TaskExternalService;
import com.example.taskflow.domain.team.service.TeamExternalService;
import com.example.taskflow.domain.user.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final TaskExternalService taskExternalService;
    private final UserExternalService userExternalService;
    private final TeamExternalService teamExternalService;

    public SearchResult searchAll(String query) {
        return SearchResult.of(
                taskExternalService.searchByKeyword(query),
                userExternalService.searchByKeyword(query),
                teamExternalService.searchByKeyword(query)
        );
    }
}
