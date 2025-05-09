package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final static User USER = new User("firstName", "ThisIsASub");

    @Test
    public void getAllQuestpools_UserExists_QuestpoolsNotNull() {
        when(userRepository.getReferenceById(USER.getSub())).thenReturn(USER);

        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(USER.getSub());

        assertNotNull(questpoolSet);
    }

    @Test
    public void getAllQuestpools_UserExists_CorrectAmountOfQPs() {
        User userWithQuestpools = new User("name", "anotherSub");
        userWithQuestpools.addQuestpool(new Questpool("one", QuestpoolType.ICEBREAKER, new HashSet<>()));
        userWithQuestpools.addQuestpool(new Questpool("two", QuestpoolType.ICEBREAKER, new HashSet<>()));
        userWithQuestpools.addQuestpool(new Questpool("three", QuestpoolType.ICEBREAKER, new HashSet<>()));
        when(userRepository.getReferenceById(userWithQuestpools.getSub())).thenReturn(userWithQuestpools);

        Set<Questpool> questpoolSet = userService.getAllQuestpoolsBySub(userWithQuestpools.getSub());

        assertEquals(3, questpoolSet.size());
    }
}
