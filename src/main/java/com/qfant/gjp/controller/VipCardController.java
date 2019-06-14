package com.qfant.gjp.controller;

import com.qfant.gjp.entity.VipCard;
import com.qfant.gjp.repository.VipCardMapper;
import com.qfant.wx.entity.Member;
import com.qfant.wx.repository.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/vip")
public class VipCardController {
//    @Autowired
//    private VipCardMapper vipCardRepository;
//    @Autowired
//    private MemberMapper memberRepository;
//
//    @RequestMapping("/getCardNo")
//    public String getCardNo(){
//        Member member=memberRepository.getOne(1);
//
//        List<VipCard> vipCards=vipCardRepository.findVipCard("00001");
//        return "index";
//    }
}
