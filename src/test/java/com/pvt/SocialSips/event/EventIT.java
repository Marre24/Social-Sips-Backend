package com.pvt.SocialSips.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pvt.SocialSips.role.Role;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventIT {

    private static final String USER_SUB_WITH_EVENT = "731723";

    private static final User USER_WITH_EVENT = new User("THIS IS A FIRST NAME", USER_SUB_WITH_EVENT, List.of(new Role("ROLE_OIDC_USER")));
    private static final Event EVENT = new Event("THIS IS AN EVENT", 2, new HashSet<>(), USER_SUB_WITH_EVENT);

    private static final OidcUser OIDC_USER = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_HOST"),
            OidcIdToken.withTokenValue("id-token").claim("sub", USER_SUB_WITH_EVENT).build(),
            "sub");

    private static final String USER_SUB_WITHOUT_EVENT = "696969";
    private static final User USER_WITHOUT_EVENT = new User("THIS IS A FIRST NAME", USER_SUB_WITHOUT_EVENT, List.of(new Role("ROLE_OIDC_USER")));
    private static final Event EVENT_WITHOUT = new Event("THIS IS AN EVENT", 2, new HashSet<>(), USER_SUB_WITHOUT_EVENT);

    private static final OidcUser OIDC_USER_WITHOUT_EVENTS = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_HOST"),
            OidcIdToken.withTokenValue("id-token").claim("sub", USER_SUB_WITHOUT_EVENT).build(),
            "sub");

    private static String EVENT_WITHOUT_IN_JSON_EXPECTED;
    private static String EVENT_IN_JSON_EXPECTED;

    private final UserService userService;
    private final EventService eventService;
    private final MockMvc mockMvc;

    @Autowired
    public EventIT(UserService userService, EventService eventService, MockMvc mockMvc){
        this.userService = userService;
        this.eventService = eventService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        userService.register(USER_WITH_EVENT);
        eventService.createEvent(EVENT, USER_SUB_WITH_EVENT);

        userService.register(USER_WITHOUT_EVENT);

        EVENT_IN_JSON_EXPECTED = ow.writeValueAsString(EVENT);
        EVENT_WITHOUT_IN_JSON_EXPECTED = ow.writeValueAsString(EVENT_WITHOUT);
    }



    @BeforeEach
    public void beforeEach(){

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
        mockMvc.perform(MockMvcRequestBuilders.get("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(EVENT_IN_JSON_EXPECTED));
    }

    @Test
    public void getEvent_WithoutEvent_HTTPStatusNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createEvent_HostWithoutEvent_HTTPCodeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVENT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createEvent_HostWithoutEvent_EventCreated() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.get("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(EVENT_WITHOUT_IN_JSON_EXPECTED));
    }

    @Test
    public void createEvent_HostWithEvent_HTTPStatusIsConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVENT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void startEvent_HostWithEvent_EventStarted() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertTrue(userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent().getStarted());
    }

    @Test
    public void startEvent_HostWithStartedEvent_HTTPCodeIsConflict() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void deleteEvent_HostWithEvent_EventDeleted() throws Exception {
        postEventToHostWithoutEvent();

        mockMvc.perform(MockMvcRequestBuilders.delete("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(null, userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent());
    }

    @Test
    public void deleteEvent_HostWithoutEvent_HTTPStatusIsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    private void postEventToHostWithoutEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER_WITHOUT_EVENTS))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EVENT_WITHOUT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print());
    }
}
