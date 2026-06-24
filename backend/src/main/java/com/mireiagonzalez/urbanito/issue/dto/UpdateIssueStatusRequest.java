package com.mireiagonzalez.urbanito.issue.dto;

import com.mireiagonzalez.urbanito.issue.IssueStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateIssueStatusRequest(
        @NotNull IssueStatus status) {

}
