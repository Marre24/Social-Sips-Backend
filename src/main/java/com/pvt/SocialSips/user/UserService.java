package com.pvt.SocialSips.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(@Validated User user) {
        Optional<User> u = userRepository.findById(user.getId());
        if(authenticate(user)) return u.orElseThrow(() -> new IllegalStateException("Not a valid user!"));
        return null;
    }

    public boolean authenticate(User user){
        return true;
    }

    public void register(User user) {

    }
}
