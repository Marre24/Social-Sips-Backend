package com.pvt.SocialSips.quest;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.id.IncrementGenerator;

@Data
@Entity
@Table
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String prompt;
    private String type;

    public Quest(String prompt, String type){
        this.prompt = prompt;
        this.type = type;
    }
}
