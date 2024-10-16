/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.gigachat.response;

public class TokenResponse {
    private String access_token;
    private long expires_at;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public long getExpiresAt() {
        return expires_at;
    }

    public void setExpiresAt(long expiresAt) {
        this.expires_at = expiresAt;
    }
}
