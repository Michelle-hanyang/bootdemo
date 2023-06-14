package com.example.bootdemo.service.sys;

import com.example.bootdemo.dao.sys.UserRepository;
import com.example.bootdemo.pojo.entity.sys.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Author: YANG
 * Date: 2023/4/26 17:18
 * Describe:
 */
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User loadUserByUserName(User user){
        User u=userRepository.findUserByName(user);
        return u;
    }


}
