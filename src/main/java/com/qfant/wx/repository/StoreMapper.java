package com.qfant.wx.repository;

import com.qfant.wx.entity.Store;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface StoreMapper {
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "address",column = "address"),
            @Result(property = "phone",column = "phone"),
            @Result(property = "sort",column = "sort")
    })

    @Select("select * from store limit #{page},#{pageSize}")
    List<Store> selectStoreAll(Integer page,Integer pageSize);

    @Select("select count(*) from store")
    Integer getStoreTotal();

    @Insert("insert into store(name,address,phone,sort) values(#{name},#{address},#{phone},#{sort})")
    void insertStore(Store store);

    @Delete("delete from store where id = #{id}")
    void deleteStore(Integer id);
    @Select("select * from store where id = #{id}")
    Store getStoreById(Integer id);

    @Update("update store set name=#{name},address=#{address},phone=#{phone},sort=#{sort} where id = #{id}")
    void updateStore(Store store);
}