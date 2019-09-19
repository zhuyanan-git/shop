package com.qfant.admin;

import com.qfant.utils.page.TableDataInfo;
import com.qfant.wx.entity.Member;
import com.qfant.wx.service.MemberService;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{

    @Autowired
    private MemberService memberService;

    @GetMapping()
    public String member(){ return "/member/member";}
    //查询会员信息
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Member member){
        startPage();
        List<Member> list = memberService.selectMemberList(member);
        return getDataTable(list);

    }

}
