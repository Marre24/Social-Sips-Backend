package com.pvt.SocialSips.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getEvent(@AuthenticationPrincipal DefaultOidcUser defaultOidcUser) {
        try {
            Event e = eventService.getEvent(defaultOidcUser.getSubject());
            return new ResponseEntity<>(e, HttpStatus.OK);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> createEvent(@AuthenticationPrincipal DefaultOidcUser defaultOidcUser, @RequestBody Event event) {
        try {
            eventService.createEvent(event, defaultOidcUser.getSubject());
            return new ResponseEntity<>("Event created", HttpStatus.OK);

        } catch (DuplicateKeyException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/test/")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        try {
            eventService.createEvent(event);
            return new ResponseEntity<>("Event created", HttpStatus.OK);

        } catch (DuplicateKeyException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }


            @PatchMapping("/start/")
    public ResponseEntity<String> startEvent(@AuthenticationPrincipal DefaultOidcUser defaultOidcUser) {
        try {
            eventService.startEvent(defaultOidcUser.getSubject());
            return new ResponseEntity<>("Event Started", HttpStatus.OK);

        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteEvent(@AuthenticationPrincipal DefaultOidcUser defaultOidcUser) {
        try {
            eventService.deleteEvent(defaultOidcUser.getSubject());
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
