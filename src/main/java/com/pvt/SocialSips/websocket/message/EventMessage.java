package com.pvt.SocialSips.websocket.message;

public class EventMessage {

    private MessageType type;


    public EventMessage() {

    }

    public EventMessage(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
