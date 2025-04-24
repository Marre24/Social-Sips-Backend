package com.pvt.SocialSips.questpool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestpoolRepository extends JpaRepository<Questpool, Long> {

}
