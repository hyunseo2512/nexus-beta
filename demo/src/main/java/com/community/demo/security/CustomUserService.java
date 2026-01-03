package com.community.demo.security;

import com.community.demo.entity.User;
import com.community.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository
                .findByEmailWithAuth(username)
                .orElseThrow(()->new UsernameNotFoundException("user not Found:" + username));
        log.info(">>> login user >> {}",user);

        return new CustomAuthUser(user);
    }
}
