package com.qfant.wx.repository;

import com.qfant.wx.entity.Seller;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SellerMapper {
    @Results({

            @Result(property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "storename",column = "name"),
            @Result(property = "phone",column = "phone"),
            @Result(property = "openid",column = "openid"),
            @Result(property = "storeid",column = "storeid"),
            @Result(property = "bindtime",column = "bindtime"),
            @Result(property = "audittime",column = "audittime"),
            @Result(property = "status",column = "status"),
            @Result(property = "isdelete",column = "isdelete"),
            @Result(property = "online",column = "online")
    })
    @Select("select se.id,se.name,st.name,se.phone,se.openid,se.storeid,se.bindtime,se.audittime,se.status from seller se,store st where se.storeid = st.id and se.id = #{id}")
    Seller getSellerById(Integer id);

    @Select("select se.id,se.name,st.name,se.phone,se.openid,se.storeid,se.bindtime,se.audittime,se.status,se.isdelete,se.online  " +
            "from seller se,store st where se.storeid = st.id and isdelete=0 limit #{page},#{pageSize}")
    List<Seller> selectSellerAll(Integer page,Integer pageSize);

    @Select("select count(*) from seller where isdelete = #{isdelete}")
    Integer getSellerTotal(Integer isdelete);

    @Insert("insert into seller(name,phone,openid,storeid,bindtime) values(#{name},#{phone},#{openid},#{storeid},#{bindtime})")
    void insertSeller(Seller seller);

    @Delete("delete from seller where id = #{id}")
    void deleteSellerById(Integer id);

    @Update("update seller set isdelete = #{isdelete} where id = #{id}")
    void editIsdeleteById(Integer id,Integer isdelete);

    @Update("update seller set status=1, audittime = #{audittime} where id = #{id}")
    void updatePass(Seller seller);

    @Update("update seller set status=2, audittime = #{audittime} where id = #{id}")
    void updateNopass(Seller seller);

    @Select("SELECT * FROM seller WHERE openid = #{openId}")
    Seller getSellerByopenId(String openId);

    @Update("update seller set storeid=#{storeid} where id=#{id}")
    void update(Seller seller);


}

