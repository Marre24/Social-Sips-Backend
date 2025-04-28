package com.pvt.SocialSips.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    public User(){

    }

    public User(Long id, String email){
        this.id = id;
        this.email = email;
    }

    public @Email String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
