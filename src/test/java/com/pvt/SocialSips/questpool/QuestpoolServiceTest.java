package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
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

    private static final String CATEGORY = "Category";

    private static final Quest q1 = new Quest("Prompt One");
    private static final Quest q2 = new Quest("Prompt Two");

    private static final HashSet<Quest> quests = new HashSet<>(List.of(q1, q2));

    @Mock
    private QuestpoolRepository questpoolRepository;

    @InjectMocks
    private QuestpoolService questpoolService;

    @Test
    public void getByQuestpoolId_IdDoesNotExist_ThrowsIllegalStateException() {
        when(questpoolRepository.findById(NON_EXISTING_QUESTPOOL_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> questpoolService.getByQuestpoolId(NON_EXISTING_QUESTPOOL_ID));
    }

    @Test
    public void getByQuestpoolId_QuestpoolIdExists_AQuestpool(){
        Questpool expected = new Questpool();
        when(questpoolRepository.findById(QUESTPOOL_ID)).thenReturn(Optional.of(expected));

        assertEquals(expected, questpoolService.getByQuestpoolId(QUESTPOOL_ID));
    }

    @Test
    public void createQuestpool_ValidArgs_QuestpoolAdded(){
        Questpool qp = new Questpool(QUESTPOOL_ID, CATEGORY, quests);

        when(questpoolRepository.save(qp)).thenReturn(qp);

        assertDoesNotThrow(() -> questpoolService.createQuestpool(qp));
    }


    @Test
    public void deleteQuestpool_ExistingQuestpool_QuestpoolRemoved(){
        Questpool qp = new Questpool(QUESTPOOL_ID, CATEGORY, quests);
        when(questpoolRepository.findById(qp.getId())).thenReturn(Optional.of(qp));
        assertDoesNotThrow(() -> questpoolService.deleteQuestpool(qp));
    }
}
