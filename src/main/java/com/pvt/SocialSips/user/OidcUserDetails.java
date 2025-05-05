package com.pvt.SocialSips.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.*;

public class OidcUserDetails implements UserDetails, OidcUser {

    private Map<String, Object> attributes;
    private OidcUserInfo oidcUserInfo;
    private OidcIdToken oidcIdToken;
    private final User user;

    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public OidcUserDetails(User user, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.user = user;
        this.grantedAuthorities = grantedAuthorities;
        attributes = new HashMap<>();
    }

    public OidcUserDetails(User user, Collection<? extends GrantedAuthority> grantedAuthorities, OidcUserInfo oidcUserInfo, OidcIdToken oidcIdToken){
        this.user = user;
        this.grantedAuthorities = grantedAuthorities != null ? grantedAuthorities : new ArrayList<>();
        this.oidcUserInfo = oidcUserInfo;
        this.oidcIdToken = oidcIdToken;
    }

    public OidcUserDetails(User user){
        this(user, null);
    }


    @Override
    public String getName() {
        return user.getFirstName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return "google";
    }

    @Override
    public String getUsername() {
        return getSubject();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUserInfo.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUserInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcIdToken;
    }

}
