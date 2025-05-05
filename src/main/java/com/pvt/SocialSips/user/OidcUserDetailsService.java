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

    private final UserService userService;

    @Autowired
    public OidcUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        User user = userService.getUserBySub(oidcUser.getEmail());

        if (user == null) {
            user = new User();
            user.setSub(oidcUser.getSubject());
            user.setFirstName(oidcUser.getGivenName());
            user.setDeviceId("Android_1337");
            user.setRoles((Arrays.asList(new Role("OIDC_USER"), new Role("GUEST"))));
            userService.register(user);
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }

    @Override
    public OidcUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserBySub(email);

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("role_" + role.getName()))
                .collect(Collectors.toList());

        return new OidcUserDetails(new User(), authorities);
    }
}

