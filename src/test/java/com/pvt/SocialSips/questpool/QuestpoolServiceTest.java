package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QuestpoolServiceTest {

    private static final Long NON_EXISTING_QUESTPOOL = -1L;
    private static final Long QUESTPOOL_ID = 1L;

    @Mock
    private QuestpoolRepository questpoolRepository;

    @InjectMocks
    private QuestpoolService questpoolService;

    @Test
    public void getByQuestpoolId_IdDoesNotExist_ThrowsIllegalStateException() {
        when(questpoolRepository.findById(NON_EXISTING_QUESTPOOL)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> questpoolService.getByQuestpoolId(NON_EXISTING_QUESTPOOL));
    }

    @Test
    public void getByQuestpoolId_QuestpoolIdExists_AQuestpool(){
        Questpool expected = new Questpool();
        when(questpoolRepository.findById(QUESTPOOL_ID)).thenReturn(Optional.of(expected));

        assertEquals(expected, questpoolService.getByQuestpoolId(QUESTPOOL_ID));
    }
}
