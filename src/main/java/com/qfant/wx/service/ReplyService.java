package com.qfant.wx.service;


import com.qfant.utils.Convert;
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

    public Reply getReplyById(int id){
        return replyMapper.getReplyById(id);
    }

    public int updateReply (Reply reply){return replyMapper.update(reply); }

    public int deleteReplyByIds (String ids){return replyMapper.deleteReplyByIds(Convert.toStrArray(ids));}

    public int insertReply(Reply reply){ return replyMapper.insert(reply);}

    public Reply getReplyByKeyword(String keyword){
        return replyMapper.getReplyByKeyword(keyword);
    }
}
