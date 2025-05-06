package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.user.Host;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Questpool {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private QuestpoolType type;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "qpId")
    private Set<Quest> quests;

    @ManyToOne
    @JoinColumn(name = "hostId")
    private Host host;

    public Questpool() {

    }

    public Questpool(String name, QuestpoolType type, Set<Quest> quests, Host host) {
        this.name = name;
        this.type = type;
        this.quests = quests;
        this.host = host;
    }

    public Questpool(Long id, String name, QuestpoolType type, Set<Quest> quests, Host host) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quests = quests;
        this.host = host;
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

    public QuestpoolType getType() {
        return type;
    }

    public void setType(QuestpoolType type) {
        this.type = type;
    }
}
