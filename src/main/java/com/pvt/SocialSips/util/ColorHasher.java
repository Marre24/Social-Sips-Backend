package com.pvt.SocialSips.util;

public class ColorHasher {
    public static String intToColorHex(int value) {
        int hash = Integer.hashCode(value);

        int rgb = hash & 0xFFFFFF;

        return String.format("#%06X", rgb);
    }
}
