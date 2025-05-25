/*
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class EventIT {

    private static final String USER_SUB_WITH_EVENT = "731723278365822873458";

    private static final User USER_WITH_EVENT = new User("THIS IS A FIRST NAME", USER_SUB_WITH_EVENT, List.of(new Role("ROLE_OIDC_USER")));
    private static final Event EVENT = new Event("THIS IS AN EVENT", 2, new HashSet<>(), USER_SUB_WITH_EVENT);

    private static final OidcUser OIDC_USER = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_HOST"),
            OidcIdToken.withTokenValue("id-token").claim("sub", USER_SUB_WITH_EVENT).build(),
            "sub");

    private static final String USER_SUB_WITHOUT_EVENT = "696969696969969696996";
    private static final User USER_WITHOUT_EVENT = new User("THIS IS A FIRST NAME", USER_SUB_WITHOUT_EVENT, List.of(new Role("ROLE_OIDC_USER")));
    private static final Event EVENT_WITHOUT = new Event("THIS IS AN EVENT", 2, new HashSet<>(), USER_SUB_WITHOUT_EVENT);

    private static final Jwt JWT_EVENTS = Jwt.withTokenValue(OIDC_USER.getIdToken().getTokenValue())
            .header("alg", "RS256")
            .claim("firebase", "FIREBASE_TOKEN_1337")
            .claim("name", USER_SUB_WITH_EVENT)
            .claim("sub", OIDC_USER.getSubject())
            .claim("roles", "ROLE_USER")
            .claim("scope", List.of("openid", "gmail", "profile", "write:event"))
            .claim("authorities", List.of("ROLE_USER"))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

    private static final Jwt JWT_NO_EVENTS = Jwt.withTokenValue(OIDC_USER.getIdToken().getTokenValue())
            .header("alg", "RS256")
            .claim("firebase", "FIREBASE_TOKEN_1337")
            .claim("name", OIDC_USER.getName())
            .claim("sub", USER_SUB_WITH_EVENT)
            .claim("scope", List.of("openid", "gmail", "profile", "write:event", "delete:event", "patch:event"))
            .claim("authorities", List.of("ROLE_USER"))
            .claim("roles", "ROLE_USER")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

    private static final OidcUser OIDC_USER_WITHOUT_EVENTS = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_HOST"),
            OidcIdToken.withTokenValue("id-token").claim("sub", USER_SUB_WITHOUT_EVENT).build(),
            "sub");

    private static final JwtAuthenticationToken JWT_AUTHENTICATION_TOKEN_EVENTS = new JwtAuthenticationToken(JWT_EVENTS);
    private static final JwtAuthenticationToken JWT_AUTHENTICATION_TOKEN_NO_EVENTS = new JwtAuthenticationToken(JWT_NO_EVENTS);
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
        authenticateTokenWithEvents();

        mockMvc.perform(MockMvcRequestBuilders.get("/event/").secure(true)
                        .header("Authentication: Bearer ", JWT_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(EVENT_IN_JSON_EXPECTED));
    }

    @Test
    public void getEvent_WithoutEvent_HTTPStatusNotFound() throws Exception {
        authenticateTokenWithoutEvents();

        SecurityContextHolder.getContext().setAuthentication(JWT_AUTHENTICATION_TOKEN_NO_EVENTS);
        SecurityContextImpl sc = new SecurityContextImpl(JWT_AUTHENTICATION_TOKEN_NO_EVENTS);
        SecurityContextHolder.setContext(sc);

        mockMvc.perform(MockMvcRequestBuilders.get("/event/").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(securityContext(SecurityContextHolder.getContext()))
                        .contentType(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createEvent_HostWithoutEvent_HTTPCodeOk() throws Exception {
        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.post("/event/").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(APPLICATION_JSON)
                        .content(EVENT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createEvent_HostWithoutEvent_EventCreated() throws Exception {
        postEventToHostWithoutEvent();

        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.get("/event").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(EVENT_WITHOUT_IN_JSON_EXPECTED));
    }

    @Test
    public void createEvent_HostWithEvent_HTTPStatusIsConflict() throws Exception {
        authenticateTokenWithEvents();

        mockMvc.perform(MockMvcRequestBuilders.post("/event").secure(true)
                        .header("Authentication: Bearer ", JWT_EVENTS.getTokenValue())
                        .with(authentication(JWT_AUTHENTICATION_TOKEN_EVENTS))
                        .contentType(APPLICATION_JSON)
                        .content(EVENT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void startEvent_HostWithEvent_EventStarted() throws Exception {
        postEventToHostWithoutEvent();

        authenticateTokenWithEvents();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start").secure(true)
                        .header("Authentication: Bearer ", JWT_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertTrue(userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent().getStarted());
    }

    @Test
    public void startEvent_HostWithStartedEvent_HTTPCodeIsConflict() throws Exception {
        postEventToHostWithoutEvent();
        authenticateTokenWithEvents();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print());

        authenticateTokenWithEvents();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void deleteEvent_HostWithEvent_EventDeleted() throws Exception {
        postEventToHostWithoutEvent();

        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.delete("/event").secure(true)
                        .header("Authentication: Bearer ", JWT_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertNull(userService.getUserBySub(USER_SUB_WITHOUT_EVENT).getEvent());
    }

    @Test
    public void deleteEvent_HostWithoutEvent_HTTPStatusIsNotFound() throws Exception {
        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.delete("/event").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void canJoinEvent_NonStartedEvent_HTTPStatusIsOk() throws Exception {
        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.get("/event/join/" + EVENT.getJoinCode()).secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void canJoinEvent_StartedEvent_HTTPStatusIsConflict() throws Exception {
        postEventToHostWithoutEvent();

        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.patch("/event/start").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print());

        authenticateTokenWithoutEvents();

        mockMvc.perform(MockMvcRequestBuilders.get("/event/join/" + EVENT_WITHOUT.getJoinCode()).secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }


    private void postEventToHostWithoutEvent() throws Exception {
        authenticateTokenWithoutEvents();

        SecurityContextHolder.setContext(new SecurityContextImpl(JWT_AUTHENTICATION_TOKEN_NO_EVENTS));

        mockMvc.perform(MockMvcRequestBuilders.post("/event").secure(true)
                        .header("Authentication: Bearer ", JWT_NO_EVENTS.getTokenValue())
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(APPLICATION_JSON)
                        .content(EVENT_WITHOUT_IN_JSON_EXPECTED))
                .andDo(MockMvcResultHandlers.print());
    }

    public void authenticateTokenWithEvents(){
        SecurityContextHolder.getContext().setAuthentication(JWT_AUTHENTICATION_TOKEN_EVENTS);
        SecurityContextImpl sc = new SecurityContextImpl(JWT_AUTHENTICATION_TOKEN_EVENTS);
        SecurityContextHolder.setContext(sc);
    }

    private void authenticateTokenWithoutEvents(){
        SecurityContextHolder.getContext().setAuthentication(JWT_AUTHENTICATION_TOKEN_NO_EVENTS);
        SecurityContextImpl sc = new SecurityContextImpl(JWT_AUTHENTICATION_TOKEN_NO_EVENTS);
        SecurityContextHolder.setContext(sc);
    }
}
*/
