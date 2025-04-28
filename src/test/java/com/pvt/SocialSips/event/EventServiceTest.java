package com.pvt.SocialSips.event;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private final static Event EVENT = new Event(1L, "name", 2, new HashSet<>());


    @Test
    public void createEvent_EmptyDatabase_EventCreated(){
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.empty());

        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        assertDoesNotThrow(() -> eventService.createEvent(EVENT));
    }

    @Test
    public void createEvent_EventWithHostIdExists_DuplicateKeyExceptionThrown(){
        when(eventRepository.findById(EVENT.getHostId())).thenReturn(Optional.of(EVENT));

        when(eventRepository.save(EVENT)).thenReturn(EVENT);

        assertThrows(DuplicateKeyException.class, () -> eventService.createEvent(EVENT));
    }

}
