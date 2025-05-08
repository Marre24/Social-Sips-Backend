package com.pvt.SocialSips.event;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.user.User;
import jakarta.persistence.*;
import org.sqids.Sqids;

import java.util.List;
import java.util.Set;

@Entity
public class Event {

    public static final Sqids SQID = Sqids.builder().minLength(5).alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ").build();

    @Id
    @Column(name = "host_sub")
    private String hostSub;
    private Boolean started = false;
    private String joinCode;

    private String name;
    private Integer groupSize;

    @OneToMany
    private Set<Questpool> questpools;

    public Event() {
    }

    public Event(String name, Integer groupSize, Set<Questpool> questpools, String hostSub) {
        joinCode = generateJoinCode(hostSub);
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
        this.joinCode = generateJoinCode(hostSub);
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

    private String generateJoinCode(String hostId){
        return SQID.encode(List.of(Long.valueOf(hostId)));
    }
}