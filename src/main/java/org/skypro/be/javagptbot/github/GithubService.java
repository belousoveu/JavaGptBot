package org.skypro.be.javagptbot.github;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.*;
import org.skypro.be.javagptbot.github.exception.GithubAuthenticationException;
import org.skypro.be.javagptbot.github.exception.InvalidPullRequestLinkException;
import org.skypro.be.javagptbot.utils.TextUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class GithubService {

    private final String githubToken = System.getenv("GITHUB_TOKEN");
    private String owner;
    private String repo;
    private int pullNumber;

    public Map<String, String> getProjectFiles(String pullRequestLink) throws IOException {
        Map<String, String> files = new HashMap<>();
        Set<String> renamedFiles;
        setRepoDetails(pullRequestLink);

        try {
            GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
            GHRepository repository = github.getRepository(owner + "/" + repo);
            GHPullRequest pullRequest = repository.getPullRequest(pullNumber);
            if (pullRequest == null || pullRequest.getState().equals(GHIssueState.CLOSED)) {
                throw new InvalidPullRequestLinkException(pullRequestLink);
            }

            renamedFiles = detectRenamedFiles(pullRequest);
            readFilesInBranch(repository, repository.getBranch(pullRequest.getBase().getRef()), files, renamedFiles);
            readFilesInBranch(repository, repository.getBranch(pullRequest.getHead().getRef()), files, renamedFiles);

        } catch (HttpException e) {
            log.error("Authentication failed", e);
            throw new GithubAuthenticationException("GitHub Authentication failed. Contact the developer", e);
        } catch (IOException e) {
            log.error("Can't get files from GitHub", e);
            throw new InvalidPullRequestLinkException(pullRequestLink, e);
        }
        return files;
    }

    private void readFilesInBranch(GHRepository repository, GHBranch branch,
                                   Map<String, String> files, Set<String> renamedFiles) throws IOException {
        readFilesInDirectory(repository, repository.getDirectoryContent("", branch.getName()), branch, files, renamedFiles);
    }

    private void readFilesInDirectory(GHRepository repository, List<GHContent> directoryContent,
                                      GHBranch branch, Map<String, String> files, Set<String> renamedFiles) throws IOException {
        for (GHContent content : directoryContent) {
            if (content.isDirectory()) {
                readFilesInDirectory(repository, repository.getDirectoryContent(content.getPath(),
                        branch.getName()), branch, files, renamedFiles);
            } else {
                if (content.getName().endsWith(".java") && !renamedFiles.contains(content.getPath())) {
                    files.put(content.getPath(), TextUtils.convertToString(content.read()));
                }
            }
        }
    }

    private static Set<String> detectRenamedFiles(GHPullRequest pullRequest) throws IOException {
        Set<String> result = new HashSet<>();
        for (GHPullRequestFileDetail fileDetail : pullRequest.listFiles().toList()) {
            if (fileDetail.getStatus().equals("renamed")) {
                result.add(fileDetail.getPreviousFilename());
            }
            if (fileDetail.getStatus().equals("removed")) {
                result.add(fileDetail.getFilename());
            }
        }
        return result;
    }

    private void setRepoDetails(String pullRequestLink) throws IOException {
        String[] parts = pullRequestLink.split("/");
        owner = parts[3];
        repo = parts[4];
        try {
            pullNumber = Integer.parseInt(parts[6]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            log.error("Invalid pull request link: {}", pullRequestLink, e);
            throw new InvalidPullRequestLinkException(pullRequestLink);
        }
    }
}
