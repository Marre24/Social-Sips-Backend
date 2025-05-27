package com.pvt.SocialSips.user;

public class GroupInfo {

    private String color;

    private int groupSize;

    public GroupInfo() {
    }

    public GroupInfo(String color, int groupSize) {
        this.color = color;
        this.groupSize = groupSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }
}
