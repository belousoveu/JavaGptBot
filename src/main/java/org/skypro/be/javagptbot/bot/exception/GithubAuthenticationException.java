package org.skypro.be.javagptbot.bot.exception;

public class GithubAuthenticationException extends RuntimeException {

    public GithubAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
