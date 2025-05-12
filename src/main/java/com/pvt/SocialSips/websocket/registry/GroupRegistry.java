package com.pvt.SocialSips.websocket.registry;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GroupRegistry {

    /*key being eventid, the key in value being colorId and arraylist being the group of guests*/
    private Map<String, Map<String, ArrayList<Guest>>> groupRegistry = new HashMap<>();


    public void addGroup(String eventId, ArrayList<Guest> group) {
        Map<String, ArrayList<Guest>> colorGroups = groupRegistry.computeIfAbsent(eventId, k -> new HashMap<>());
        String colorId = generateRandomColorId();
        colorGroups.put(colorId, group);
    }

    public void removeAllGroupsFromEvent(String eventId) {
        groupRegistry.remove(eventId);
    }

    public void removeGuestFromGroup(String eventId, String colorId, String deviceId) {
        ArrayList<Guest> group = groupRegistry.get(eventId).get(colorId);
        group.removeIf(guest -> guest.getDeviceId().equals(deviceId));

        if (group.isEmpty()) {
            groupRegistry.get(eventId).remove(colorId);
        }
    }

    public Map<String, ArrayList<Guest>> getAllGroupsFromEvent(String eventId) {
        return groupRegistry.get(eventId);
    }

    public ArrayList<Guest> getGroupFromEvent(String eventId, String colorId) {
        return groupRegistry.get(eventId).get(colorId);
    }

    public String getGuestsGroup(String eventId, String deviceId) {
        Map<String, ArrayList<Guest>> groups = groupRegistry.get(eventId);
        if (groups == null) return null;

        for (Map.Entry<String, ArrayList<Guest>> entry : groups.entrySet()) {
            String groupColor = entry.getKey();
            List<Guest> guests = entry.getValue();

            for (Guest guest : guests) {
                if (guest.getDeviceId().equals(deviceId)) {
                    return groupColor;
                }
            }
        }

        return null;
    }


    public static ArrayList<ArrayList<Guest>> matchGuests(ArrayList<Guest> guests, int groupSize) {
        ArrayList<Guest> toBeMatched = new ArrayList<>(guests);
        Collections.shuffle(toBeMatched);
        ArrayList<ArrayList<Guest>> groups = new ArrayList<>();

        int amountOfGroups = toBeMatched.size() / groupSize;

        if (amountOfGroups == 0)
            amountOfGroups = 1;

        for (int i = 0; i < amountOfGroups; i++)
            groups.add(new ArrayList<>());

        for (int amountMatched = 0; amountMatched < toBeMatched.size(); amountMatched++) {
            Guest u = toBeMatched.get(amountMatched);
            groups.get(amountMatched % amountOfGroups).add(u);
        }

        return groups;
    }


    private String generateRandomColorId() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return String.format("#%02x%02x%02x", r, g, b);
    }

}
