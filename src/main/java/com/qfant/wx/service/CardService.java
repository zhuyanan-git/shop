package com.qfant.wx.service;


import com.qfant.wx.entity.Card;
import com.qfant.wx.entity.User;
import com.qfant.wx.repository.CardMapper;
import com.qfant.wx.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CardService {
    @Autowired
    private CardMapper cardMapper;

    public Card getCardById(int id){
        return cardMapper.getCardById(id);
    }

    public void updateCard (Card card){
        cardMapper.updateCard(card);
    }
}
