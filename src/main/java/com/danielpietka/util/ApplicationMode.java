package com.danielpietka.util;

public class ApplicationMode {

    public static boolean isCommandMode(String[] args) {
        return args.length > 0;
    }

    public static boolean isServerMode(String[] args) {
        return args.length == 0;
    }
}
