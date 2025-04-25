package com.pvt.SocialSips.questpool;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestpoolService {

    @Autowired
    private QuestpoolRepository questpoolRepository;


    public QuestpoolService(QuestpoolRepository questpoolRepository) {
        this.questpoolRepository = questpoolRepository;
    }

    public Questpool getByQuestpoolId(Long userId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(userId);
        if (questpoolOptional.isEmpty())
            throw new IllegalStateException("No such questpool exists!");

        return questpoolOptional.get();
    }


    public void createQuestpool(Questpool qp) {
        questpoolRepository.save(qp);
    }

    @Transactional
    public void updateQuestpool(Questpool questpool, Long id) {
        Optional<Questpool> optionalQuestpool = questpoolRepository.findById(id);

        if(optionalQuestpool.isPresent()) {
            questpoolRepository.delete(optionalQuestpool.get());

            questpool.setId(id);
            questpoolRepository.save(questpool);

        } else {
            throw new IllegalStateException("Questpool with id: " + id + " does not exist!");
        }


    }
}
