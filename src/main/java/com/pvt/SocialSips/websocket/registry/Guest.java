package com.pvt.SocialSips.websocket.registry;

public class Guest {

    private String deviceId;
    private String eventId;
    private boolean quizMaster = false;
    private boolean foundSipMates = false;

    public Guest(String deviceId, String eventId) {
        this.deviceId = deviceId;
        this.eventId = eventId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isQuizMaster() {
        return quizMaster;
    }

    public void setQuizMaster(boolean quizMaster) {
        this.quizMaster = quizMaster;
    }

    public boolean FoundSipMates() {
        return foundSipMates;
    }

    public void setFoundSipMates(boolean foundSipMates) {
        this.foundSipMates = foundSipMates;
    }

}
