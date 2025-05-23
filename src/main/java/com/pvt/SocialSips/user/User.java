package com.pvt.SocialSips.user;

import com.pvt.SocialSips.event.Event;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.role.Role;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sub", referencedColumnName = "host_sub")
    private Event event;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hostId")
    private final Set<Questpool> questpools = new HashSet<>();

    public User() {
    }

    public User(@NonNull User user){
        this.sub = user.sub;
        this.firstName = user.firstName;
        this.roles = user.roles;
        this.event = user.event;
        this.questpools.addAll(user.questpools);
    }

    public User(String firstName, String sub) {
        this.sub = sub;
        this.firstName = firstName;
    }

    public User(String firstName, String sub, List<Role> roles) {
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

    public void setEvent(String name, Integer groupSize, Set<Questpool> questpools) {
        this.event = new Event(name, groupSize, questpools, getSub());
    }

    public Event getEvent() {
        return event;
    }

    public void removeEvent() {
        event = null;
    }

    public void removeQuestpool(Questpool qp) {
        questpools.remove(qp);
    }
}
