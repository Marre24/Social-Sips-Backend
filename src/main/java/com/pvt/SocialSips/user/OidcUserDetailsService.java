package com.pvt.SocialSips.user;

import com.pvt.SocialSips.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OidcUserDetailsService extends OidcUserService implements UserDetailsService {

    private final HostService hostService;

    @Autowired
    public OidcUserDetailsService(HostService hostService) {
        this.hostService = hostService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            return processOidcUser(oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUser oidcUser) {
        Host host = hostService.getUserBySub(oidcUser.getSubject());

        if (host == null) {
            host = new Host();
            host.setSub(oidcUser.getSubject());
            host.setFirstName(oidcUser.getGivenName());
            host.setDeviceId("Android_1337");
            host.setRoles((Arrays.asList(new Role("OIDC_USER"), new Role("GUEST"))));
            hostService.register(host);
        }
        List<GrantedAuthority> authorities = host.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }

    @Override
    public OidcUserDetails loadUserByUsername(String sub) throws UsernameNotFoundException {
        Host host = hostService.getUserBySub(sub);

        List<GrantedAuthority> authorities = host.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("role_" + role.getName()))
                .collect(Collectors.toList());

        return new OidcUserDetails(new Host(), authorities);
    }
}

