package com.pvt.SocialSips.quest;


public class Trivia extends Quest {

    private String[] options;
    private final int correctOption;

    public Trivia(Long id, String prompt, String[] options, int correctOption){
        super(id, prompt);
        this.correctOption = correctOption;
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return options[correctOption];
    }


}
