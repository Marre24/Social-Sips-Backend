package com.pvt.SocialSips.quest;


import java.util.Arrays;

public class Trivia extends Quest {

    private String[] options;
    private int correctOption;

    public Trivia(Long id, String prompt, String[] options, int correctOption){
        super(id, prompt);
        this.correctOption = correctOption;
        this.options = options;
    }

    public void setOptions(String[] options){;
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

    public void setCorrectOption(int correctOption){
        this.correctOption = correctOption;
    }

    public String getCorrectOption() {
        return options[correctOption];
    }


}
