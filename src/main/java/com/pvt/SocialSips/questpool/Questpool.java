package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Questpool {

    @Id
    @GeneratedValue
    private Long id;
    private String category;
    @OneToMany
    @JoinColumn(name = "qpId")
    Set<Quest> quests;

    public Questpool(){

    }

    public Questpool(Long id, String category, HashSet<Quest> quests) {
        this.id = id;
        this.category = category;
        this.quests = quests;
    }

    public Questpool(String category, Set<Quest> quests) {
        this.category = category;
        this.quests = quests;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(HashSet<Quest> quests) {
        this.quests = quests;
    }
}
