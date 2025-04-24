package com.pvt.SocialSips.quest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class QuestService {

    @Autowired
    private final QuestRepository repo;

    public QuestService(QuestRepository repo) {
        this.repo = repo;
    }

    public Quest getQuest(Long id) {
        Optional<Quest> optionalQuest = repo.findById(id);
        if(optionalQuest.isPresent()){
            return optionalQuest.get();
        }
        else throw new IllegalArgumentException("No such quest exists!");
    }

    public void createQuest(Quest q){
        //checka??

        repo.save(q);
    }
}
