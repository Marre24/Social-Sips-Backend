package com.pvt.SocialSips.questpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestpoolService {

    private QuestpoolRepository questpoolRepository;

    @Autowired
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
        if (questpoolRepository.findById(qp.getId()).isPresent())
            throw new IllegalArgumentException("Questpool already exists");

        Questpool result = questpoolRepository.save(qp);
    }
}
