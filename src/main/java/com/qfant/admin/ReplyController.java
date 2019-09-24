package com.qfant.admin;

import com.qfant.utils.AjaxResult;
import com.qfant.utils.page.TableDataInfo;
import com.qfant.wx.entity.Reply;
import com.qfant.wx.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/reply")
public class ReplyController extends BaseController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ReplyService replyService;


    @GetMapping()
    public String reply(){return "reply/reply";}

    //查询文本回复信息
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Reply reply){
        startPage();
        List<Reply> list = replyService.selectReplyList(reply);
        return getDataTable(list);
    }
    //增加文本回复信息
    @GetMapping("/add")
    public String add(){ return "reply/add";}

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Reply reply) { return toAjax(replyService.insertReply(reply)); }

    //删除文本回复信息
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids){return toAjax(replyService.deleteReplyByIds(ids));}

    //修改文本信息
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id,ModelMap mmap){
        Reply reply =replyService.getReplyById(id);
        mmap.put("reply",reply);
        return "reply/edit";
    }

    //修改文本信息保存
    @ResponseBody
    @PostMapping("/edit")
    public AjaxResult editSave(Reply reply){return  toAjax(replyService.updateReply(reply));}

}