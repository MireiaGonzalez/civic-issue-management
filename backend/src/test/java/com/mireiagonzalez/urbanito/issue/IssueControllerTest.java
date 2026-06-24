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
import java.util.List;
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

        private final UUID firstIssueId = UUID.randomUUID();
        private final UUID secondIssueId = UUID.randomUUID();
        private final UUID reporterId = UUID.randomUUID();
        private final UUID categoryId = UUID.randomUUID();

        private final IssueResponse firstResponse = new IssueResponse(
                        firstIssueId,
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

        private final IssueResponse secondResponse = new IssueResponse(
                        secondIssueId,
                        reporterId,
                        "John",
                        categoryId,
                        "Other",
                        "Second issue",
                        "Second issue description",
                        "Second location",
                        IssueStatus.SUBMITTED,
                        IssuePriority.MEDIUM,
                        Instant.parse("2026-06-18T12:00:00Z"),
                        Instant.parse("2026-06-18T12:00:00Z"),
                        null);

        @Test
        void createIssueReturnsCreatedIssueResponse() throws Exception {
                // Arrange
                when(issueService.createIssue(any(CreateIssueRequest.class))).thenReturn(firstResponse);

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
                                .andExpect(jsonPath("$.id").value(firstIssueId.toString()))
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

        @Test
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
                when(issueService.getIssueById(firstIssueId)).thenReturn(firstResponse);

                // Act and Assert
                mockMvc.perform(get("/api/issues/{id}", firstIssueId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(firstIssueId.toString()))
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

        @Test
        void getAllIssuesReturnsIssueResponses() throws Exception {
                // Arrange

                when(issueService.getAllIssues()).thenReturn(List.of(firstResponse, secondResponse));

                // Act and Assert
                mockMvc.perform(get("/api/issues"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[0].title").value("Issue test title"))
                                .andExpect(jsonPath("$[1].title").value("Second issue"));
        }

}
