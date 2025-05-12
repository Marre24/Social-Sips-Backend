package com.pvt.SocialSips.quest;


import jakarta.persistence.Entity;

import java.util.Set;

@Entity
public class Trivia extends Quest {

    // Semicolon separated, first option is always the correct one
    private String answers;

    public Trivia() {

    }

    public Trivia(String prompt, String answers) {
        super(prompt);
        this.answers = answers;
    }

    public Trivia(Long id, String prompt, String answers) {
        super(id, prompt);
        this.answers = answers;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
