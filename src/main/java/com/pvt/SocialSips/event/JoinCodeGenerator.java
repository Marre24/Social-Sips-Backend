package com.pvt.SocialSips.event;

import java.math.BigInteger;
import java.security.MessageDigest;

public class JoinCodeGenerator {
    private static final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final String SALT = "AndradeLiteFel";

    public static String generateLetterCode(String input) {
        try {
            String combined = SALT + input;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes());

            BigInteger bigInt = new BigInteger(1, hash);

            StringBuilder code = new StringBuilder();

            for (int i = 0; i < 6; i++) {
                int index = bigInt.mod(BigInteger.valueOf(26)).intValue();
                code.append(LETTERS[index]);
                bigInt = bigInt.divide(BigInteger.valueOf(26));
            }

            return code.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
