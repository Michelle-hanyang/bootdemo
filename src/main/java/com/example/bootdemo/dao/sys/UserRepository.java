package com.example.bootdemo.dao.sys;

import com.example.bootdemo.pojo.entity.sys.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: YANG
 * Date: 2023/4/26 16:22
 * Describe:
 */
public interface UserRepository  extends JpaRepository<User,String> {

    User findUserByName(User user);

}
