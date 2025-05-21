package com.pvt.SocialSips.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{sub}")
    public ResponseEntity<?> getEvent(@PathVariable String sub) {
        try {
            Event e = eventService.getEvent(sub);
            return new ResponseEntity<>(e, HttpStatus.OK);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{sub}")
    public ResponseEntity<String> createEvent(@RequestBody Event event, @PathVariable String sub) {
        try {
            eventService.createEvent(event, sub);
            return new ResponseEntity<>("Event created", HttpStatus.OK);

        } catch (DuplicateKeyException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/start/{sub}")
    public ResponseEntity<String> startEvent(@PathVariable String sub) {
        try {
            eventService.startEvent(sub);
            return new ResponseEntity<>("Event Started", HttpStatus.OK);

        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{sub}")
    public ResponseEntity<String> deleteEvent(@PathVariable String sub) {
        try {
            eventService.deleteEvent(sub);
            return new ResponseEntity<>("Event deleted", HttpStatus.OK);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/join/{joinCode}")
    public ResponseEntity<String> canJoinEvent(@PathVariable String joinCode) {
        try {
            if (eventService.canJoinEvent(joinCode))
                return new ResponseEntity<>("Event with join code: " + joinCode + " can be joined!", HttpStatus.OK);
            return new ResponseEntity<>("Event with join code: " + joinCode + " has already started!", HttpStatus.CONFLICT);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
