package org.secretfriend.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtils {

    private PasswordUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final int ROUNDS = 12;

    public static String hashPassword(String password) {

        String salt = BCrypt.gensalt(ROUNDS);
        return BCrypt.hashpw(password, salt);
    }

    public static boolean checkPassword(String password, String storedHash) {

        return BCrypt.checkpw(password, storedHash);
    }
}
