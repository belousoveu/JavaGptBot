package org.skypro.be.javagptbot.github;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GithubService {

    private static final String GITHUB_API_URL = "https://api.github.com";
    private final OkHttpClient client = new OkHttpClient();

    public Map<String, String> getProjectFiles(String pullRequestLink) {
        Map<String, String> files = new HashMap<String, String>();

        String apiUrl = getPullRequestApiUrl(pullRequestLink);

        Request request = new Request.Builder().url(apiUrl).build();



        return files;
    }

    private String getPullRequestApiUrl(String pullRequestLink) {
        String[] parts = pullRequestLink.split("/");
        try {
            return String.format("%s/repos/%s/%s/pulls/%d",
                    GITHUB_API_URL, parts[3], parts[4], Integer.parseInt(parts[5]));
        } catch (NumberFormatException e) {
            log.error("Invalid pull request link: {}", pullRequestLink, e);
            throw new RuntimeException("Invalid pull request link: " + pullRequestLink);
        }
    }
}
