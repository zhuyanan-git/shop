package com.qfant.wx.repository;

import com.qfant.wx.entity.Card;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CardMapper {
    @Results({ //2
            @Result(property = "id", column = "id"), //2
            @Result(property = "cardJson", column = "card_json"),
            @Result(property = "wxcardId", column = "wxcard_id")
    })
    @Select("SELECT * FROM card WHERE id = #{id}")
    Card getCardById(int id);

    @Update("update card set card_json=#{cardJson},wxcard_id=#{wxcardId} where id = #{id}")
    void updateCard(Card card);
}
