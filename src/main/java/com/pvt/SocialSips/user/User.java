package com.pvt.SocialSips.user;

import com.pvt.SocialSips.event.Event;
import com.pvt.SocialSips.questpool.Questpool;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    private String sub;
    private String firstName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sub", referencedColumnName = "host_sub")
    private Event event;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hostId")
    private final Set<Questpool> questpools = new HashSet<>();

    public User() {
    }

    public User(String firstName, String sub) {
        this.sub = sub;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void addQuestpool(Questpool qp) {
        questpools.add(qp);
    }

    public Set<Questpool> getQuestpools() {
        return questpools;
    }

    public void setEvent(Integer groupSize, Set<Questpool> questpools) {
        this.event = new Event(groupSize, questpools, getSub());
    }

    public Event getEvent() {
        return event;
    }

    public void removeEvent() {
        event = null;
    }

    public void removeQuestpool(Questpool qp) {
        questpools.remove(qp);
    }
}
