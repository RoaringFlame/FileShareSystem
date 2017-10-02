package com.fss.util.security;

import com.fss.controller.vo.UserInfo;
import com.fss.dao.domain.User;
import com.fss.dao.repositories.UserRepository;
import com.fss.util.HeadPictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            String role = "ROLE_" + user.getRole().name();
            authorities.add(new SimpleGrantedAuthority(role));

            UserInfo userInfo = new UserInfo(user.getUsername(), user.getPassword(), user.isUsable(),
                    authorities);
            userInfo.setUserId(user.getId());
            userInfo.setName(user.getName());
            userInfo.setEmail(user.getEmail());
            userInfo.setDepartment(user.getDepartment().getText());
            userInfo.setPictureName(HeadPictureUtil.getPictureName(user.getGender(),user.getRole()));
            return userInfo;
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }
}