package com.qfant.wx.service;


import com.qfant.wx.entity.User;
import com.qfant.wx.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(int id){
        return userMapper.getUserById(id);
    }

    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

}
