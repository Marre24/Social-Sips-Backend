package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserRepository;
import com.pvt.SocialSips.user.UserService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private EventService eventService;



    private final static Event EVENT = new Event(21343211L, "name", 2, new HashSet<>());
    private final static String DEVICE_ID = "thisIsAUUID";
    private final static ArrayList<User> GUESTS = new ArrayList<>(List.of(
            new User("1"),
            new User("2"),
            new User("3"),
            new User("4"),
            new User("5"),
            new User("6"),
            new User("7"),
            new User("8"),
            new User("9"),
            new User("10") ));

    @Test
    public void getEvent_EventExists_EventReturned() {
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.of(EVENT));

        assertEquals(EVENT, eventService.getEvent(EVENT.getHostId()));
    }

    @Test
    public void getEvent_EventDoesNotExist_EntityNotFoundExceptionThrown() {
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.getEvent(EVENT.getHostId()));
    }


    @Test
    public void createEvent_EmptyDatabase_EventCreated() {
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.empty());
        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        assertDoesNotThrow(() -> eventService.createEvent(EVENT));
    }

    @Test
    public void createEvent_EventWithHostIdExists_DuplicateKeyExceptionThrown() {
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.of(EVENT));
        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        assertThrows(DuplicateKeyException.class, () -> eventService.createEvent(EVENT));
    }

    @Test
    public void startEvent_EventNotStarted_EventStarted() {
        Event nonStartedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());

        when(eventRepository.findById(nonStartedEvent.getHostId())).thenReturn(Optional.of(nonStartedEvent));
        eventService.startEvent(nonStartedEvent.getHostId());

        assertTrue(nonStartedEvent.getStarted());
    }

    @Test
    public void startEvent_EventAlreadyStarted_IllegalStateExceptionThrown() {
        Event startedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());
        startedEvent.setStarted(true);

        when(eventRepository.findById(startedEvent.getHostId())).thenReturn(Optional.of(startedEvent));

        assertThrows(IllegalStateException.class, () -> eventService.startEvent(startedEvent.getHostId()));
    }

    @Test
    public void deleteEvent_EventExists_EventDeleted(){
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.of(EVENT));

        assertDoesNotThrow(() -> eventService.deleteEvent(EVENT.getHostId()));
    }

    @Test
    public void codeGenerator_IdIsTen_CodeIsGenerated(){
        Event idTen = new Event(10L, "NonStartedEvent", 2, new HashSet<>());

        assertNotNull(idTen.getJoinCode());
    }

    @Test
    public void joinEvent_NonStartedEvent_UserAdded(){
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.of(EVENT));

        eventService.joinEvent(EVENT.getJoinCode(), DEVICE_ID);
        assertFalse(EVENT.getGuests().isEmpty());
    }

    @Test
    public void joinEvent_StartedEvent_IllegalStateExceptionThrown(){
        Event startedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());
        startedEvent.setStarted(true);
        when(eventRepository.findById(startedEvent.getHostId())).thenReturn(Optional.of(startedEvent));

        assertThrows(IllegalStateException.class, () -> eventService.joinEvent(startedEvent.getJoinCode(), DEVICE_ID));
    }

    @Test
    public void matchUsers_EvenlyDividedGroups_CorrectGroupAmount(){
        Event startedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());
        for(User u : GUESTS)
            startedEvent.addGuest(u);
        startedEvent.setStarted(true);

        assertEquals(startedEvent.getGuests().size() / startedEvent.getGroupSize(), eventService.matchUsers(startedEvent).size());
    }

    @Test
    public void matchUsers_EvenlyDividedGroups_CorrectGroupSize(){
        Event startedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());
        for(User u : GUESTS)
            startedEvent.addGuest(u);
        startedEvent.setStarted(true);

        assertEquals(startedEvent.getGroupSize(), eventService.matchUsers(startedEvent).get(0).size());
    }


    @Test
    public void matchUsers_UnevenlyDividedPairs_AddedExtraToFirstPair(){
        Event startedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());
        for(User u : GUESTS)
            startedEvent.addGuest(u);
        startedEvent.addGuest(new User("11"));
        startedEvent.setStarted(true);

        assertEquals(startedEvent.getGroupSize() + 1, eventService.matchUsers(startedEvent).get(0).size());
    }


    @Test
    public void matchUsers_UnevenlyDividedGroups_CorrectGroupSize(){
        Event startedEvent = new Event(2L, "NonStartedEvent", 3, new HashSet<>());
        for(User u : GUESTS)
            startedEvent.addGuest(u);
        startedEvent.setStarted(true);

        assertTrue(eventService.matchUsers(startedEvent).get(0).size() < startedEvent.getGroupSize() * 2);
    }




}
