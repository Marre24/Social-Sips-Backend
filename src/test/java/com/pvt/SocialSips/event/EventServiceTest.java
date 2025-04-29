package com.pvt.SocialSips.event;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private final static Event EVENT = new Event(21343211L, "name", 2, new HashSet<>());
    private final static String DEVICE_ID = "thisIsAUUID";

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
        startedEvent.setStarted();

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
    public void joinEvent_NonStartedEvent_GuestAdded(){
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.of(EVENT));

        eventService.joinEvent(EVENT.getJoinCode(), DEVICE_ID);
        assertFalse(EVENT.getGuests().isEmpty());
    }

    @Test
    public void joinEvent_StartedEvent_IllegalStateExceptionThrown(){
        Event startedEvent = new Event(2L, "NonStartedEvent", 2, new HashSet<>());
        startedEvent.setStarted();
        when(eventRepository.findById(startedEvent.getHostId())).thenReturn(Optional.of(startedEvent));
        
        assertThrows(IllegalStateException.class, () -> eventService.joinEvent(startedEvent.getJoinCode(), DEVICE_ID));
    }
}
