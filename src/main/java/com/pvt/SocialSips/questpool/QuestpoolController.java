package com.pvt.SocialSips.questpool;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    private final QuestpoolService questpoolService;


    @Autowired
    public QuestpoolController( QuestpoolService questpoolService) {
        this.questpoolService = questpoolService;
    }

    @GetMapping("/{qpId}")
    public Questpool getByUserId(@PathVariable Long qpId) {
        return questpoolService.getByQuestpoolId(qpId);
    }

    @PostMapping("/")
    public ResponseEntity<Questpool> addQuestpool(@RequestBody Questpool questpool) {
        questpoolService.createQuestpool(questpool);

        return ResponseEntity.ok(questpool);
    }

    @PutMapping("/{id}")
    public void updateQuestpool(@RequestBody Questpool questpool, @PathVariable Long id) {
        questpoolService.updateQuestpool(questpool, id);
    }



}
