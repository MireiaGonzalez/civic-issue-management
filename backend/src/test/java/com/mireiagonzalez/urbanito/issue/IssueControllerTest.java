package com.mireiagonzalez.urbanito.issue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mireiagonzalez.urbanito.issue.dto.CreateIssueRequest;
import com.mireiagonzalez.urbanito.issue.dto.IssueResponse;

@WebMvcTest(IssueController.class)
class IssueControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private IssueService issueService;

        void createIssueReturnsCreatedIssueResponse() throws Exception {
                // Arrange
                UUID issueId = UUID.randomUUID();
                UUID reporterId = UUID.randomUUID();
                UUID categoryId = UUID.randomUUID();

                IssueResponse response = new IssueResponse(
                                issueId,
                                reporterId,
                                "John",
                                categoryId,
                                "Other",
                                "Issue test title",
                                "Issue creation test description",
                                "A random location",
                                IssueStatus.SUBMITTED,
                                IssuePriority.MEDIUM,
                                Instant.parse("2026-06-18T12:00:00Z"),
                                Instant.parse("2026-06-18T12:00:00Z"),
                                null);

                when(issueService.createIssue(any(CreateIssueRequest.class))).thenReturn(response);

                String requestJson = """
                                {
                                  "reporterId": "%s",
                                  "categoryId": "%s",
                                  "title": "Issue test title",
                                  "description": "Issue creation test description",
                                  "location": "A random location"
                                }
                                """.formatted(reporterId, categoryId);

                // Act and Assert
                mockMvc.perform(post("/api/issues")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(issueId.toString()))
                                .andExpect(jsonPath("$.reporterId").value(reporterId.toString()))
                                .andExpect(jsonPath("$.reporterName").value("John"))
                                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                                .andExpect(jsonPath("$.categoryName").value("Other"))
                                .andExpect(jsonPath("$.title").value("Issue test title"))
                                .andExpect(jsonPath("$.description").value("Issue creation test description"))
                                .andExpect(jsonPath("$.location").value("A random location"))
                                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                                .andExpect(jsonPath("$.priority").value("MEDIUM"));
        }

        void createIssueRequestReturnsBadRequestWhenRequestIsInvalid() throws Exception {
                // Arrange
                String requestJson = """
                                {
                                    "reporterId": null,
                                    "categoryId": null,
                                    "title": "",
                                    "description": "",
                                    "location": ""
                                }
                                """;

                // Act and Assert
                mockMvc.perform(post("/api/issues")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andExpect(status().isBadRequest());

                // Verify
                verify(issueService, never()).createIssue(any(CreateIssueRequest.class));
        }

        @Test
        void getIssueByIdReturnsIssueResponse() throws Exception {
                // Arrange
                UUID issueId = UUID.randomUUID();
                UUID reporterId = UUID.randomUUID();
                UUID categoryId = UUID.randomUUID();

                IssueResponse response = new IssueResponse(
                                issueId,
                                reporterId,
                                "John",
                                categoryId,
                                "Other",
                                "Issue test title",
                                "Issue creation test description",
                                "A random location",
                                IssueStatus.SUBMITTED,
                                IssuePriority.MEDIUM,
                                Instant.parse("2026-06-18T12:00:00Z"),
                                Instant.parse("2026-06-18T12:00:00Z"),
                                null);

                when(issueService.getIssueById(issueId)).thenReturn(response);

                // Act and Assert
                mockMvc.perform(get("/api/issues/{id}", issueId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(issueId.toString()))
                                .andExpect(jsonPath("$.reporterId").value(reporterId.toString()))
                                .andExpect(jsonPath("$.reporterName").value("John"))
                                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                                .andExpect(jsonPath("$.categoryName").value("Other"))
                                .andExpect(jsonPath("$.title").value("Issue test title"))
                                .andExpect(jsonPath("$.description")
                                                .value("Issue creation test description"))
                                .andExpect(jsonPath("$.location").value("A random location"))
                                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                                .andExpect(jsonPath("$.priority").value("MEDIUM"));
        }

        @Test
        void getIssueByIdReturnsNotFoundWhenIssueDoesNotExist() throws Exception {
                // Arrange
                UUID issueId = UUID.randomUUID();

                when(issueService.getIssueById(issueId))
                                .thenThrow(new IssueNotFoundException());

                // Act and Assert
                mockMvc.perform(get("/api/issues/{id}", issueId))
                                .andExpect(status().isNotFound());
        }
}
