package org.skypro.be.javagptbot.exception;

public class GithubAuthenticationException extends RuntimeException {

    public GithubAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
