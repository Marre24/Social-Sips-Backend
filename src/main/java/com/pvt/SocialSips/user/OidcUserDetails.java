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
    private final Host host;

    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public OidcUserDetails(Host host, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.host = host;
        this.grantedAuthorities = grantedAuthorities;
        attributes = new HashMap<>();
    }

    public OidcUserDetails(Host host, Collection<? extends GrantedAuthority> grantedAuthorities, OidcUserInfo oidcUserInfo, OidcIdToken oidcIdToken){
        this.host = host;
        this.grantedAuthorities = grantedAuthorities != null ? grantedAuthorities : new ArrayList<>();
        this.oidcUserInfo = oidcUserInfo;
        this.oidcIdToken = oidcIdToken;
    }

    public OidcUserDetails(Host host){
        this(host, null);
    }


    @Override
    public String getName() {
        return host.getFirstName();
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
