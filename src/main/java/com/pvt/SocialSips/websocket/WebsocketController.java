package com.pvt.SocialSips.websocket;

import com.pvt.SocialSips.websocket.message.EventMessage;
import com.pvt.SocialSips.websocket.message.JoinGroupMessage;
import com.pvt.SocialSips.websocket.message.MatchMessage;
import com.pvt.SocialSips.websocket.message.MessageType;
import com.pvt.SocialSips.websocket.registry.GroupRegistry;
import com.pvt.SocialSips.websocket.registry.Guest;
import com.pvt.SocialSips.websocket.registry.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class WebsocketController {

    private SimpMessagingTemplate template;
    private SessionRegistry sessionRegistry;
    private GroupRegistry groupRegistry;

    @Autowired
    public WebsocketController(SimpMessagingTemplate template, SessionRegistry sessionRegistry, GroupRegistry groupRegistry) {
        this.template = template;
        this.sessionRegistry = sessionRegistry;
        this.groupRegistry = groupRegistry;
    }

    /*match people when started, host specifies groupsize*/
    /*Host only endpoint to start an event*/
    @MessageMapping("/event/start/{eventId}")
    public void startEvent(@DestinationVariable String eventId, @Payload MatchMessage matchMessage) {
        this.template.convertAndSend("/topic/public/event/" + eventId, new EventMessage(MessageType.START_EVENT));
        this.template.convertAndSend("/topic/public/event/" + eventId, matchMessage);

        addGroupsToEvent(eventId, matchMessage.getGroupSize());
    }

    @MessageMapping("/event/stop/{eventId}")
    public void stopEvent(@DestinationVariable String eventId) {
        groupRegistry.removeAllGroupsFromEvent(eventId);
    }

    @MessageMapping("/event/{eventId}/{colorId}/foundsipmates")
    public void foundSipMates(@DestinationVariable String eventId, @DestinationVariable String colorId, Message<Object> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        String deviceId = sessionRegistry.get(sessionId).getDeviceId();

        groupRegistry.getGroupFromEvent(eventId, colorId).forEach(guest -> {
            if (guest.getDeviceId().equals(deviceId)) {
                guest.setFoundSipMates(true);
            }
        });

        boolean everyoneFound = true;
        for (Guest guest : groupRegistry.getAllGroupsFromEvent(eventId).get(colorId)) {
            if (!guest.FoundSipMates()) {
                everyoneFound = false;
                break;
            }
        }

        if (everyoneFound) {
            this.template.convertAndSend("/topic/public/event/" + eventId + "/"
                    + colorId, new EventMessage(MessageType.EVERYONE_FOUND));
        }
    }


    @MessageMapping("/event/{eventId}/{colorId}/requestquizmaster")
    public void requestQuizMaster(@DestinationVariable String eventId, @DestinationVariable String colorId, Message<Object> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        String deviceId = sessionRegistry.get(sessionId).getDeviceId();

        boolean noCurrentQuizMaster = true;
        for (Guest guest : groupRegistry.getAllGroupsFromEvent(eventId).get(colorId)) {
            if (guest.isQuizMaster()) {
                noCurrentQuizMaster = false;
            }
        }

        if (noCurrentQuizMaster) {
            groupRegistry.getAllGroupsFromEvent(eventId).get(colorId).forEach(guest -> {
                if (guest.getDeviceId().equals(deviceId)) {
                    guest.setQuizMaster(true);
                    this.template.convertAndSend("/topic/public/event/" + eventId + "/"
                            + deviceId, new EventMessage(MessageType.QUIZ_MASTER_RECEIVED));
                }
            });

        }

    }


    @MessageMapping("/event/{eventId}/{colorId}/quizmasterchosen")
    public void quizmasterchosen(@DestinationVariable String eventId, @DestinationVariable String colorId, Message<Object> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        String deviceId = sessionRegistry.get(sessionId).getDeviceId();

        groupRegistry.getAllGroupsFromEvent(eventId).get(colorId).forEach(guest -> {
            if (!guest.getDeviceId().equals(deviceId)) {
                this.template.convertAndSend("/topic/public/event/" + eventId + "/"
                        + guest.getDeviceId(), new EventMessage(MessageType.QUIZ_MASTER_CHOSEN));
            }
        });

    }

    /*Specific user endpoint*/
    @MessageMapping("/event/{eventId}/{deviceId}")
    public void sendToGuest(@DestinationVariable String eventId, @DestinationVariable String deviceId, @Payload EventMessage eventMessage) {
        this.template.convertAndSend("/topic/public/event/" + eventId + "/" + deviceId, eventMessage);
    }

    private void addGroupsToEvent(String eventId, int groupSize) {
        ArrayList<Guest> guestsToBeMatched = new ArrayList<>();
        for (Guest guest : sessionRegistry.getSessionRegistry().values()) {
            if (guest.getEventId().equals(eventId)) {
                guestsToBeMatched.add(guest);
            }
        }

        ArrayList<ArrayList<Guest>> matchedGroups = GroupRegistry.matchGuests(guestsToBeMatched, groupSize);
        for (ArrayList<Guest> group : matchedGroups) {
            groupRegistry.addGroup(eventId, group);
        }

        setGroupColors(eventId);
    }

    private void setGroupColors(String eventId) {
        groupRegistry.getAllGroupsFromEvent(eventId).forEach((k, v)
                -> v.forEach(guest -> this.template.convertAndSend(
                "/topic/public/event/" + eventId + "/" +
                        guest.getDeviceId(), new JoinGroupMessage(k))));
    }

}
