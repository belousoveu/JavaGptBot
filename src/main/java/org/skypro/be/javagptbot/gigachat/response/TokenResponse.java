/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.gigachat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_at")
    private long expiresAt;

}
