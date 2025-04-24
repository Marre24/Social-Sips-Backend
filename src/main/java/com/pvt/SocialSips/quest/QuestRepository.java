package com.pvt.SocialSips.quest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestRepository extends JpaRepository<Quest, Long> {

    Optional<Quest> findById(Long id);
    void deleteById(Long id);
}
