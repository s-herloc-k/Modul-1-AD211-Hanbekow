package com.modul.sherlock.configs;

import com.modul.sherlock.models.Role;
import com.modul.sherlock.models.User;
import com.modul.sherlock.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public boolean createUser(User user)  {

        if(userRepo.findByEmail(user.getEmail())!= null) return false;

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new user: {}", user.getEmail());
        userRepo.save(user);
        return true;
    }
}
