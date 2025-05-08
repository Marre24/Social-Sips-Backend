package com.pvt.SocialSips.user;

import com.pvt.SocialSips.event.EventRepository;
import com.pvt.SocialSips.event.EventService;
import com.pvt.SocialSips.questpool.Questpool;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public User login(OidcUserRequest request) {
        System.out.println("\tAccessToken Scopes = ");
        request.getAccessToken().getScopes().forEach(System.out::println);
        System.out.println("OidcUserRequest");
        request.getIdToken();
        return null;
    }

    @Transactional
    public User register(User user) {
        return userRepository.save(user);
    }

    public User getUserBySub(String sub) {
        return userRepository.findById(sub).orElse(null);
    }

    public Set<Questpool> getAllQuestpoolsBySub(String sub) {
        return userRepository.getReferenceById(sub).getQuestpools();
    }

    public void deleteUser(User standard) {
        userRepository.deleteById(standard.getSub());
        eventRepository.deleteById(standard.getSub());
    }
}
