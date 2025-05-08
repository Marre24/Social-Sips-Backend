package com.pvt.SocialSips.questpool;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Trivia;
import com.pvt.SocialSips.role.Role;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestpoolIT {

    private static final String TEST_USER_SUB = "TEST USER SUB";
    private static final User TEST_USER = new User(TEST_USER_SUB, TEST_USER_SUB, List.of(new Role("ROLE_OIDC_USER")));

    private static final OidcUser OIDC_USER = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_OIDC_USER"),
            OidcIdToken.withTokenValue("id-token").claim("sub", TEST_USER_SUB).build(),
            "sub");

    private static final Questpool QUESTPOOL_TO_BE_ADDED = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_ONE = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_TWO = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_THREE = new Questpool("A quest pool", QuestpoolType.TRIVIA,
            new HashSet<>(List.of(new Trivia("Question two", new HashSet<>(List.of("opp1", "correct", "opp3", "opp4")), 2))));

    private static final String OLD_QUESTS = "[ { \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" } ]";
    private static final String NEW_QUESTS = "[ " +
                                                    "{ \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" }," +
                                                    "{ \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" }," +
                                                    "{ \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" }" +
                                                " ]";

    private final UserService userService;

    private final MockMvc mockMvc;

    @Autowired
    public QuestpoolIT(UserService userService, MockMvc mockMvc) {
        this.userService = userService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setup() {
        TEST_USER.addQuestpool(QUESTPOOL_ONE);
        TEST_USER.addQuestpool(QUESTPOOL_TWO);
        TEST_USER.addQuestpool(QUESTPOOL_THREE);

        userService.register(TEST_USER);
    }

    @AfterAll
    public void shutDown() {
        userService.deleteUser(TEST_USER);
    }

    @Test
    public void addQuestpool_ValidHost_HTTPCodeIsOK() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(QUESTPOOL_TO_BE_ADDED);

        mockMvc.perform(MockMvcRequestBuilders.post("/questpool/")
                        .with(oidcLogin().oidcUser(OIDC_USER)).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addQuestpool_ValidHost_HostQuestpoolsSizeIsIncrementedByOne() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(QUESTPOOL_TO_BE_ADDED);

        int expectedAmount = userService.getUserBySub(TEST_USER_SUB).getQuestpools().size() + 1;

        mockMvc.perform(MockMvcRequestBuilders.post("/questpool/")
                        .with(oidcLogin().oidcUser(OIDC_USER)).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        int actual = userService.getUserBySub(TEST_USER_SUB).getQuestpools().size();
        assertEquals(expectedAmount, actual);
    }

    @Test
    public void deleteQuestpool_ValidHost_HTTPCodeIsOK() throws Exception {
        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();

        mockMvc.perform(MockMvcRequestBuilders.delete("/questpool/" + questpool.getId())
                        .with(oidcLogin().oidcUser(OIDC_USER)).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteQuestpool_ValidHost_HostQuestpoolsSizeIsDecrementedByOne() throws Exception {
        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        int expectedAmount = set.size() - 1;
        var it = set.iterator();
        var questpool = it.next();

        mockMvc.perform(MockMvcRequestBuilders.delete("/questpool/" + questpool.getId())
                        .with(oidcLogin().oidcUser(OIDC_USER)).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        int actual = userService.getUserBySub(TEST_USER_SUB).getQuestpools().size();
        assertEquals(expectedAmount, actual);
    }

    @Test
    public void updateQuestpool_ValidHost_HTTPCodeIsOK() throws Exception {

        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();

        mockMvc.perform(MockMvcRequestBuilders.patch("/questpool/" + questpool.getId())
                        .with(oidcLogin().oidcUser(OIDC_USER)).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_JSON)
                        .content(OLD_QUESTS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateQuestpool_ValidHost_HostQuestpoolsSizeIsDecrementedByOne() throws Exception {
        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();
        var expectedNotToBe = questpool.getQuests();



        mockMvc.perform(MockMvcRequestBuilders.patch("/questpool/" + questpool.getId())
                        .with(oidcLogin().oidcUser(OIDC_USER)).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_JSON)
                        .content(NEW_QUESTS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        var newSet = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var newIt = newSet.iterator();
        var actualQuests = newIt.next().getQuests();

        assertNotEquals(expectedNotToBe, actualQuests);
    }

}
