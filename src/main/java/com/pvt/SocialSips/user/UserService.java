package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.role.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        if (email != null) {
            User newUser = new User(oAuth2User.getName(), oAuth2User.getName());
            userRepository.save(newUser);
        }
        return oAuth2User;
    }

    public Set<Questpool> getAllQuestpoolsBySub(String sub) {
        User user = getUserBySub(sub);

        return user.getQuestpools();
    }

    public void deleteUser(User standard) {
        userRepository.deleteById(standard.getSub());
    }

    @Transactional
    public void removeEventFrom(String hostSub) {
        User user = getUserBySub(hostSub);
        user.removeEvent();
    }

    @Transactional
    public void removeQuestpoolFrom(User user, Questpool qp) {
        user.removeQuestpool(qp);
    }

    public User getOrCreateUser(User user) {
        User u = userRepository.findById(user.getSub()).orElse(null);
        if(u == null){
            return null;
        }
        return userRepository.save(new User(user));
    }
}
