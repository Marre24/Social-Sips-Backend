package com.pvt.SocialSips.user;

import com.pvt.SocialSips.questpool.Questpool;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final GuestRepository guestRepository;

    @Autowired
    public UserService(UserRepository userRepository, GuestRepository guestRepository) {
        this.userRepository = userRepository;
        this.guestRepository = guestRepository;
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
            throw new EntityNotFoundException("User with sub: " + sub + " does not exist!");
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

    public User getOrCreateUser(User user) {
        String sub = user.getSub();
        String name = user.getFirstName();

        Optional<User> u = userRepository.findById(sub);
        if(u.isPresent()) return u.get();

        return userRepository.save(new User(name, sub));
    }

    public Guest registerGuest(String uuid) {
        Guest g = new Guest(uuid);

        try {
            g = guestRepository.save(g);

        }catch(Exception e) {
            throw new DuplicateKeyException("Could not save guest with uuid: " + uuid);
        }

        return g;
    }
}
