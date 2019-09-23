package com.qfant.wx.repository;

import com.qfant.wx.entity.Member;
import com.qfant.wx.entity.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReplyMapper {
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "keyword",column = "keyword"),
            @Result(property = "content",column = "content"),
            @Result(property = "createtime",column = "createtime")
            })
    @Select("SELECT * FROM Reply WHERE id = #{id}")
    Reply getReplyById(int id);

    @Insert("INSERT INTO Reply(keyword, content,createtime) VALUES (#{keyword}, #{content},#{createtime})")
    void insert(Reply reply);


    @Update("update Reply set keyword=#{keyword}, content=#{content},createtime=#{createtime} where id=#{id}")
    void update(Reply reply);


    @Select("<script> select * from Reply where" +
            " <if test='keyword!=null'> keyword like concat('%',#{keyword},'%') </if>" +
            "</script>")
    List<Reply> selectReplyList(Reply reply);

    @Delete("delete from reply where id=#{id}")
    void deleteReply(int id);
}
