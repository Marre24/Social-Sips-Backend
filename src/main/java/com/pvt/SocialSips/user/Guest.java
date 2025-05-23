package com.pvt.SocialSips.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Guest guest) {
            return guest.getUuid().equals(this.uuid);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
