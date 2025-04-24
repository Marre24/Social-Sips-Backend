package com.pvt.SocialSips.questpool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class QuestpoolServiceTest {

    private QuestpoolService questpoolService;

    @Autowired
    public QuestpoolServiceTest(QuestpoolService questpoolService) {
        this.questpoolService = questpoolService;
    }

    @Test
    public void getByUserId_UserDontHaveQuestpools_EmptyList() {
        assertEquals(questpoolService.getByUserId(22L), new ArrayList<Questpool>());
    }

}
