package com.pvt.SocialSips.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Guest {

    @Id
    private String uuid;

    public Guest() {

    }

    public Guest(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
