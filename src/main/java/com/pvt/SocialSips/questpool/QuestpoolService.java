package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.QuestRepository;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserRepository;
import com.pvt.SocialSips.user.UserService;
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

    private final UserService userService;

    @Autowired
    public QuestpoolService(QuestRepository questRepository, QuestpoolRepository questpoolRepository, UserService userService) {
        this.questpoolRepository = questpoolRepository;
        this.questRepository = questRepository;
        this.userService = userService;
    }

    public Questpool getByQuestpoolId(Long qpId) {
        Optional<Questpool> questpoolOptional = questpoolRepository.findById(qpId);
        return questpoolOptional.orElseThrow(() -> new EntityNotFoundException("No such questpool exists!"));
    }

    @Transactional
    public void deleteQuestpoolById(Long qpId, String sub){
        User user = userService.getUserBySub(sub);
        Questpool qp = getByQuestpoolId(qpId);

        if (!user.getQuestpools().contains(qp))
            throw new IllegalCallerException("Tried to delete a questpool that is not owned by: " + user.getFirstName());

        userService.removeQuestpoolFrom(user, qp);
    }


    @Transactional
    public void createQuestpoolWithHost(Questpool qp, String sub) {
        User user = userService.getUserBySub(sub);

        user.addQuestpool(qp);

        userService.register(user);
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
