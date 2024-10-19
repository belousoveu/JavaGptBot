/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.github.exception;

public class GithubAuthenticationException extends RuntimeException {

    public GithubAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
