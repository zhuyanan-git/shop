package com.qfant.wx.repository;


import com.qfant.wx.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    public User getUserById(int id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    public User getUserByUsername(String username);
}
