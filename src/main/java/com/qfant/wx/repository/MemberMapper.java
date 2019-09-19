package com.qfant.wx.repository;

import com.qfant.wx.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Results({
            @Result(property = "cardno",column = "cardno"),
            @Result(property = "name",column = "name")
            })
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


    @Select("<script> select * from member where" +
            " <if test='cardno!=null'> cardno like concat('%',#{cardno},'%') </if>" +
            " <if test='name!=null'> and name like concat('%',#{name},'%') </if> " +
            "<if test='cardno==null and name==null'> name is not null</if>"+
            "</script>")
    List<Member> selectMemberList(Member member);

}
