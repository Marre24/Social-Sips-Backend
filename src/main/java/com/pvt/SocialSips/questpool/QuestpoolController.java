package com.pvt.SocialSips.questpool;


import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    private final QuestService questService;

    private final QuestpoolService questpoolService;


    @Autowired
    public QuestpoolController(QuestService questService, QuestpoolService questpoolService) {
        this.questService = questService;
        this.questpoolService = questpoolService;
    }

    @GetMapping("/{userId}")
    public Long getByUserId(@PathVariable Long userId) {
        return userId;
    }

    @PostMapping("/")
    public Questpool addQuestpool(@RequestBody Questpool questpool) {
       // System.out.println(questpool.getCategory());
        questpoolService.createQuestpool(questpool);

        return questpool;
    }


    @PatchMapping("/edit/{id}")
    public void editQuest(@RequestBody Quest quest, @PathVariable Long id){

        System.out.println(quest);
        questService.editQuest(quest, id);
    }

    @PutMapping("/{id}")
    public void updateQuestpool(@RequestBody Questpool questpool, @PathVariable Long id) {
        questpoolService.updateQuestpool(questpool, id);
    }



}
