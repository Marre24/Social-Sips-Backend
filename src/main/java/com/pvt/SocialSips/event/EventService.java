package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.User;
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
        User user = userService.getUserByDeviceId(deviceId);

        if(user == null)
            user = userService.register(new User(deviceId));

        if (e.getStarted())
            throw new IllegalStateException("Tried to join a started event!");

        e.addGuest(user);
    }

    public ArrayList<ArrayList<User>> matchUsers(Event e){
        ArrayList<User> toBeMatched = new ArrayList<>(e.getGuests());
        Collections.shuffle(toBeMatched);
        ArrayList<ArrayList<User>> groups = new ArrayList<>();

        int groupSize = e.getGroupSize();
        int amountOfGroups = toBeMatched.size() / groupSize;

        for(int i = 0; i < amountOfGroups; i++)
            groups.add(new ArrayList<>());

        for(int amountMatched = 0; amountMatched < toBeMatched.size(); amountMatched++){
            User u = toBeMatched.get(amountMatched);
            groups.get(amountMatched % amountOfGroups).add(u);
        }

        //todo connect to solution for saving/processing groups, shouldn't return like this
        return groups;
    }

}
