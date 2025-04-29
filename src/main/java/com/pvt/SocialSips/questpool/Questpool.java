package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Questpool {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "qpId")
    private Set<Quest> quests;

    public Questpool() {

    }

    public Questpool(Long id, String name, Set<Quest> quests) {
        this.id = id;
        this.name = name;
        this.quests = quests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Quest> getQuests() {
        return quests;
    }

    public void setQuests(Set<Quest> quests) {
        this.quests = quests;
    }
}
