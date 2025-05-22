package com.pvt.SocialSips.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pvt.SocialSips.util.JwtParser.extractSub;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<?> getEvent() {
        try {
            Event e = eventService.getEvent(extractSub());
            return new ResponseEntity<>(e, HttpStatus.OK);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        try {
            Event newEvent = eventService.createEvent(event, extractSub());
            return new ResponseEntity<>(newEvent.getJoinCode(), HttpStatus.OK);

        } catch (DuplicateKeyException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/start")
    public ResponseEntity<String> startEvent() {
        try {
            eventService.startEvent(extractSub());
            return new ResponseEntity<>("Event Started", HttpStatus.OK);

        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteEvent() {
        try {
            eventService.deleteEvent(extractSub());
            return new ResponseEntity<>("Event deleted", HttpStatus.OK);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/join/{joinCode}")
    public ResponseEntity<String> canJoinEvent(@PathVariable String joinCode) {
        try {
            if (eventService.canJoinEvent(joinCode))
                return new ResponseEntity<>("Event with join code: " + joinCode + " is able to be joined!", HttpStatus.OK);
            return new ResponseEntity<>("Event with join code: " + joinCode + " has already started!", HttpStatus.CONFLICT);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/questpools/{joinCode}")
    public ResponseEntity<?> getQuestpools(@PathVariable String joinCode) {
        try {
            var questpools = eventService.getQuestpoolsFor(joinCode);

            return ResponseEntity.ok(questpools);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
