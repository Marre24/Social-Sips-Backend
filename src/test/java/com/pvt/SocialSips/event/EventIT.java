package com.pvt.SocialSips.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pvt.SocialSips.user.Guest;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventIT {

    private static final String USER_SUB_WITH_EVENT = "731723278365822873458";
    private static final String USER_SUB_WITHOUT_EVENT = "696969696969969696996";

    private static final User USER_WITH_EVENT = new User("THIS IS A FIRST NAME", USER_SUB_WITH_EVENT);
    private static final User USER_WITHOUT_EVENT = new User("THIS IS A FIRST NAME", USER_SUB_WITHOUT_EVENT);

    private static final Event EVENT = new Event("THIS IS AN EVENT", 2, new HashSet<>(), USER_SUB_WITH_EVENT);
    private static final Event EVENT_WITHOUT = new Event("THIS IS AN EVENT", 2, new HashSet<>(), USER_SUB_WITHOUT_EVENT);

    private static String EVENT_WITHOUT_IN_JSON_EXPECTED;
    private static String EVENT_IN_JSON_EXPECTED;

    private final UserService userService;
    private final EventService eventService;
    private final MockMvc mockMvc;

    @Autowired
    public EventIT(UserService userService, EventService eventService, MockMvc mockMvc) {
        this.userService = userService;
        this.eventService = eventService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        for (int i = 0; i < 11; i++)
            EVENT_WITHOUT.addGuest(new Guest(Integer.toString(i)));

        userService.register(USER_WITH_EVENT);
        userService.register(USER_WITHOUT_EVENT);

        eventService.createEvent(EVENT, USER_SUB_WITH_EVENT);

        EVENT_IN_JSON_EXPECTED = ow.writeValueAsString(EVENT);
        EVENT_WITHOUT_IN_JSON_EXPECTED = ow.writeValueAsString(EVENT_WITHOUT);

    }


    @BeforeEach
    public void beforeEach() {


        userService.deleteUser(USER_WITHOUT_EVENT);
        userService.register(USER_WITHOUT_EVENT);
    }

    @AfterAll
    public void shutDown() {
        userService.deleteUser(USER_WITH_EVENT);
        userService.deleteUser(USER_WITHOUT_EVENT);
    }

    @Test
    public void getEvent_HostWithEvent_EventReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/" + USER_SUB_WITH_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(EVENT_IN_JSON_EXPECTED));
    }

    @Test
    public void getEvent_WithoutEvent_HTTPStatusNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createEvent_HostWithoutEvent_HTTPCodeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVENT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createEvent_HostWithoutEvent_EventCreated() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.get("/event/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(EVENT_WITHOUT_IN_JSON_EXPECTED));
    }

    @Test
    public void createEvent_HostWithEvent_HTTPStatusIsConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/" + USER_SUB_WITH_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVENT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void startEvent_HostWithEvent_EventStarted() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertTrue(userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent().getStarted());
    }

    @Test
    public void startEvent_HostWithStartedEvent_HTTPCodeIsConflict() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void startEvent_HostWithEventWithGuests_GuestGetDividedIntoGroups() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        var guests = userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent().getGuests();

        boolean isOk = true;
        for (Guest g : guests) {
            System.out.println("Guest: " + g.getUuid() + " is in group: " + g.getGroupNumber());
            if(g.getGroupNumber() == -1) {
                isOk = false;
                break;
            }
        }
        assertTrue(isOk);
    }

    @Test
    public void deleteEvent_HostWithEvent_EventDeleted() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.delete("/event/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertNull(userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent());
    }

    @Test
    public void deleteEvent_HostWithoutEvent_HTTPStatusIsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/event/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void joinEvent_NonStartedEvent_HTTPStatusIsOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/event/join/" + EVENT.getJoinCode() + "/" + UUID.randomUUID()).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void joinEvent_StartedEvent_HTTPStatusIsConflict() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/event/join/" + EVENT_WITHOUT.getJoinCode() + "/" + UUID.randomUUID()).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void joinEvent_NonStartedEvent_EventsGuestsIncrementedByOne() throws Exception {
        postEventToHostWithoutEvent();

        Event e = eventService.getEvent(USER_SUB_WITHOUT_EVENT);
        int expected = e.getGuests().size() + 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/event/join/" + EVENT_WITHOUT.getJoinCode() + "/" + UUID.randomUUID()).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Event newEvent = eventService.getEvent(USER_SUB_WITHOUT_EVENT);
        int actual = newEvent.getGuests().size();

        assertEquals(expected, actual);
    }


    //Help method
    private void postEventToHostWithoutEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/" + USER_SUB_WITHOUT_EVENT).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVENT_WITHOUT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print());

        for (int i = 0; i < 11; i++)
            joinEvent(EVENT_WITHOUT.getJoinCode(), Integer.toString(i));
    }


    private void joinEvent(String joinCode, String uuid) throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/event/join/" + joinCode + "/" + uuid).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
