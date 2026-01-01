package com.example.fixmate.utils;

import java.util.Random;

public class HelperUtil {

    public String generateString() {
        Random random = new Random();
        String random6Digit = String.format("%06d", random.nextInt(1000000));
        return random6Digit;
    }
}
