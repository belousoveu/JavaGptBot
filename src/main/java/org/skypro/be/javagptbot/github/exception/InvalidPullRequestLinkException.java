/*
 * Copyright (c) 2024.
 *
 */

package org.skypro.be.javagptbot.github.exception;

import java.io.IOException;

public class InvalidPullRequestLinkException extends IOException {
    public InvalidPullRequestLinkException(String message) {

        super("Invalid Pull Request Link " + message + ". Send a correct link and try again");
    }

    public InvalidPullRequestLinkException(String message, Throwable cause) {
        super("Can't get files from github. Check pull-request link " + message + " and try again", cause);
    }
}
