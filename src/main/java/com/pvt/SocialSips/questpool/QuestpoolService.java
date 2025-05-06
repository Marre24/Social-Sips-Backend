package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestRepository;
import com.pvt.SocialSips.user.Host;
import com.pvt.SocialSips.user.HostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuestpoolService {

    private final QuestRepository questRepository;

    private final QuestpoolRepository questpoolRepository;

    private final HostRepository hostRepository;

    @Autowired
    public QuestpoolService(QuestRepository questRepository, QuestpoolRepository questpoolRepository, HostRepository hostRepository) {
        this.questpoolRepository = questpoolRepository;
        this.questRepository = questRepository;
        this.hostRepository = hostRepository;
    }

    public Questpool getByQuestpoolId(Long qpId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(qpId);
        return questpoolOptional.orElseThrow(() -> new EntityNotFoundException("No such questpool exists!"));
    }

    public void deleteQuestpoolById(Long qpId){
        Questpool qp = getByQuestpoolId(qpId);
        questpoolRepository.deleteById(qpId);
    }

    public void createQuestpoolWithHost(Questpool qp) {
        createQuestpoolWithHost(qp, qp.getHost());
    }

    public void createQuestpoolWithHost(Questpool qp, Host host) {
        host.addQuestpool(qp);

        hostRepository.save(host);
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

    public List<Questpool> getAllQuestpools() {
        return questpoolRepository.findAll();
    }
}
