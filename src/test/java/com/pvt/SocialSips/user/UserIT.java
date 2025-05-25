/*
package com.pvt.SocialSips.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pvt.SocialSips.config.TestSecurityConfig;
import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Trivia;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolType;
import com.pvt.SocialSips.role.Role;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserIT {
    private static final String STANDARD_SUB = "STANDARD";
    private static final String TEST_USER_SUB = "TEST USER SUB";
    private static final String TEST_USER_WITHOUT_SUB = "TEST USER WITHOUT SUB";
    private static final String TEST_USER_FIRST_NAME = "THIS IS A FIRST NAME";

    private static final User USER = new User(TEST_USER_FIRST_NAME, TEST_USER_SUB, List.of(new Role("ROLE_OIDC_USER")));
    private static final Questpool QUESTPOOL_ONE = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_TWO = new Questpool("A quest pool", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(new Icebreaker("prompt"))));
    private static final Questpool QUESTPOOL_THREE = new Questpool(
            "A quest pool",
            QuestpoolType.TRIVIA,
            new HashSet<>(List.of(new Trivia("Question two", "correct;opp2;opp3;opp4"))));

    private static final OidcUser OIDC_USER = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_OIDC_USER"),
            OidcIdToken.withTokenValue("id-token").claim("sub", TEST_USER_SUB).build(),
            "sub");

    private static final User USER_WITHOUT = new User(TEST_USER_FIRST_NAME, TEST_USER_WITHOUT_SUB, List.of(new Role("ROLE_OIDC_USER")));

    private static final OidcUser OIDC_USER_WITHOUT = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_OIDC_USER"),
            OidcIdToken.withTokenValue("id-token").claim("sub", TEST_USER_WITHOUT_SUB).build(),
            "sub");

    private static final Jwt JWT = Jwt.withTokenValue(OIDC_USER.getIdToken().getTokenValue())
            .header("alg", "RS256")
            .claim("firebase", "FIREBASE_TOKEN_1337")
            .claim("name", TEST_USER_FIRST_NAME)
            .claim("sub", TEST_USER_SUB)
            .claim("scope", List.of("openid", "gmail", "profile", "write:questpool"))
            .claim("authorities", List.of("ROLE_OIDC_USER","ROLE_USER"))
            .claim("roles", List.of("ROLE_OIDC_USER","ROLE_USER"))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();
    private static final JwtAuthenticationToken JWT_AUTHENTICATION_TOKEN = new JwtAuthenticationToken(JWT, List.of(new SimpleGrantedAuthority("ROLE_OIDC_USER"),
            new SimpleGrantedAuthority("ROLE_OIDC_USER"),
            new SimpleGrantedAuthority("ROLE_HOST")));


    private static String QUESTPOOLS_IN_JSON_EXPECTED;
    private static String STANDARD_QUESTPOOLS_IN_JSON_EXPECTED;

    private final UserService userService;

    private final MockMvc mockMvc;

    @Autowired
    public UserIT(UserService userService, MockMvc mockMvc) {
        this.userService = userService;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    @Transactional
    public void setup() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        USER.addQuestpool(QUESTPOOL_ONE);
        USER.addQuestpool(QUESTPOOL_TWO);
        USER.addQuestpool(QUESTPOOL_THREE);
        User user = userService.register(USER);

        userService.register(USER_WITHOUT);

        QUESTPOOLS_IN_JSON_EXPECTED = ow.writeValueAsString(userService.getUserBySub(user.getSub()).getQuestpools());
    }

    @AfterAll
    public void shutDown() {
        userService.deleteUser(USER);
        userService.deleteUser(USER_WITHOUT);
    }


    @Test
    public void getAllQuestpools_HostExists_HTTPCodeIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/").secure(true)
                .header("Authentication: Bearer ", JWT.getTokenValue())
                .with(authentication(JWT_AUTHENTICATION_TOKEN)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllQuestpools_HostWithQuestpools_QuestpoolsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/").secure(true)
                .header("Authentication: Bearer ", JWT.getTokenValue())
                .with(authentication(JWT_AUTHENTICATION_TOKEN)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(QUESTPOOLS_IN_JSON_EXPECTED));
    }

    @Test
    public void getAllQuestpools_HostWithoutQuestpools_EmptySetReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/").secure(true)
                        .header("Authentication: Bearer ", JWT.getTokenValue())
                        .with(authentication(JWT_AUTHENTICATION_TOKEN))
                        .contentType(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void profile_HostExists_HostNameReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/profile").secure(true)
                .header("Authentication: Bearer ", JWT.getTokenValue())
                .with(authentication(JWT_AUTHENTICATION_TOKEN)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(USER.getFirstName()));
    }

}
*/
