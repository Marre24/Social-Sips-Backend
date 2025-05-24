package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.Guest;
import com.pvt.SocialSips.user.UserService;
import com.pvt.SocialSips.util.ColorHasher;
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
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
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
            Event newEvent = eventService.createEvent(event, sub);
            return new ResponseEntity<>(newEvent.getJoinCode(), HttpStatus.OK);

        } catch (DuplicateKeyException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
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

    @GetMapping("/join/{joinCode}/{uuid}")
    public ResponseEntity<String> joinEvent(@PathVariable String joinCode, @PathVariable String uuid) {
        try {
            if (eventService.canJoinEvent(joinCode)) {
                eventService.joinEvent(joinCode, uuid);

                return new ResponseEntity<>("Guest with uuid: " + uuid + " joined Event with join code: " + joinCode, HttpStatus.OK);
            }

            return new ResponseEntity<>("Event with join code: " + joinCode + " has already started!", HttpStatus.CONFLICT);

        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }catch (DuplicateKeyException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/started/{joinCode}/{uuid}")
    public ResponseEntity<String> isStarted(@PathVariable String joinCode, @PathVariable String uuid) {
        try {
            if (eventService.isStarted(joinCode)){
                Guest g = userService.getGuest(uuid);

                Event e = eventService.getByJoinCode(uuid);

                if (!e.getGuests().contains(g))
                    return new ResponseEntity<>("Guest: " + uuid + " is not in event with join code: " + joinCode, HttpStatus.NOT_FOUND);


                return new ResponseEntity<>(ColorHasher.intToColorHex(g.getGroupNumber()), HttpStatus.OK);
            }

            return new ResponseEntity<>("The event is not started!", HttpStatus.CONFLICT);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
