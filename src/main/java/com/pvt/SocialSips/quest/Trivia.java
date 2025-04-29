package com.pvt.SocialSips.quest;


import jakarta.persistence.Entity;

import java.util.Set;

@Entity
public class Trivia extends Quest {

    private Set<String> options;
    private Integer correctOption;

    public Trivia() {

    }

    public Trivia(Long id, String prompt, Set<String> options, int correctOption){
        super(id, prompt);
        this.correctOption = correctOption;
        this.options = options;
    }

    public void setOptions(Set<String> options){;
        this.options = options;
    }

    public Set<String> getOptions() {
        return options;
    }

    public void setCorrectOption(int correctOption){
        this.correctOption = correctOption;
    }

    public Integer getCorrectOption() {
        return correctOption;
    }


}
