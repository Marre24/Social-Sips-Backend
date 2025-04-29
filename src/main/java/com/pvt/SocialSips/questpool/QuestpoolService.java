package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class QuestpoolService {

    @Autowired
    private final QuestRepository questRepository;

    @Autowired
    private final QuestpoolRepository questpoolRepository;


    public QuestpoolService(QuestRepository questRepository, QuestpoolRepository questpoolRepository) {
        this.questpoolRepository = questpoolRepository;
        this.questRepository = questRepository;
    }

    public Questpool getByQuestpoolId(Long qpId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(qpId);
        return questpoolOptional.orElseThrow(() -> new EntityNotFoundException("No such questpool exists!"));
    }

    public void deleteQuestpoolById(Long qpId) {
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

        qp.getQuests().addAll(quests);
        questRepository.saveAll(qp.getQuests());
        questpoolRepository.save(qp);
    }

}
