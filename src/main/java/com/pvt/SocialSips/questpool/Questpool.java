package com.pvt.SocialSips.questpool;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Questpool {

    @Id
    @GeneratedValue
    private Long id;
    private String category;



}
