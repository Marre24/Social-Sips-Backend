package com.pvt.SocialSips.event;

import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserRepository;
import com.pvt.SocialSips.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    private final static int TWO_GROUP_SIZE = 2;
    private final static int THREE_GROUP_SIZE = 3;

    private final static String USER_SUB = "69";
    private final static String NON_STARTED_USER_SUB = "420";
    private final static String STARTED_USER_SUB = "1337";


    private final static User USER = new User("firstName", USER_SUB);
    private final static User NON_STARTED_USER = new User("firstName", NON_STARTED_USER_SUB);
    private final static User STARTED_USER = new User("firstName", STARTED_USER_SUB);

    private final static Event EVENT = new Event("name", 2, new HashSet<>(), USER);
    private final static Set<String> GUESTS = new HashSet<>(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
    private final static Set<String> ELEVEN_GUESTS = new HashSet<>(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"));

    @Test
    public void getEvent_EventExists_EventReturned() {
        when(eventRepository.findById(USER_SUB)).thenReturn(Optional.of(EVENT));

        assertEquals(EVENT, eventService.getEvent(USER_SUB));
    }

    @Test
    public void getEvent_EventDoesNotExist_EntityNotFoundExceptionThrown() {
        when(eventRepository.findById(USER_SUB)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.getEvent(USER_SUB));
    }


    @Test
    public void createEvent_EmptyDatabase_EventCreated() {
        when(eventRepository.findById(USER_SUB)).thenReturn(Optional.empty());
        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        assertDoesNotThrow(() -> eventService.createEvent(EVENT));
    }

    @Test
    public void createEvent_EventWithHostIdExists_DuplicateKeyExceptionThrown() {
        when(eventRepository.findById(USER_SUB)).thenReturn(Optional.of(EVENT));
        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        assertThrows(DuplicateKeyException.class, () -> eventService.createEvent(EVENT));
    }

    @Test
    public void startEvent_EventNotStarted_EventStarted() {
        Event nonStartedEvent = new Event("NonStartedEvent", 2, new HashSet<>(), NON_STARTED_USER);

        when(eventRepository.findById(NON_STARTED_USER_SUB)).thenReturn(Optional.of(nonStartedEvent));
        eventService.startEvent(nonStartedEvent.getHostSub());

        assertTrue(nonStartedEvent.getStarted());
    }

    @Test
    public void startEvent_EventAlreadyStarted_IllegalStateExceptionThrown() {
        Event startedEvent = new Event("NonStartedEvent", 2, new HashSet<>(), STARTED_USER);
        startedEvent.setStarted(true);

        when(eventRepository.findById(startedEvent.getHostSub())).thenReturn(Optional.of(startedEvent));

        assertThrows(IllegalStateException.class, () -> eventService.startEvent(startedEvent.getHostSub()));
    }

    @Test
    public void deleteEvent_EventExists_EventDeleted() {
        when(eventRepository.findById(EVENT.getHostSub())).thenReturn(Optional.of(EVENT));

        assertDoesNotThrow(() -> eventService.deleteEvent(EVENT.getHostSub()));
    }

    @Test
    public void codeGenerator_IdIsTen_CodeIsGenerated() {
        Event idTen = new Event("NonStartedEvent", 2, new HashSet<>(), USER);

        assertNotNull(idTen.getJoinCode());
    }

    @Test
    public void canJoinEvent_NonStartedEvent_EventCouldBeJoined() {
        when(eventRepository.findById(EVENT.getHostSub())).thenReturn(Optional.of(EVENT));

        assertTrue(eventService.canJoinEvent(EVENT.getJoinCode()));
    }

    @Test
    public void canJoinEvent_StartedEvent_EventCouldntBeJoined() {
        Event startedEvent = new Event("NonStartedEvent", 2, new HashSet<>(), USER);
        startedEvent.setStarted(true);
        when(eventRepository.findById(startedEvent.getHostSub())).thenReturn(Optional.of(startedEvent));

        assertFalse(eventService.canJoinEvent(startedEvent.getJoinCode()));
    }

    @Test
    public void matchUsers_EvenlyDividedGroups_CorrectGroupAmount() {

        assertEquals(
                GUESTS.size() / TWO_GROUP_SIZE,
                EventService.matchUsers(GUESTS, TWO_GROUP_SIZE).size());
    }

    @Test
    public void matchUsers_EvenlyDividedGroups_CorrectGroupSize() {

        assertEquals(TWO_GROUP_SIZE, EventService.matchUsers(GUESTS, TWO_GROUP_SIZE).get(0).size());
    }


    @Test
    public void matchUsers_UnevenlyDividedPairs_AddedExtraToFirstPair() {
        assertEquals(TWO_GROUP_SIZE + 1, EventService.matchUsers(ELEVEN_GUESTS, TWO_GROUP_SIZE).get(0).size());
    }


    @Test
    public void matchUsers_UnevenlyDividedGroups_CorrectGroupSize() {

        assertEquals(THREE_GROUP_SIZE + 1, EventService.matchUsers(ELEVEN_GUESTS, THREE_GROUP_SIZE).get(0).size());
    }


}
