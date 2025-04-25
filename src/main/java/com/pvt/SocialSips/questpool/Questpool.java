package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;

@Data
@Entity
public class Questpool {

    @Id
    @GeneratedValue
    private Long id;
    private String category;

    @OneToMany
    @JoinColumn(name = "qpId")
    HashSet<Quest> quests;
}
