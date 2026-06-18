package com.mireiagonzalez.urbanito.issue;

import org.springframework.stereotype.Service;

import com.mireiagonzalez.urbanito.category.IssueCategory;
import com.mireiagonzalez.urbanito.category.IssueCategoryRepository;
import com.mireiagonzalez.urbanito.issue.dto.CreateIssueRequest;
import com.mireiagonzalez.urbanito.issue.dto.IssueResponse;
import com.mireiagonzalez.urbanito.user.User;
import com.mireiagonzalez.urbanito.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final IssueCategoryRepository issueCategoryRepository;

    public IssueResponse createIssue(CreateIssueRequest request) {
        User reporter = userRepository.findById(request.reporterId())
                .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));
        IssueCategory category = issueCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Issue newIssue = Issue.create(reporter, category, request.title(),
                request.description(), request.location());

        Issue savedIssue = issueRepository.save(newIssue);

        return toResponse(savedIssue);
    }

    private IssueResponse toResponse(Issue issue) {
        User reporter = issue.getReporter();
        IssueCategory category = issue.getCategory();

        return new IssueResponse(
                issue.getId(),
                reporter.getId(),
                reporter.getName(),
                category.getId(),
                category.getName(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getLocation(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getResolvedAt());
    }

}
