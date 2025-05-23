package com.pvt.SocialSips.event;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.user.Guest;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {


    @Id
    @Column(name = "host_sub")
    private String hostSub;
    private Boolean started = false;
    private String joinCode;

    private String name;
    private Integer groupSize;

    @OneToMany
    private Set<Questpool> questpools;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Guest> guests = new HashSet<>();

    public Event() {
    }

    public Event(String name, Integer groupSize, Set<Questpool> questpools, String hostSub) {
        joinCode = JoinCodeGenerator.generateLetterCode(hostSub);
        this.hostSub = hostSub;
        this.name = name;
        this.groupSize = groupSize;
        this.questpools = questpools;
    }

    public String getHostSub() {
        return hostSub;
    }

    public void setHostSub(String hostSub) {
        this.hostSub = hostSub;
        joinCode = JoinCodeGenerator.generateLetterCode(hostSub);
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
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

    public void addGuest(Guest g) {
        guests.add(g);
    }

    public void setGuests(Set<Guest> guests) {
        this.guests = guests;
    }

    public boolean containsGuest(Guest g) {
        return guests.contains(g);
    }

    public Set<Guest> getGuests() {
        return guests;
    }
}