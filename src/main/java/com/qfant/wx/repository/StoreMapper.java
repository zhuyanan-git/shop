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

    @Select("select * from store limit #{start},#{pageSize}")
    List<Store> selectStoreAll(Integer start ,Integer pageSize);

    @Select("select * from store")
    List<Store> selectStoreList();

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

    @Update("update store set name=#{name}, address=#{address},phone=#{phone},sort=#{sort},qrcode=#{qrcode} where id=#{id}")
    void update(Store store);
}
