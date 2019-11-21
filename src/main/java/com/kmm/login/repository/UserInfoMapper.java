package com.kmm.login.repository;

import com.kmm.login.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
public interface UserInfoMapper {
    @Results({
        @Result(property = "id",column = "id"),
        @Result(property = "userName",column = "user_name"),
        @Result(property = "password",column = "password"),
        @Result(property = "salt",column = "salt")


    })

    /**
     * 登录验证用户名密码
     */
    @Select("select * from user_info where user_name = #{userName}")
    public User getUserByUserName(String userName);

    /**
     * 修改密码获取旧密码
     */
    @Select("select password from user_info where id = #{id} ")
    public String getUserById(Integer id);

    @Update("update user_info set password = #{password},salt = #{salt} where id = #{id} ")
    public void update( Integer id,String password,String salt);
}
