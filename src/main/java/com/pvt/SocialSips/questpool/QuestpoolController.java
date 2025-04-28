package com.pvt.SocialSips.questpool;


import com.pvt.SocialSips.quest.Quest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    private final QuestpoolService questpoolService;


    @Autowired
    public QuestpoolController( QuestpoolService questpoolService) {
        this.questpoolService = questpoolService;
    }

    @GetMapping("/{qpId}")
    public ResponseEntity<Questpool> getByQuestpoolId(@PathVariable Long qpId) {
        try{
            return ResponseEntity.ok(questpoolService.getByQuestpoolId(qpId));
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{qpId}")
    public ResponseEntity<String> deleteByQuestpoolId(@PathVariable Long qpId){
        try{
            questpoolService.deleteQuestpoolById(qpId);
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such Questpool exists!");
        }
        return ResponseEntity.ok("Questpool was deleted!");
    }

    @PostMapping("/")
    public ResponseEntity<Questpool> addQuestpool(@RequestBody Questpool questpool) {
        questpoolService.createQuestpool(questpool);
        return ResponseEntity.ok(questpool);
    }

    @PatchMapping("/{qpId}")
    public void updateQuestpool(@RequestBody Set<Quest> quests, @PathVariable Long qpId) {
        questpoolService.updateQuestpool(quests, qpId);
    }



}
