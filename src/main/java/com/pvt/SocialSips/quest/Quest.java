package com.pvt.SocialSips.quest;

import jakarta.persistence.*;

@Entity
@Table
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prompt;

    public Quest(){

    }

    public Quest(Long id, String prompt){
        this.id = id;
        this.prompt = prompt;
    }

    public Long getId() {
        return id;
    }

    public Quest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPrompt() {
        return prompt;
    }

    public Quest setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }
}
