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
    private final Long hostId;
    private Boolean started = false;
    private final String name;
    private final Integer groupSize;

    @OneToMany
    @JoinColumn(name = "eventId")
    private final Set<Questpool> questpools;



    public Event(Long hostId, String name, Integer groupSize, Set<Questpool> questpools) {
        this.hostId = hostId;
        this.name = name;
        this.groupSize = groupSize;
        this.questpools = questpools;
    }


    public Long getHostId() {
        return hostId;
    }

    public Boolean hasStarted() {
        return started;
    }

    public void start() {
        started = true;
    }

}
