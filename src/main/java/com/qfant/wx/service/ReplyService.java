package com.qfant.wx.service;


import com.qfant.wx.entity.Reply;
import com.qfant.wx.repository.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReplyService {
    @Autowired
    private ReplyMapper replyMapper;

    public Reply getReplyById(int id){
        return replyMapper.getReplyById(id);
    }

    public void updateReply (Reply reply){
        replyMapper.update(reply);
    }

    public void deleteReply (int id){
        replyMapper.deleteReply(id);
    }
}
