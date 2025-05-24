package com.pvt.SocialSips.config;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Trivia;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolParser;
import com.pvt.SocialSips.questpool.QuestpoolService;
import com.pvt.SocialSips.questpool.QuestpoolType;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
public class QuestpoolConfig {

    @Bean
    CommandLineRunner questpoolCommandLineRunner(QuestpoolService questpoolService, UserService userService) {
        return args -> {
            try {
                User oldStandard = userService.getUserBySub("STANDARD");
                userService.deleteUser(oldStandard);
            } catch (EntityNotFoundException e){}

            User standard = new User("STANDARD", "STANDARD");
            var questpools = QuestpoolParser.getAllStandardQuestpools();
            userService.register(standard);
            
            for (var questpool : questpools)
                questpoolService.createQuestpoolWithHost(questpool, standard.getSub());
        };
    }


}

