package com.pvt.SocialSips.quest;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.id.IncrementGenerator;

@Data
@Entity
@Table
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long questId;

    private String prompt;
    private String type;
}
