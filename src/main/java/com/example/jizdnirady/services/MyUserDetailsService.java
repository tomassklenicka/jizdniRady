package com.example.jizdnirady.services;

import com.example.jizdnirady.entity.http.User;
import com.example.jizdnirady.models.MyUserDetails;
import com.example.jizdnirady.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        System.out.println(userName + " username");
        Optional<User> user = userRepository.findByUserName(userName);

        user.orElseThrow(()->new UsernameNotFoundException("Not Found: "+userName));

        return user.map(MyUserDetails::new).get();
    }
}
