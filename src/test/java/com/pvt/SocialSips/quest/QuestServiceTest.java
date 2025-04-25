package com.pvt.SocialSips.quest;

import com.pvt.SocialSips.SocialSipsApplication;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SocialSipsApplication.class)
public class QuestServiceTest {

    private final Quest QUEST = new Quest(1L, "sawe");

    @Mock
    private QuestRepository repository;

    @InjectMocks
    private QuestService service;

    @Test
    public void testGetQuest(){
        Quest quest = new Quest( 1L,"Olivpaj");
        when(repository.findById(1L)).thenReturn(Optional.of(QUEST));
        assertEquals(service.getQuest(1L).getPrompt(), "hejs");

    }





}
