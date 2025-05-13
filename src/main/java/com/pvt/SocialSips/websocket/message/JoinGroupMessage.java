package com.pvt.SocialSips.websocket.message;

public class JoinGroupMessage extends EventMessage {

    private String groupColor;

    public JoinGroupMessage(String groupColor) {
        super(MessageType.JOIN_GROUP);
        this.groupColor = groupColor;
    }


    public String getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(String groupColor) {
        this.groupColor = groupColor;
    }
}
