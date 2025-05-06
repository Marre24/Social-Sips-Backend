package com.pvt.SocialSips.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HostService {

    private final HostRepository hostRepository;

    @Autowired
    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
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
        return hostRepository.save(host);
    }

    public Host getUserBySub(String sub) {
        return hostRepository.findById(sub).orElse(null);
    }
}
