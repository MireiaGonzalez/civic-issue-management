package com.mireiagonzalez.urbanito.issue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mireiagonzalez.urbanito.category.IssueCategory;
import com.mireiagonzalez.urbanito.category.IssueCategoryRepository;
import com.mireiagonzalez.urbanito.issue.dto.CreateIssueRequest;
import com.mireiagonzalez.urbanito.issue.dto.IssueResponse;
import com.mireiagonzalez.urbanito.user.User;
import com.mireiagonzalez.urbanito.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

        @Mock
        private IssueRepository issueRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private IssueCategoryRepository issueCategoryRepository;

        @InjectMocks
        private IssueService issueService;

        // Test request
        private final UUID reporterId = UUID.randomUUID();
        private final UUID categoryId = UUID.randomUUID();

        private final CreateIssueRequest request = new CreateIssueRequest(
                        reporterId,
                        categoryId,
                        "Issue test title",
                        "Issue creation test description",
                        "A random location");

        @Test
        void createIssueSavesIssueAndReturnsResponse() {
                // Arrange
                User reporter = mock(User.class);
                IssueCategory category = mock(IssueCategory.class);

                when(reporter.getId()).thenReturn(reporterId);
                when(reporter.getName()).thenReturn("John");

                when(category.getId()).thenReturn(categoryId);
                when(category.getName()).thenReturn("Other");

                when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
                when(issueCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

                when(issueRepository.save(any(Issue.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                IssueResponse response = issueService.createIssue(request);

                // Assert
                assertThat(response.reporterId()).isEqualTo(reporterId);
                assertThat(response.reporterName()).isEqualTo("John");
                assertThat(response.categoryId()).isEqualTo(categoryId);
                assertThat(response.categoryName()).isEqualTo("Other");
                assertThat(response.title()).isEqualTo("Issue test title");
                assertThat(response.description()).isEqualTo("Issue creation test description");
                assertThat(response.location()).isEqualTo("A random location");
                assertThat(response.status()).isEqualTo(IssueStatus.SUBMITTED);
                assertThat(response.priority()).isEqualTo(IssuePriority.MEDIUM);

                // Verify
                verify(issueRepository).save(any(Issue.class));
        }

        @Test
        void createIssueThrowsErrorWhenReporterDoesNotExist() {
                // Arrange
                when(userRepository.findById(reporterId)).thenReturn(Optional.empty());

                // Act and Assert
                assertThatThrownBy(() -> issueService.createIssue(request))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Reporter not found");

                // Verify
                verify(issueRepository, never()).save(any(Issue.class));
                verify(issueCategoryRepository, never()).findById(any(UUID.class));
        }

        @Test
        void createIssueThrowsErrorWhenCategoryDoesNotExist() {
                // Arrange
                User reporter = mock(User.class);

                when(userRepository.findById(reporterId)).thenReturn(Optional.of(reporter));
                when(issueCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

                // Act and Assert
                assertThatThrownBy(() -> issueService.createIssue(request))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessage("Category not found");

                // Verify
                verify(issueRepository, never()).save(any(Issue.class));
        }

        @Test
        void getIssueByIdReturnsIssueResponse() {
                // Arrange
                UUID issueId = UUID.randomUUID();

                User reporter = mock(User.class);
                IssueCategory category = mock(IssueCategory.class);

                when(reporter.getId()).thenReturn(reporterId);
                when(reporter.getName()).thenReturn("John");
                when(category.getId()).thenReturn(categoryId);
                when(category.getName()).thenReturn("Other");

                Issue issue = Issue.create(
                                reporter,
                                category,
                                "Issue test title",
                                "Issue creation test description",
                                "A random location");

                when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));

                // Act
                IssueResponse response = issueService.getIssueById(issueId);

                // Assert
                assertThat(response.reporterId()).isEqualTo(reporterId);
                assertThat(response.reporterName()).isEqualTo("John");
                assertThat(response.categoryId()).isEqualTo(categoryId);
                assertThat(response.categoryName()).isEqualTo("Other");
                assertThat(response.title()).isEqualTo("Issue test title");
                assertThat(response.description())
                                .isEqualTo("Issue creation test description");
                assertThat(response.location()).isEqualTo("A random location");
                assertThat(response.status()).isEqualTo(IssueStatus.SUBMITTED);
                assertThat(response.priority()).isEqualTo(IssuePriority.MEDIUM);
        }

        @Test
        void getIssueByIdThrowsIssueNotFoundExceptionWhenIssueDoesNotExist() {
                // Arrange
                UUID issueId = UUID.randomUUID();

                when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

                // Act and Assert
                assertThatThrownBy(() -> issueService.getIssueById(issueId))
                                .isInstanceOf(IssueNotFoundException.class)
                                .hasMessage("Issue not found");
        }

}
