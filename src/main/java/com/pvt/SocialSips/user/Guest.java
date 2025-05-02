package com.pvt.SocialSips.user;

import com.pvt.SocialSips.event.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.mapping.PrimaryKey;

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
