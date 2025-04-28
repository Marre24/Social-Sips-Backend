package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.QuestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestpoolService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestpoolRepository questpoolRepository;


    public QuestpoolService(QuestRepository questRepository, QuestpoolRepository questpoolRepository) {
        this.questpoolRepository = questpoolRepository;
        this.questRepository = questRepository;
    }

    public Questpool getByQuestpoolId(Long userId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(userId);
        return questpoolOptional.orElseThrow(() -> new IllegalStateException("No such questpool exists!"));
    }

    public void deleteQuestpoolById(Long qpId){
        Questpool qp = getByQuestpoolId(qpId);
        questpoolRepository.deleteById(qpId);
    }


    public void createQuestpool(Questpool qp) {
        questRepository.saveAll(qp.getQuests());

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

    public void deleteQuestpool(Questpool qp) {
        getByQuestpoolId(qp.getId());
        questpoolRepository.delete(qp);
    }
}
