package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    public Questpool getByQuestpoolId(Long qpId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(qpId);
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
    public void updateQuestpool(Set<Quest> quests, Long qpId) {
        Questpool qp = getByQuestpoolId(qpId);
        questRepository.deleteAll(qp.getQuests());
        qp.getQuests().clear();

        qp.quests.addAll(quests);
        questRepository.saveAll(qp.getQuests());
        questpoolRepository.save(qp);
    }

}
