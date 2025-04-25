package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.QuestRepository;
import com.pvt.SocialSips.quest.QuestService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class QuestpoolServiceTest {

    private static final Long NON_EXISTING_USER = -1L;
    private static final Long USER_ID_WITH_NO_POOL = 0L;
    private static final Long USER_ID_WITH_ONE_POOL = 1L;

    @Mock
    private QuestpoolRepository questpoolRepository;

    @InjectMocks
    private QuestpoolService questpoolService;

    @Test
    public void getByUserId_IdDoesNotExist_ThrowsIllegalStateException() {
        when(questpoolRepository.findById(NON_EXISTING_USER)).thenThrow(IllegalStateException.class);

        assertThrows(IllegalStateException.class, () -> questpoolService.getByUserId(NON_EXISTING_USER))                                                                       ;
    }

    @Test
    public void getByUserId_UserDontHaveQuestpools_EmptyList() {
        when(questpoolRepository.findById(USER_ID_WITH_NO_POOL)).thenReturn(Optional.empty());
        assertTrue(questpoolService.getByUserId(USER_ID_WITH_NO_POOL).isEmpty());
    }

    @Test
    public void getByUserId_UserHasOneQuestpool_ListSizeOne(){
        when(questpoolRepository.findById(USER_ID_WITH_ONE_POOL)).thenReturn(Optional.of(new Questpool()));
        assertEquals(1, questpoolService.getByUserId(USER_ID_WITH_ONE_POOL).size());
    }
}
