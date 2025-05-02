package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.Guest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEvent(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if(optionalEvent.isEmpty())
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

        if(event.getStarted())
            throw new IllegalStateException("Event has already started");

        event.setStarted(true);

    }

    public void deleteEvent(Long hostId) {
        getEvent(hostId);

        eventRepository.deleteById(hostId);
    }

    public void joinEvent(String joinCode, String deviceId){
        Event e = getEvent(Event.SQID.decode(joinCode).get(0));
        if(e.getStarted())
            throw new IllegalStateException("Tried to join a started event!");

        e.addGuest(new Guest(deviceId));
    }


}
