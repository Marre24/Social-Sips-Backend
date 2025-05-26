package com.pvt.SocialSips.config;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Trivia;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolParser;
import com.pvt.SocialSips.questpool.QuestpoolService;
import com.pvt.SocialSips.questpool.QuestpoolType;
import com.pvt.SocialSips.user.User;
import com.pvt.SocialSips.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
public class QuestpoolConfig {

//    @Bean
//    CommandLineRunner questpoolCommandLineRunner(QuestpoolService questpoolService, UserService userService) {
//        return args -> {
//            User standard = new User("STANDARD", "STANDARD");
//
//            var questpools = QuestpoolParser.getAllStandardQuestpools();
//
//            userService.deleteUser(standard);
//            userService.register(standard);
//
//            for (var questpool : questpools)
//                questpoolService.createQuestpoolWithHost(questpool, standard.getSub());
//        };
//    }


}

