package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestRepository;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QuestpoolServiceTest {

    private static final Long NON_EXISTING_QUESTPOOL_ID = -1L;
    private static final Long QUESTPOOL_ID = 1L;

    private static final String CATEGORY = "TRIVIA";

    private static final Icebreaker q1 = new Icebreaker("Prompt One");
    private static final Icebreaker q2 = new Icebreaker("Prompt Two");

    private static final HashSet<Quest> quests = new HashSet<>(List.of(q1, q2));

    private static final Questpool VALID_QUESTPOOL = new Questpool(QUESTPOOL_ID, CATEGORY, QuestpoolType.ICEBREAKER, quests);

    private static final String SUB = "31987";

    private static final User USER = new User("this is a name", SUB);

    @Mock
    private QuestpoolRepository questpoolRepository;

    @Mock
    private QuestRepository questRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private QuestpoolService questpoolService;

    @BeforeAll
    public static void setup() {
        USER.getQuestpools().add(VALID_QUESTPOOL);
    }


    @Test
    public void getByQuestpoolId_IdDoesNotExist_ThrowsIllegalStateException() {
        when(questpoolRepository.findById(NON_EXISTING_QUESTPOOL_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> questpoolService.getByQuestpoolId(NON_EXISTING_QUESTPOOL_ID));
    }

    @Test
    public void getByQuestpoolId_QuestpoolIdExists_AQuestpool() {
        Questpool expected = new Questpool();
        when(questpoolRepository.findById(QUESTPOOL_ID)).thenReturn(Optional.of(expected));

        assertEquals(expected, questpoolService.getByQuestpoolId(QUESTPOOL_ID));
    }

    @Test
    public void createQuestpool_ValidArgs_QuestpoolAdded() {
        when(userService.getUserBySub(SUB)).thenReturn(USER);

        assertDoesNotThrow(() -> questpoolService.createQuestpoolWithHost(VALID_QUESTPOOL, USER.getSub()));
    }


    @Test
    public void deleteQuestpool_ExistingQuestpool_QuestpoolRemoved() {
        when(userService.getUserBySub(SUB)).thenReturn(USER);
        when(questpoolRepository.findById(VALID_QUESTPOOL.getId())).thenReturn(Optional.of(VALID_QUESTPOOL));

        assertDoesNotThrow(() -> questpoolService.deleteQuestpoolById(VALID_QUESTPOOL.getId(), USER.getSub()));
    }

    @Test
    public void deleteQuestpool_NonExistingQuestpool_IllegalStatExceptionThrown() {
        when(questpoolRepository.findById(VALID_QUESTPOOL.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> questpoolService.deleteQuestpoolById(VALID_QUESTPOOL.getId(), USER.getSub()));
    }


}
