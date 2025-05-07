package com.pvt.SocialSips.user;

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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIntegrationTest {

    private static final String TEST_USER_SUB = "TEST USER SUB";

    private static final String URL = "https://localhost/user";


    private static final OidcUser OIDC_USER = new DefaultOidcUser(
            AuthorityUtils.createAuthorityList("ROLE_HOST"),
            OidcIdToken.withTokenValue("id-token").claim("sub", TEST_USER_SUB).build(),
            "sub");

    private static final int ZERO = 0;

    private UserRepository userRepository;

    private final MockMvc mockMvc;

    @Autowired
    public UserIntegrationTest(UserRepository userRepository, MockMvc mockMvc) {
        this.userRepository = userRepository;
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public void setup(){
        userRepository.save(new User(TEST_USER_SUB, TEST_USER_SUB));
    }

    @AfterAll
    public void shutDown(){
        userRepository.delete(new User(TEST_USER_SUB, TEST_USER_SUB));
    }

    @Test
    public void getAllQuestpools_HostExists_HTTPCodeIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/").with(oidcLogin().oidcUser(OIDC_USER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
