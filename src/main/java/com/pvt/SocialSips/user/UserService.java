package com.pvt.SocialSips.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Host login(OidcUserRequest request) {
        System.out.println("\tAccessToken Scopes = ");
        request.getAccessToken().getScopes().forEach(System.out::println);
        System.out.println("OidcUserRequest");
        request.getIdToken();
        return null;
    }

    @Transactional
    public Host register(Host host) {
        return userRepository.save(host);
    }

    public Host getUserBySub(String sub) {
        return userRepository.findBySub(sub).orElse(null);
    }

    public Host getUserByDeviceId(String deviceId) {
        Optional<Host> user = userRepository.findByDeviceId(deviceId);
        return user.orElse(null);
    }
}
