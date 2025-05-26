/*
package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final static User USER_WITH_QUESTPOOLS = new User("firstName", "ThisIsASub");
    private final static User USER_WITHOUT_QUESTPOOLS = new User("firName", "ThisIsASuuub");
    private final static int AMOUNT_OF_USR_QUESTPOOLS = 3;

    @BeforeAll
    public void setup(){
        USER_WITH_QUESTPOOLS.addQuestpool(new Questpool("one", QuestpoolType.ICEBREAKER, new HashSet<>()));
        USER_WITH_QUESTPOOLS.addQuestpool(new Questpool("two", QuestpoolType.ICEBREAKER, new HashSet<>()));
        USER_WITH_QUESTPOOLS.addQuestpool(new Questpool("three", QuestpoolType.ICEBREAKER, new HashSet<>()));
        when(userRepository.findById(USER_WITH_QUESTPOOLS.getSub())).thenReturn(Optional.of(USER_WITH_QUESTPOOLS));

        when(userRepository.findById(USER_WITHOUT_QUESTPOOLS.getSub())).thenReturn(Optional.of(USER_WITHOUT_QUESTPOOLS));
    }

    @Test
    public void getAllQuestpools_UserExists_QuestpoolsNotNull() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITHOUT_QUESTPOOLS.getSub());

        assertNotNull(questpoolSet);
    }

    @Test
    public void getAllQuestpools_UserWithoutQPs_EmptySet() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITHOUT_QUESTPOOLS.getSub());

        assertTrue(questpoolSet.isEmpty());
    }

    @Test
    public void getAllQuestpools_UserExists_CorrectAmountOfQPs() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITH_QUESTPOOLS.getSub());

        assertEquals(AMOUNT_OF_USR_QUESTPOOLS, questpoolSet.size());
    }

    @Test
    public void removeQuestpool_UserExists_AmountOfQuestpoolsDecreased() {
        User user = new User();
        Questpool questpoolToRemove = new Questpool("two", QuestpoolType.ICEBREAKER, new HashSet<>());
        user.addQuestpool(new Questpool("one", QuestpoolType.ICEBREAKER, new HashSet<>()));
        user.addQuestpool(questpoolToRemove);

        int expected = user.getQuestpools().size() - 1;

        userService.removeQuestpoolFrom(user, questpoolToRemove);
        assertEquals(expected, user.getQuestpools().size());
    }
}
*/
