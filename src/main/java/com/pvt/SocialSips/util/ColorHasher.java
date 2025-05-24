package com.pvt.SocialSips.util;

public class ColorHasher {
    public static String intToColorHex(int groupNumber, int totalGroups) {
        float hue = (groupNumber * 360.0f / totalGroups) % 360;

        float saturation = 0.65f;
        float lightness = 0.5f;

        java.awt.Color color = java.awt.Color.getHSBColor(hue / 360.0f, saturation, lightness);
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
