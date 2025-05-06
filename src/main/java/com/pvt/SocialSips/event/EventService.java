package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.Host;
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

    public Event getEvent(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException("Event with id: " + id + " does not exist");

        return optionalEvent.get();
    }

    public void createEvent(Event event) {
        if (eventRepository.findById(event.getHostId()).isPresent())
            throw new DuplicateKeyException("Event with id: " + event.getHostId() + " already exists");

        eventRepository.save(event);
    }


    @Transactional
    public void startEvent(Long hostId) {
        Event event = getEvent(hostId);

        if (event.getStarted())
            throw new IllegalStateException("Event has already started");

        event.setStarted(true);


    }

    public void deleteEvent(Long hostId) {
        getEvent(hostId);

        eventRepository.deleteById(hostId);
    }

    @Transactional
    public void joinEvent(String joinCode, String deviceId) {
        Event e = getEvent(Event.SQID.decode(joinCode).get(0));
        Host host = userService.getUserByDeviceId(deviceId);

        if(host == null)
            host = userService.register(new Host(deviceId));

        if (e.getStarted())
            throw new IllegalStateException("Tried to join a started event!");

        e.addGuest(host);
    }

    public ArrayList<ArrayList<Host>> matchUsers(Event e){
        ArrayList<Host> toBeMatched = new ArrayList<>(e.getGuests());
        Collections.shuffle(toBeMatched);
        ArrayList<ArrayList<Host>> groups = new ArrayList<>();

        int groupSize = e.getGroupSize();
        int amountOfGroups = toBeMatched.size() / groupSize;

        for(int i = 0; i < amountOfGroups; i++)
            groups.add(new ArrayList<>());

        for(int amountMatched = 0; amountMatched < toBeMatched.size(); amountMatched++){
            Host u = toBeMatched.get(amountMatched);
            groups.get(amountMatched % amountOfGroups).add(u);
        }

        //todo connect to solution for saving/processing groups, shouldn't return like this
        return groups;
    }

}
