package com.danielpietka.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    public static String encodePassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
}
