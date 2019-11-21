package com.kmm.login.service;

import com.kmm.login.entity.User;
import com.kmm.login.repository.UserInfoMapper;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 登录验证逻辑层
     * @param userName
     * @return
     */
    public User getUserByUsername(String userName){ return userInfoMapper.getUserByUserName(userName);    }

    public String getUserById(Integer id){ return userInfoMapper.getUserById(id);}

    public void update(Integer id,String newPassword,String salt){userInfoMapper.update(id,newPassword,salt);}

}
