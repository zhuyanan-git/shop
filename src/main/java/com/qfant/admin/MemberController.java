package com.qfant.admin;

import com.qfant.framework.AjaxResult;
import com.qfant.utils.page.TableDataInfo;
import com.qfant.utils.poi.ExcelUtil;
import com.qfant.wx.entity.Member;
import com.qfant.wx.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{

    @Autowired
    private MemberService memberService;

    @GetMapping()
    public String member(){ return "member/member";}
    //查询会员信息
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Member member){
        startPage();
        List<Member> list = memberService.selectMemberList(member);
        return getDataTable(list);

    }

    /**
     * 导出家庭成员列表
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Member member) {
        List<Member> list = memberService.selectMemberList(member);
        ExcelUtil<Member> util = new ExcelUtil<Member>(Member.class);
        return util.exportExcel(list, "会员信息");
    }

}
