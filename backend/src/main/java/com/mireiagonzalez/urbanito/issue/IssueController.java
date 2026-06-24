package com.mireiagonzalez.urbanito.issue;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mireiagonzalez.urbanito.issue.dto.CreateIssueRequest;
import com.mireiagonzalez.urbanito.issue.dto.IssueResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @GetMapping("/{id}")
    public IssueResponse getIssueById(@PathVariable UUID id) {
        return issueService.getIssueById(id);
    }

    @GetMapping
    public List<IssueResponse> getAllIssues() {
        return issueService.getAllIssues();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse createIssue(@Valid @RequestBody CreateIssueRequest request) {
        return issueService.createIssue(request);
    }

}
