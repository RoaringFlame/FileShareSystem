package com.fss.util.security;

import com.eshore.fss.sysmgr.dao.UserDao;
import com.eshore.fss.sysmgr.pojo.User;
import com.eshore.fss.util.HeadPictureUtil;
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
    private UserDao userDao;

    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            String role = "ROLE_" + user.getRole().name();
            authorities.add(new SimpleGrantedAuthority(role));

            UserInfo userInfo = new UserInfo(user.getUsername(), user.getPassword(), user.getUsable(),
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