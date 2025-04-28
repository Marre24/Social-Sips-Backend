package com.pvt.SocialSips.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(Event event) {
        if (eventRepository.findById(event.getHostId()).isPresent())
            throw new DuplicateKeyException("Event with given hostId already exists");

        eventRepository.save(event);
    }

}
