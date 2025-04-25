package com.pvt.SocialSips.questpool;


import com.pvt.SocialSips.quest.Quest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    private final QuestpoolService questpoolService;


    @Autowired
    public QuestpoolController( QuestpoolService questpoolService) {
        this.questpoolService = questpoolService;
    }

    @GetMapping("/{userId}")
    public Long getByUserId(@PathVariable Long userId) {
        return userId;
    }

    @PostMapping("/")
    public Questpool addQuestpool(@RequestBody Questpool questpool) {
        questpoolService.createQuestpool(questpool);

        return questpool;
    }

    @PutMapping("/{id}")
    public void updateQuestpool(@RequestBody Questpool questpool, @PathVariable Long id) {
        questpoolService.updateQuestpool(questpool, id);
    }



}
