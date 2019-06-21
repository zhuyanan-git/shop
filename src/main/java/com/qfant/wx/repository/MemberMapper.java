package com.qfant.wx.repository;

import com.qfant.wx.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM Member WHERE id = #{id}")
    Member getMemberById(int id);

    @Insert("INSERT INTO member(name, phone,idcard,gender,birthday,openid,nickname,bonus,balance,status,cardno,cardcode,type,createtime,bindtime) " +
            "VALUES (#{name}, #{phone},#{idcard}, #{gender},#{birthday}, #{openid}, #{nickname},#{bonus}, #{balance},#{status},#{cardno}, #{cardcode},#{type},#{createtime},#{bindtime})")
    void insert(Member member);

    @Select("SELECT * FROM Member WHERE openid = #{openId}")
    Member getMemberByopenId(String openId);

    @Update("update member set name=#{name}, phone=#{phone},idcard=#{idcard},gender=#{gender},birthday=#{birthday},openid=#{openid},nickname=#{nickname},bonus=#{bonus}" +
            ",balance=#{balance},status=#{status},cardno=#{cardno},cardcode=#{cardcode},type=#{type},createtime=#{createtime} ,bindtime=#{bindtime} where id=#{id}")
    void update(Member member);
}
