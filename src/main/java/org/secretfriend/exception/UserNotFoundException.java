package org.secretfriend.exception;

import lombok.Getter;
import org.secretfriend.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    @Getter
    private final String email;

    public UserNotFoundException(String email) {
        super("No User found with this email");
        this.email = email;
    }
}
