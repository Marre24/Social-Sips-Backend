package com.pvt.SocialSips.questpool;


import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.user.Host;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "https://group-2-75.pvt.dsv.su.se/")
@RequestMapping("/questpool")
public class QuestpoolController {

    private final QuestpoolService questpoolService;

    @Autowired
    public QuestpoolController( QuestpoolService questpoolService) {
        this.questpoolService = questpoolService;
    }

    @GetMapping("/{qpId}")
    public ResponseEntity<?> getByQuestpoolId(@PathVariable Long qpId) {
        try{
            Questpool questpool = questpoolService.getByQuestpoolId(qpId);
            return new ResponseEntity<>(questpool, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{qpId}")
    public ResponseEntity<String> deleteByQuestpoolId(@PathVariable Long qpId){
        try{
            questpoolService.deleteQuestpoolById(qpId);
            return new ResponseEntity<>("Questpool was deleted!", HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/")
    public ResponseEntity<Questpool> addQuestpool(@RequestBody Questpool questpool,
                                                  @AuthenticationPrincipal DefaultOidcUser defaultOidcUser) {
        questpoolService.createQuestpoolWithHost(questpool, defaultOidcUser.getSubject());
        return new ResponseEntity<>(questpool, HttpStatus.OK);
    }

    @PatchMapping("/{qpId}")
    public ResponseEntity<String> updateQuestpool(@RequestBody Set<Quest> quests, @PathVariable Long qpId) {
        try{
            questpoolService.updateQuestpool(quests, qpId);
            return new ResponseEntity<>("Questpool has been updated!", HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllQuestpools(){
        List<Questpool> questpools = questpoolService.getAllQuestpools();
        if(questpools.isEmpty()) return new ResponseEntity<>("No questpools exists in the database.",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(questpools, HttpStatus.OK);
    }

}
