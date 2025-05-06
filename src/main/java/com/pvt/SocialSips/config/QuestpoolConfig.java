package com.pvt.SocialSips.config;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.Trivia;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.questpool.QuestpoolRepository;
import com.pvt.SocialSips.questpool.QuestpoolType;
import com.pvt.SocialSips.user.Host;
import com.pvt.SocialSips.user.HostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
public class QuestpoolConfig {

    @Bean
    CommandLineRunner questpoolCommandLineRunner(QuestpoolRepository questpoolRepository, HostRepository hostRepository) {
        return args -> {
            Host standard = new Host("STANDARD", "STANDARD");

            Questpool icebreakerOne = new Questpool(
                    "icebreakerOne",
                    QuestpoolType.ICEBREAKER,
                    new HashSet<>(List.of(
                            new Icebreaker("Ask about interests"),
                            new Icebreaker("Ask about gaming"))),
                    standard
            );

            Questpool icebreakerTwo = new Questpool(
                    "icebreakerTwo",
                    QuestpoolType.ICEBREAKER,
                    new HashSet<>(List.of(
                            new Icebreaker("Ask about music"),
                            new Icebreaker("Ask about fashion"))),
                    standard
            );

            Questpool triviaOne = new Questpool(
                    "triviaOne",
                    QuestpoolType.TRIVIA,
                    new HashSet<>(List.of(
                            new Trivia("Question one", new HashSet<>(List.of("Correct", "opp2", "opp3", "opp4")), 1)
                    )),
                    standard
            );

            Questpool triviaTwo = new Questpool(
                    "triviaTwo",
                    QuestpoolType.TRIVIA,
                    new HashSet<>(List.of(
                            new Trivia("Question two", new HashSet<>(List.of("opp1", "correct", "opp3", "opp4")), 2)
                    )),
                    standard
            );

            /*Deletes everything including custom questpools*/
            questpoolRepository.deleteAll();

            hostRepository.save(standard);
            questpoolRepository.save(icebreakerOne);
            questpoolRepository.save(icebreakerTwo);
            questpoolRepository.save(triviaOne);
            questpoolRepository.save(triviaTwo);
        };
    }


}
