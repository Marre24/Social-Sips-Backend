package com.pvt.SocialSips.event;

import com.pvt.SocialSips.questpool.Questpool;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private static Event event = new Event(1L, "name", 2, new HashSet<>());


    @Test
    public void createEvent_EmptyDatabase_EventCreated(){
        when(eventRepository.findById(event.getHostId())).thenReturn(Optional.empty());

        when(eventRepository.save(event)).thenReturn(event);

        assertDoesNotThrow(() -> eventService.createEvent(event));
    }

    @Test
    public void createEvent_EventWithHostIdExists_DuplicateKeyExceptionThrown(){
        when(eventRepository.findById(event.getHostId())).thenReturn(Optional.of(event));

        when(eventRepository.save(event)).thenReturn(event);

        assertThrows(DuplicateKeyException.class, () -> eventService.createEvent(event));
    }

}
