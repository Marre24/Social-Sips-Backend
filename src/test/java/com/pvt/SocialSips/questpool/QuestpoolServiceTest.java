package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestRepository;
import com.pvt.SocialSips.user.Host;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QuestpoolServiceTest {

    private static final Long NON_EXISTING_QUESTPOOL_ID = -1L;
    private static final Long QUESTPOOL_ID = 1L;

    private static final String CATEGORY = "Category";

    private static final Icebreaker q1 = new Icebreaker("Prompt One");
    private static final Icebreaker q2 = new Icebreaker("Prompt Two");

    private static final HashSet<Quest> quests = new HashSet<>(List.of(q1, q2));

    private static final Host HOST = new Host();

    @Mock
    private QuestpoolRepository questpoolRepository;

    @Mock
    private QuestRepository questRepository;

    @InjectMocks
    private QuestpoolService questpoolService;

    @Test
    public void getByQuestpoolId_IdDoesNotExist_ThrowsIllegalStateException() {
        when(questpoolRepository.findById(NON_EXISTING_QUESTPOOL_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> questpoolService.getByQuestpoolId(NON_EXISTING_QUESTPOOL_ID));
    }

    @Test
    public void getByQuestpoolId_QuestpoolIdExists_AQuestpool(){
        Questpool expected = new Questpool();
        when(questpoolRepository.findById(QUESTPOOL_ID)).thenReturn(Optional.of(expected));

        assertEquals(expected, questpoolService.getByQuestpoolId(QUESTPOOL_ID));
    }

    @Test
    public void createQuestpool_ValidArgs_QuestpoolAdded(){
        Questpool qp = new Questpool(QUESTPOOL_ID, CATEGORY, QuestpoolType.ICEBREAKER, quests, HOST);

        when(questpoolRepository.save(qp)).thenReturn(qp);

        assertDoesNotThrow(() -> questpoolService.createQuestpoolWithHost(qp, HOST.getSub()));
    }


    @Test
    public void deleteQuestpool_ExistingQuestpool_QuestpoolRemoved(){
        Questpool qp = new Questpool(QUESTPOOL_ID, CATEGORY, QuestpoolType.ICEBREAKER, quests, HOST);
        when(questpoolRepository.findById(qp.getId())).thenReturn(Optional.of(qp));
        assertDoesNotThrow(() -> questpoolService.deleteQuestpoolById(qp.getId()));
    }

    @Test
    public void deleteQuestpool_NonExistingQuestpool_IllegalStatExceptionThrown(){
        Questpool qp = new Questpool(QUESTPOOL_ID, CATEGORY, QuestpoolType.ICEBREAKER, quests, HOST);
        when(questpoolRepository.findById(qp.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,() -> questpoolService.deleteQuestpoolById(qp.getId()));
    }


}
