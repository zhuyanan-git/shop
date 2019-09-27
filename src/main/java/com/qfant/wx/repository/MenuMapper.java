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
            @Result(property = "keyword",column = "keyword"),
            @Result(property = "url",column = "url"),
            @Result(property = "appid",column = "appid"),
            @Result(property = "pagepath",column = "pagepath"),
            @Result(property = "pid",column = "pid"),
            @Result(property = "sort",column = "sort"),
    })


    @Select("SELECT * FROM menu WHERE id = #{id}")
    Menu getMenuById(Integer id);



    @Select("SELECT * FROM menu WHERE pid = #{pid} order by sort desc")
    List<Menu> getMenuByPid(Integer pid);

    @Insert("INSERT INTO menu(name,type,keyword,url,appid,pagepath,pid,sort)" +
            " VALUES (#{name},#{type},#{keyword},#{url},#{appid},#{pagepath},#{pid},#{sort})")
    void insert(Menu menu);

    @Update("update menu set name=#{name},keyword=#{keyword},type=#{type},url=#{url},appid=#{appid},pagepath=#{pagepath},pid=#{pid},sort=#{sort} where id=#{id}")
    void update(Menu menu);


    @Select("select * from menu order by sort desc limit #{start},#{pageSize} ")
    List<Menu> selecrAllMenu(Integer start,Integer pageSize);

    @Select("select count(*) from menu")
    Integer getTotal();

    @Delete("delete from menu where id=#{id}")
    void deleteById(Integer id);
}
