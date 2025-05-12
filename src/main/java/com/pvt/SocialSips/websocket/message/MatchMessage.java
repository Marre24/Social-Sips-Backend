package com.pvt.SocialSips.websocket.message;

public class MatchMessage extends EventMessage {

    private int groupSize;

    public MatchMessage(int groupSize) {
        super(MessageType.MATCH);
        this.groupSize = groupSize;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }
}
