package com.pvt.SocialSips.util;

public class ColorHasher {
    public static String intToColorHex(int groupNumber, int totalGroups) {
        if (totalGroups <= 0) {
            throw new IllegalArgumentException("Total groups must be > 0");
        }

        float hue = (groupNumber * 360.0f / totalGroups) % 360;

        float saturation = 0.65f;
        float brightness = 0.80f;

        java.awt.Color color = java.awt.Color.getHSBColor(hue / 360f, saturation, brightness);
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
