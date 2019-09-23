package com.qfant.wx.repository;


import com.qfant.wx.entity.Menu;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 乐乐
 * @site www.javanood.com
 * @create 2019-09-23 15:15
 */
@Mapper
public interface MenuMapper {


    @Results({
            @Result(property = "name",column = "name"),
            @Result(property = "type",column = "type"),
            @Result(property = "key",column = "key"),
            @Result(property = "url",column = "url"),
            @Result(property = "appid",column = "appid"),
            @Result(property = "pagepath",column = "pagepath"),
            @Result(property = "pid",column = "pid"),
    })


    @Select("SELECT * FROM menu WHERE id = #{id}")
    Menu getMenuById(Integer id);



    @Select("SELECT * FROM menu WHERE openid = #{openId}")
    List<Menu> getMenuByOpenId(String openId);

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO menu(name,type,key,url,appid,pagepath,pid)" +
            "VALUES (#{name},#{type},#{key},#{url},#{pagepath},#{pid})")
    void insert(Menu menu);

    @Update("update menu set name=#{name}, type=#{type},url=#{url},appip=#{appip},pagepath=#{pagepath},pid=#{pid} where id=#{id}")
    void update(Menu menu);


    @Select(" select * from menu limit #{page},#{pageSize} ")
    List<Menu> selecrAllMenu(Integer page,Integer pageSize);

    @Select("select count(*) from menu")
    Integer getTotal();

}
