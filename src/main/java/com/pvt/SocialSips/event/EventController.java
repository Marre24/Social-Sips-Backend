package com.pvt.SocialSips.event;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://group-2-75.pvt.dsv.su.se/")
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{hostId}")
    public ResponseEntity<?> getEvent(@PathVariable Long hostId){
        try{
            Event e = eventService.getEvent(hostId);
            return new ResponseEntity<>(e, HttpStatus.OK);

        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> postEvent(@RequestBody Event event){
        try{
            eventService.createEvent(event);
            return new ResponseEntity<>("Event created", HttpStatus.OK);

        } catch (DuplicateKeyException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/start/{hostId}")
    public ResponseEntity<String> startEvent(@PathVariable Long hostId){
        try{

            eventService.startEvent(hostId);
            return new ResponseEntity<>("Event Started", HttpStatus.OK);

        } catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{hostId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long hostId){
        try{
            eventService.deleteEvent(hostId);
            return new ResponseEntity<>("Event deleted", HttpStatus.OK);

        } catch (EntityNotFoundException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/join/{joinCode}")
    public ResponseEntity<String> canJoinEvent(@PathVariable String joinCode){
        try {
            if (eventService.canJoinEvent(joinCode))
                return new ResponseEntity<>("Event with join code: " + joinCode + " can be joined!", HttpStatus.OK);
            return new ResponseEntity<>("Event with join code: " + joinCode + " has already started!", HttpStatus.CONFLICT);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
