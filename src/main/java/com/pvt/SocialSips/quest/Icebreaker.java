package com.pvt.SocialSips.quest;

import jakarta.persistence.Entity;

@Entity
public class Icebreaker extends Quest{

    public Icebreaker() {

    }

    public Icebreaker(String prompt){
        super(prompt);
    }

}
