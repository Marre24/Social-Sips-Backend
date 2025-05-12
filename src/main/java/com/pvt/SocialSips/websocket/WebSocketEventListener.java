
package com.pvt.SocialSips.websocket;

import com.pvt.SocialSips.websocket.message.EventMessage;
import com.pvt.SocialSips.websocket.message.MessageType;
import com.pvt.SocialSips.websocket.registry.GroupRegistry;
import com.pvt.SocialSips.websocket.registry.Guest;
import com.pvt.SocialSips.websocket.registry.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

@Component
public class WebSocketEventListener {

    private SimpMessagingTemplate template;
    private SessionRegistry sessionRegistry;
    private GroupRegistry groupRegistry;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final String eventPattern = "/topic/public/event/{eventId}";

    @Autowired
    public WebSocketEventListener(SimpMessagingTemplate template, SessionRegistry sessionRegistry, GroupRegistry groupRegistry) {
        this.template = template;
        this.sessionRegistry = sessionRegistry;
        this.groupRegistry = groupRegistry;
    }

    /*removes any user that disconects*/
    @EventListener
    public void handleWebSocketEventDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String deviceId = sessionRegistry.get(sessionId).getDeviceId();
        String eventId = sessionRegistry.get(sessionId).getEventId();
        String colorId = groupRegistry.getGuestsGroup(eventId, deviceId);
        this.template.convertAndSend("/topic/public/event/" + eventId, new EventMessage(MessageType.LEAVE_EVENT));

        if (colorId != null) {
            groupRegistry.removeGuestFromGroup(eventId, colorId, deviceId);
        }

        sessionRegistry.remove(sessionId);
    }

    /*when subscribing to an eventid alert everyone else in the same event*/
    @EventListener
    public void handleWebSocketSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        List<String> deviceIdHeader = accessor.getNativeHeader("deviceId");

        if (destination != null && pathMatcher.match(eventPattern, destination)) {
            String eventId = destination.substring(destination.lastIndexOf("/") + 1);
            this.template.convertAndSend(destination, new EventMessage(MessageType.JOIN_EVENT));

            if (deviceIdHeader != null && !deviceIdHeader.isEmpty()) {
                String deviceId = deviceIdHeader.get(0);
                sessionRegistry.put(sessionId, new Guest(deviceId, eventId));
            }
        }
    }
}
