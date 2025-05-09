package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final static User USER_WITH_QUESTPOOLS = new User("firstName", "ThisIsASub");
    private final static User USER_WITHOUT_QUESTPOOLS = new User("firName", "ThisIsASuuub");
    private final static User STANDARD = new User("STANDARD", "STANDARD");

    private final static int AMOUNT_OF_STD_QUESTPOOLS = 1;
    private final static int AMOUNT_OF_USR_QUESTPOOLS = 3;

    @BeforeAll
    public void setup(){
        STANDARD.addQuestpool(new Questpool("standard questpool", QuestpoolType.ICEBREAKER, new HashSet<>()));
        when(userRepository.getReferenceById(STANDARD.getSub())).thenReturn(STANDARD);

        USER_WITH_QUESTPOOLS.addQuestpool(new Questpool("one", QuestpoolType.ICEBREAKER, new HashSet<>()));
        USER_WITH_QUESTPOOLS.addQuestpool(new Questpool("two", QuestpoolType.ICEBREAKER, new HashSet<>()));
        USER_WITH_QUESTPOOLS.addQuestpool(new Questpool("three", QuestpoolType.ICEBREAKER, new HashSet<>()));
        when(userRepository.getReferenceById(USER_WITH_QUESTPOOLS.getSub())).thenReturn(USER_WITH_QUESTPOOLS);

        when(userRepository.getReferenceById(USER_WITHOUT_QUESTPOOLS.getSub())).thenReturn(USER_WITHOUT_QUESTPOOLS);
    }

    @Test
    public void getAllQuestpools_UserExists_QuestpoolsNotNull() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITHOUT_QUESTPOOLS.getSub());

        assertNotNull(questpoolSet);
    }

    @Test
    public void getAllQuestpools_UserWithoutQPs_OnlyStandardQPs() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITHOUT_QUESTPOOLS.getSub());

        assertEquals(STANDARD.getQuestpools(), questpoolSet);
    }

    @Test
    public void getAllQuestpools_UserExists_CorrectAmountOfQPs() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITH_QUESTPOOLS.getSub());

        assertEquals(AMOUNT_OF_STD_QUESTPOOLS + AMOUNT_OF_USR_QUESTPOOLS, questpoolSet.size());
    }

    @Test
    public void getAllQuestpools_UserExists_ReturnsStandardQPs() {
        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER_WITH_QUESTPOOLS.getSub());

        assertTrue(questpoolSet.containsAll(STANDARD.getQuestpools()));
    }
}
