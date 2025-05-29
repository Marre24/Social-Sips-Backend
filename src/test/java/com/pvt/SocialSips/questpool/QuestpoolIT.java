package com.pvt.SocialSips.questpool;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pvt.SocialSips.token.TokenService;
import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Trivia;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestpoolIT {

    private static final String TEST_USER_SUB = "TEST USER SUB";
    private static final User TEST_USER = new User(TEST_USER_SUB, TEST_USER_SUB);

    private static final String OTHER_TEST_USER_SUB = "OTHER TEST USER SUB";
    private static final User OTHER_TEST_USER = new User(OTHER_TEST_USER_SUB, OTHER_TEST_USER_SUB);

    private static final Questpool QUESTPOOL_TO_BE_ADDED = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_ONE = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_TWO = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_THREE = new Questpool(
            "A quest pool",
            QuestpoolType.TRIVIA,
            new HashSet<>(List.of(new Trivia("Question two", "correct;opp2;opp3;opp4"))));

    private static final String OLD_QUESTS = "[ { \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" } ]";
    private static final String NEW_QUESTS = "[ " +
            "{ \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" }," +
            "{ \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" }," +
            "{ \"id\" : null, \"prompt\" : \"A prompt\", \"type\" : \"ICEBREAKER\" }" +
            " ]";

    private static final String QUESTPOOL_NAME = "THIS GOT UPDATED IN A TEST";
    private static final String NEW_NAME = "THIS IS MY NEW NAME";

    private static String TEST_USER_TOKEN;

    private final UserService userService;
    private final TokenService tokenService;

    private final MockMvc mockMvc;

    @Autowired
    public QuestpoolIT(UserService userService, TokenService tokenService, MockMvc mockMvc) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setup() {
        TEST_USER.addQuestpool(QUESTPOOL_ONE);
        TEST_USER.addQuestpool(QUESTPOOL_TWO);
        TEST_USER.addQuestpool(QUESTPOOL_THREE);

        OTHER_TEST_USER.addQuestpool(QUESTPOOL_ONE);

        userService.register(TEST_USER);
        userService.register(OTHER_TEST_USER);

        TEST_USER_TOKEN = tokenService.generateToken(TEST_USER);
    }

    @AfterAll
    public void shutDown() {
        userService.deleteUser(TEST_USER);
        userService.deleteUser(OTHER_TEST_USER);
    }

    @Test
    public void addQuestpool_ValidHost_HTTPCodeIsOK() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(QUESTPOOL_TO_BE_ADDED);

        mockMvc.perform(MockMvcRequestBuilders.post("/questpool").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN)
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

        mockMvc.perform(MockMvcRequestBuilders.post("/questpool").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN)
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/questpool/" + questpool.getId()).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN))
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/questpool/" + questpool.getId()).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        int actual = userService.getUserBySub(TEST_USER_SUB).getQuestpools().size();
        assertEquals(expectedAmount, actual);
    }

    @Test
    public void deleteQuestpool_NonExistingQuestpool_HTTPStatusIsNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/questpool/-1").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteQuestpool_OtherUsersQuestpool_HTTPStatusIsForbidden() throws Exception {
        var set = userService.getUserBySub(OTHER_TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();

        mockMvc.perform(MockMvcRequestBuilders.delete("/questpool/" + questpool.getId()).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void updateQuestpool_ValidHost_HTTPCodeIsOK() throws Exception {

        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();

        mockMvc.perform(MockMvcRequestBuilders.patch("/questpool/" + questpool.getId() + "/" + QUESTPOOL_NAME).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(OLD_QUESTS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateQuestpool_ValidHost_HostQuestpoolsIsChanged() throws Exception {
        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();
        var expectedNotToBe = questpool.getQuests();

        mockMvc.perform(MockMvcRequestBuilders.patch("/questpool/" + questpool.getId()  + "/" + QUESTPOOL_NAME).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN)
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


    @Test
    public void updateQuestpool_AnotherHostsQuestpool_HTTPStatusForbidden() throws Exception {
        var set = userService.getUserBySub(OTHER_TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();

        mockMvc.perform(MockMvcRequestBuilders.patch("/questpool/" + questpool.getId() + "/" + QUESTPOOL_NAME).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(NEW_QUESTS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void updateQuestpool_NewName_NameUpdated() throws Exception {
        var set = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var it = set.iterator();
        var questpool = it.next();
        var oldName = questpool.getName();
        var id = questpool.getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/questpool/" + questpool.getId() + "/" + NEW_NAME).secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(NEW_QUESTS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        var newSet = userService.getUserBySub(TEST_USER_SUB).getQuestpools();

        assertFalse(set.isEmpty());

        var newIt = newSet.iterator();
        Questpool qp = null;
        while (newIt.hasNext()) {
            var temp = newIt.next();
            if (Objects.equals(temp.getId(), id)) {
                qp = temp;
                break;
            }
        }

        if (qp == null)
            fail();

        var actual = qp.getName();
        assertNotEquals(oldName, actual);
        assertEquals(NEW_NAME, actual);
    }

    @Test
    public void getStandardQuestpools_StandardHostExists_HTTPCodeIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/questpool/standard").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + TEST_USER_TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}