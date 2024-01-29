package com.example.backend.config;

import com.example.backend.domain.User;
import com.example.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserInfoDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(username);
        if(user.isEmpty()){
            throw  new UsernameNotFoundException("user not found " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(),user.get().getHashedPassword(), Collections.emptyList());
    }


}
