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

    public List<Questpool> getByUserId(Long userId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(userId);
        if (questpoolOptional.isEmpty())
            return new ArrayList<>();

        return List.of(questpoolOptional.get());
    }


}
