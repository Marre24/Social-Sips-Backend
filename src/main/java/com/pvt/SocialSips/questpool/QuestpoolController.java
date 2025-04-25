package com.pvt.SocialSips.questpool;


import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    @Autowired
    private final QuestService questService;

    public QuestpoolController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping("/{userId}")
    public Long getByUserId(@PathVariable Long userId) {
        return userId;
    }

    @PostMapping("/{userId}")
    public HttpStatus addQuestpool(@PathVariable Long userId) {
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/")
    public void createQuest(@RequestBody Quest quest, @RequestParam String type){
        questService.createQuest(quest);
    }

    @PatchMapping("/edit/{id}")
    public void editQuest(@RequestBody Quest quest, @PathVariable Long id){

        System.out.println(quest);
        questService.editQuest(quest, id);
    }
}
