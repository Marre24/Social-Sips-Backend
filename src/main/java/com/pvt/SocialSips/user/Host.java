package com.pvt.SocialSips.user;

import com.pvt.SocialSips.role.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinTable(name = "user_deviceId", joinColumns = @JoinColumn(name = "USER_ID"))
    private String deviceId;
    private String sub;
    private String firstName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<Role> roles = new ArrayList<>();

    public Host(){
    }

    public Host(String deviceId) {
        this.deviceId = deviceId;
    }

    public Host(String firstName, String deviceId, String sub){
        this.sub = sub;
        this.firstName = firstName;
        this.deviceId = deviceId;
    }

    public Host(String firstName, String sub){
        this.sub = sub;
        this.firstName = firstName;

    }

    public Host(String firstName, String deviceId, String sub, List<Role> roles){
        this.deviceId = deviceId;
        this.sub = sub;
        this.firstName = firstName;
        this.roles = roles;

    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
}
