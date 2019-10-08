package com.qfant.wx.service;


import com.qfant.wx.entity.Reply;
import com.qfant.wx.repository.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReplyService {
    @Autowired
    private ReplyMapper replyMapper;

    public List<Reply> selectReplyList(Reply reply){return replyMapper.selectReplyList(reply);}

    public Integer getTotal(Reply reply){return replyMapper.getTotal(reply);}

    public Reply getReplyById(int id){
        return replyMapper.getReplyById(id);
    }

    public void updateReply (Reply reply){replyMapper.update(reply); }

    public void deleteReplyById (Integer id){replyMapper.deleteReplyById(id);}

    public void insertReply(Reply reply){replyMapper.insert(reply);}

    public Reply getReplyByKeyword(String keyword){
        return replyMapper.getReplyByKeyword(keyword);
    }
}
