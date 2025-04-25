package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import jakarta.persistence.*;

import java.util.HashSet;

@Entity
public class Questpool {

    @Id
    @GeneratedValue
    private Long id;
    private String category;

    @OneToMany
    @JoinColumn(name = "qpId")
    HashSet<Quest> quests;

    public Questpool(){

    }

    public Questpool(Long id, String category, HashSet<Quest> quests) {
        this.id = id;
        this.category = category;
        this.quests = quests;
    }

    public Long getId() {
        return  id;
    }
}
