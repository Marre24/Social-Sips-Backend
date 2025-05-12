package com.pvt.SocialSips.websocket.registry;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*keeps track of each sessionId*/
@Component
public class SessionRegistry {

    /*key being sessionId and value the actual guest*/
    private Map<String, Guest> sessionRegistry = new HashMap<>();


    public void put(String sessionId, Guest guest) {
        sessionRegistry.put(sessionId, guest);
    }

    public Guest get(String sessionId) {
        return sessionRegistry.get(sessionId);
    }

    public Guest remove(String sessionId) {
        return sessionRegistry.remove(sessionId);
    }

    public Map<String, Guest> getSessionRegistry() {
        return sessionRegistry;
    }

}
