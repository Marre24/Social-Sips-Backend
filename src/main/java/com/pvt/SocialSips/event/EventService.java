package com.pvt.SocialSips.event;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.user.Guest;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public Event getEvent(String hostSub) {
        Optional<Event> optionalEvent = eventRepository.findById(hostSub);

        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException("Event with id: " + hostSub + " does not exist");

        return optionalEvent.get();
    }

    @Transactional
    public Event createEvent(Event event, String sub) {
        User user = userService.getUserBySub(sub);

        if (eventRepository.findById(sub).isPresent())
            throw new DuplicateKeyException("Host with id: " + sub + " already has an event!");

        user.setEvent(event.getGroupSize(), event.getQuestpools());
        userService.register(user);

        return getEvent(user.getSub());
    }


    @Transactional
    public void startEvent(String hostSub) {
        Event event = getEvent(hostSub);

        if (event.getStarted())
            throw new IllegalStateException("Event has already started");

        var groups = matchUsers(event.getGuests(), event.getGroupSize());
        for (int i = 1; i <= groups.size(); i++)
            for (Guest g : groups.get(i - 1))
                g.setGroupNumber(i);

        event.setStarted(true);
    }

    public void deleteEvent(String hostSub) {
        getEvent(hostSub);

        userService.removeEventFrom(hostSub);
        eventRepository.deleteById(hostSub);
    }

    public boolean canJoinEvent(String joinCode) {
        var event = eventRepository.findByJoinCode(joinCode);

        if (event.isEmpty())
            throw new EntityNotFoundException("Could not find event with join code: " + joinCode);

        return !event.get().getStarted();
    }

    public static ArrayList<ArrayList<Guest>> matchUsers(Set<Guest> guests, int groupSize) {
        if (guests.isEmpty())
            return new ArrayList<>();

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

    public Set<Questpool> getQuestpoolsFor(String joinCode) {
        var event = eventRepository.findByJoinCode(joinCode);

        if (event.isEmpty())
            throw new EntityNotFoundException("Could not find event with join code: " + joinCode);

        return event.get().getQuestpools();
    }

    @Transactional
    public void joinEvent(String joinCode, String uuid) {
        var event = eventRepository.findByJoinCode(joinCode);

        if (event.isEmpty())
            throw new EntityNotFoundException("Could not find event with join code: " + joinCode);

        Guest g = userService.registerGuest(uuid);

        if (event.get().containsGuest(g))
            throw new DuplicateKeyException("Guest with uuid: " + uuid + " is already joined in this event");


        event.get().addGuest(g);
    }

    public boolean isStarted(String joinCode) {
        var event = eventRepository.findByJoinCode(joinCode);

        if (event.isEmpty())
            throw new EntityNotFoundException("Could not find event with join code: " + joinCode);

        return event.get().getStarted();
    }

    public Event getByJoinCode(String joinCode) {
        var event = eventRepository.findByJoinCode(joinCode);

        if (event.isEmpty())
            throw new EntityNotFoundException("Could not find event with join code: " + joinCode);

        return event.get();
    }

    @Transactional
    public void removeFromEvent(String joinCode, String uuid) {
        var event = getByJoinCode(joinCode);
        var guest = userService.getGuest(uuid);

        event.removeGuest(guest);
    }
}
