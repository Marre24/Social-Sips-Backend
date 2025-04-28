package com.pvt.SocialSips.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(Event event) {
        if (eventRepository.findById(event.getHostId()).isPresent())
            throw new DuplicateKeyException("Event with id: " + event.getHostId() + " already exists");

        eventRepository.save(event);
    }

    public Event getEvent(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if(optionalEvent.isEmpty())
            throw new EntityNotFoundException("Event with id: " + id + " does not exist");

        return optionalEvent.get();
    }
}
