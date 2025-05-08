package com.pvt.SocialSips.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Trivia;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolType;
import com.pvt.SocialSips.role.Role;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIT {
    private static final String TEST_USER_SUB = "TEST USER SUB";
    private static final String TEST_USER_FIRST_NAME = "THIS IS A FIRST NAME";

    private static final User USER = new User(TEST_USER_FIRST_NAME, TEST_USER_SUB, List.of(new Role("ROLE_OIDC_USER")));
    private static final Questpool QUESTPOOL_ONE = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_TWO = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_THREE = new Questpool("A quest pool", QuestpoolType.TRIVIA,
            new HashSet<>(List.of(new Trivia("Question two", new HashSet<>(List.of("opp1", "correct", "opp3", "opp4")), 2))));

    private static final OidcUser OIDC_USER = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_OIDC_USER"),
            OidcIdToken.withTokenValue("id-token").claim("sub", TEST_USER_SUB).build(),
            "sub");


    private static String THREE_QUESTPOOLS_IN_JSON_EXPECTED;

    private final UserRepository userRepository;

    private final MockMvc mockMvc;

    @Autowired
    public UserIT(UserRepository userRepository, MockMvc mockMvc) {
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        USER.addQuestpool(QUESTPOOL_ONE);
        USER.addQuestpool(QUESTPOOL_TWO);
        USER.addQuestpool(QUESTPOOL_THREE);
        User user = userRepository.save(USER);

        THREE_QUESTPOOLS_IN_JSON_EXPECTED = ow.writeValueAsString(user.getQuestpools());
    }

    @AfterAll
    public void shutDown() {
        userRepository.deleteById(USER.getSub());
    }

    @Test
    public void getAllQuestpools_UserNotAuthorized_IsRedirected() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    public void getAllQuestpools_HostExists_HTTPCodeIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllQuestpools_HostWithQuestpools_QuestpoolsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(THREE_QUESTPOOLS_IN_JSON_EXPECTED));
    }

    @Test
    public void profile_HostExists_HostNameReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(oidcLogin().oidcUser(OIDC_USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(USER.getFirstName()));
    }

    @Test
    public void profile_UserNotAuthorized_IsRedirected() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").secure(true)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

}
