package com.pvt.SocialSips.user;

public class GroupInfo {

    private String color;

    private int groupNumber;

    public GroupInfo() {
    }

    public GroupInfo(String color, int groupNumber) {
        this.color = color;
        this.groupNumber = groupNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }
}
