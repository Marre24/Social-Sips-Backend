package com.pvt.SocialSips.quest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@TestConfiguration
public class QuestServiceTest {

    @Autowired
    private QuestRepository repository;

    private class QuestServiceImpl extends QuestService{
        public QuestServiceImpl() {
            super(repository);
        }
    }


    @Autowired
    private QuestService service;

    @Test
    public void testGetQuest(){
        Quest quest = new Quest( "Olivpaj", "Olivpaj");
        repository.save(quest);
        assertEquals(service.getQuest(1L).getPrompt(), "Olivpaj");

    }



}
