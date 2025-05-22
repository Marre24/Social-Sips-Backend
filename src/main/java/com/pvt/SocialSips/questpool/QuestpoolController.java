package com.pvt.SocialSips.questpool;


import com.pvt.SocialSips.quest.Quest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.pvt.SocialSips.util.JwtParser.extractSub;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    private final QuestpoolService questpoolService;

    @Autowired
    public QuestpoolController(QuestpoolService questpoolService) {
        this.questpoolService = questpoolService;
    }

    @DeleteMapping("/{qpId}")
    public ResponseEntity<String> deleteByQuestpoolId(@PathVariable Long qpId) {
        try {
            questpoolService.deleteQuestpoolById(qpId, extractSub());
            return new ResponseEntity<>("Questpool was deleted!", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalCallerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping
    public ResponseEntity<Questpool> addQuestpool(@RequestBody Questpool questpool) {
        questpoolService.createQuestpoolWithHost(questpool, extractSub());
        return new ResponseEntity<>(questpool, HttpStatus.OK);
    }

    @PatchMapping("/{qpId}")
    public ResponseEntity<String> updateQuestpool(@RequestBody Set<Quest> quests, @PathVariable Long qpId) {
        try {
            questpoolService.updateQuestpool(quests, qpId, extractSub());
            return new ResponseEntity<>("Questpool has been updated!", HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalCallerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/standard")
    public ResponseEntity<?> getAllStandardQuestpools(){
        try {
            Set<Questpool> questpools = questpoolService.getAllStandardQuestpools();
            return new ResponseEntity<>(questpools, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
