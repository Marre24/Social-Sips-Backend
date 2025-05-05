package com.pvt.SocialSips.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Guest {

    @Id
    private String deviceId;
    
    public Guest(){}

    public Guest(String deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
