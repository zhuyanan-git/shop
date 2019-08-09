package com.qfant.wx.service;

import com.qfant.gjp.entity.VipCard;
import com.qfant.gjp.repository.VipCardMapper;
import com.qfant.utils.UUIDUtils;
import com.qfant.wx.entity.Member;
import com.qfant.wx.repository.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional
public class MemberService {
    @Autowired
    private VipCardMapper vipCardMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Transactional
    public void saveAllMember(Member member) {//新增加会员
        memberMapper.update(member);
        VipCard vipCard=new VipCard();
        vipCard.setCardNo(member.getCardcode());
        vipCard.setName(member.getName());
        vipCard.setTel(member.getPhone());

        vipCard.setBirthday( Timestamp.valueOf(member.getBirthday()+" 00:00:00"));
        vipCard.setBulidDate(new Date());
        vipCard.setCardid(UUIDUtils.getUUID36Bits());
        vipCard.setVIPBarCode(member.getCardcode());
        vipCardMapper.insert(vipCard);
    }
    @Transactional
    public void insertMember(Member member) {
        memberMapper.insert(member);
    }
    @Transactional
    public void updateMember(Member member) {
        memberMapper.update(member);
    }
    public Member getMemberByOPenId(String openId){
        return memberMapper.getMemberByopenId(openId);
    }
}
