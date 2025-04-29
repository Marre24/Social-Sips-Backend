package com.pvt.SocialSips.event;

import com.pvt.SocialSips.questpool.Questpool;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import org.sqids.Sqids;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Event {

    public static final Sqids SQID = Sqids.builder().minLength(5).alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ").build();

    @Id
    private Long hostId;
    private Boolean started = false;
    private String joinCode;
    private String name;
    private Integer groupSize;

    @OneToMany
    @JoinColumn(name = "eventId")
    private Set<Questpool> questpools;

    // TODO: 2025-04-29 add relationship
    private Set<String> guests = new HashSet<>();


    public Event() {
    }

    public Event(Long hostId, String name, Integer groupSize, Set<Questpool> questpools) {
        joinCode = SQID.encode(List.of(hostId));

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

    public String getJoinCode() {
        return joinCode;
    }

    public Set<String> getGuests() {
        return guests;
    }

    public void addGuest(String deviceId) {
        guests.add(deviceId);
    }
}