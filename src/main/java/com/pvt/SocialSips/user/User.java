package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.role.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {


    @Id
    private String sub;
    private String firstName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hostId")
    private Set<Questpool> questpools = new HashSet<>();

    public User(){
    }

    public User(String firstName, String sub){
        this.sub = sub;
        this.firstName = firstName;

    }

    public User(String firstName, String sub, List<Role> roles){
        this.sub = sub;
        this.firstName = firstName;
        this.roles = roles;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void addQuestpool(Questpool qp) {
        questpools.add(qp);
    }

    public Set<Questpool> getQuestpools() {
        return questpools;
    }
}
