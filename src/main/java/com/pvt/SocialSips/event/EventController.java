package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.GroupInfo;
import com.pvt.SocialSips.user.Guest;
import com.pvt.SocialSips.user.UserService;
import com.pvt.SocialSips.util.ColorHasher;
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
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
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
    public ResponseEntity<?> isStarted(@PathVariable String joinCode, @PathVariable String uuid) {
        try {
            if (eventService.isStarted(joinCode)){
                Guest g = userService.getGuest(uuid);

                Event e = eventService.getByJoinCode(joinCode);

                if (!e.getGuests().contains(g))
                    return new ResponseEntity<>("Guest: " + uuid + " is not in event with join code: " + joinCode, HttpStatus.NOT_FOUND);


                return new ResponseEntity<>(new GroupInfo(ColorHasher.intToColorHex(g.getGroupNumber(), e.getAmountOfGroups()), g.getGroupNumber()), HttpStatus.OK);
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

    @GetMapping("/guests")
    public ResponseEntity<?> getAmountOfGuests() {
        try {
            var questpools = eventService.getEvent(extractSub());

            return ResponseEntity.ok(questpools.getGuests().size());
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/guests/{joinCode}/{uuid}")
    public ResponseEntity<?> leaveEvent(@PathVariable String joinCode, @PathVariable String uuid) {
        try {
            eventService.removeFromEvent(joinCode, uuid);

            return ResponseEntity.ok(uuid + " was able to leave event with join code: " + joinCode);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
