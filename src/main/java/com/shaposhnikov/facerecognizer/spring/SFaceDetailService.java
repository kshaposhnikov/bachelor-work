package com.shaposhnikov.facerecognizer.spring;

import com.shaposhnikov.facerecognizer.data.SFaceUser;
import com.shaposhnikov.facerecognizer.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 09.05.2017.
 */
@Configuration
public class SFaceDetailService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SFaceUser sFaceUser = repository.findByUserName(username);
        List<GrantedAuthority> roles = new ArrayList<>();
        sFaceUser.getRole().forEach(role -> roles.add(new SimpleGrantedAuthority("ROLE_" + role)));
        return new User(sFaceUser.getUserName(), sFaceUser.getPassword(), roles);
    }
}
