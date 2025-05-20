package com.pvt.SocialSips.user;

import com.google.firebase.auth.FirebaseToken;
import com.pvt.SocialSips.questpool.Questpool;
import com.pvt.SocialSips.role.Role;
import com.pvt.SocialSips.authentication.FirebaseAuthenticationToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

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

    public User getOrCreateUser(String userUid) {
        User u = userRepository.findById(userUid).orElse(null);
        if(u == null){
            Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(o instanceof FirebaseAuthenticationToken token){
                FirebaseToken firebaseToken = token.getFirebaseToken();
                u = new User(firebaseToken.getName(),
                        firebaseToken.getUid(),
                        token.getAuthorities().stream()
                                .map(auth -> new Role(auth
                                        .getAuthority()))
                                .collect(Collectors.toList()));
                userRepository.save(u);
            }
            else System.out.println(o);
        }
        return u;

    }
}
