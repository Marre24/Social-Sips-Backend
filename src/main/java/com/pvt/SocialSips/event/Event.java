package com.pvt.SocialSips.event;

import com.pvt.SocialSips.questpool.Questpool;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Event {

    @Id
    private Long hostId;
    private Boolean started = false;


    private String name;
    private Integer groupSize;

    @OneToMany
    @JoinColumn(name = "eventId")
    private Set<Questpool> questpools;


    public Event() {
    }

    public Event(Long hostId, String name, Integer groupSize, Set<Questpool> questpools) {
        this.hostId = hostId;
        this.name = name;
        this.groupSize = groupSize;
        this.questpools = questpools;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    public Set<Questpool> getQuestpools() {
        return questpools;
    }

    public void setQuestpools(Set<Questpool> questpools) {
        this.questpools = questpools;
    }

    public Long getHostId() {
        return hostId;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted() {
        started = true;
    }

}