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

    private static final String PROMPT = "prompt";
    private static final Long ID = 1L;

    private final Quest QUEST = new Quest(ID, PROMPT);

    @Mock
    private QuestRepository repository;

    @InjectMocks
    private QuestService service;

    @Test
    public void testGetQuest(){
        when(repository.findById(ID)).thenReturn(Optional.of(QUEST));

        assertEquals(service.getQuest(ID).getPrompt(), PROMPT);
    }





}
