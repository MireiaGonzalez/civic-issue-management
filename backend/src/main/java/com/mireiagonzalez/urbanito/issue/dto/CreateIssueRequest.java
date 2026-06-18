package com.mireiagonzalez.urbanito.issue.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIssueRequest(@NotNull UUID reporterId, @NotNull UUID categoryId,
        @NotBlank @Size(max = 200) String title, @NotBlank String description,
        @NotBlank @Size(max = 255) String location) {
}
