package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public void createEvent(Event event) {
        if (eventRepository.findById(event.getHostSub()).isPresent())
            throw new DuplicateKeyException("Event with id: " + event.getHostSub() + " already exists");

        eventRepository.save(event);
    }


    @Transactional
    public void startEvent(String hostSub) {
        Event event = getEvent(hostSub);

        if (event.getStarted())
            throw new IllegalStateException("Event has already started");

        event.setStarted(true);


    }

    public void deleteEvent(String hostSub) {
        getEvent(hostSub);

        eventRepository.deleteById(hostSub);
    }

    public boolean canJoinEvent(String joinCode) {
        Long code = Event.SQID.decode(joinCode).get(0);

        return !getEvent(code.toString()).getStarted();
    }

    public static ArrayList<ArrayList<String>> matchUsers(Set<String> guests, int groupSize){
        ArrayList<String> toBeMatched = new ArrayList<>(guests);
        Collections.shuffle(toBeMatched);
        ArrayList<ArrayList<String>> groups = new ArrayList<>();

        int amountOfGroups = toBeMatched.size() / groupSize;

        for(int i = 0; i < amountOfGroups; i++)
            groups.add(new ArrayList<>());

        for(int amountMatched = 0; amountMatched < toBeMatched.size(); amountMatched++){
            String u = toBeMatched.get(amountMatched);
            groups.get(amountMatched % amountOfGroups).add(u);
        }

        //todo connect to solution for saving/processing groups, shouldn't return like this
        return groups;
    }

}
