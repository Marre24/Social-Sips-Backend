package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
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


    @Transactional
    //this method uses transactional and an unused call to getQuestpools to fetch the questpools to solve the standard lazy fetch
    public User getUserBySub(String sub) {
        Optional<User> user = userRepository.findById(sub);
        if (user.isEmpty())
            return null;
        int size = user.get().getQuestpools().size();
        return user.get();
    }

    public Set<Questpool> getAllQuestpoolsBySub(String sub) {
        return userRepository.getReferenceById(sub).getQuestpools();
    }

    public void deleteUser(User standard) {
        userRepository.deleteById(standard.getSub());
    }
}
