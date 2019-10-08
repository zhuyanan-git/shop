package com.qfant.admin;

import com.qfant.wx.entity.Reply;
import com.qfant.wx.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/reply")
public class ReplyController extends BaseController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ReplyService replyService;


    @GetMapping()
    public String reply(){return "reply/reply";}

    //查询文本回复信息
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value="page") Integer page,@RequestParam(value = "limit") Integer limit, Reply reply){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        reply.setStart((page-1)*limit);
        reply.setPageSize(limit);
        Integer count = replyService.getTotal(reply);
        List<Reply> replyList = replyService.selectReplyList(reply);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data",replyList);
        return resultMap;
    }
    //增加文本回复信息
    @GetMapping("/add")
    public String add(){ return "reply/add";}

    @PostMapping("/addSave")
    @ResponseBody
    public Map<String,Object> addSave(Reply reply) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        replyService.insertReply(reply);
        resultMap.put("success",true);
        return resultMap;
    }

    //删除文本回复信息
    @PostMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(Integer id){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        replyService.deleteReplyById(id);
        resultMap.put("success",true);
        return resultMap;
    }

    //修改文本信息
    @RequestMapping("/edit")
    public String edit(Integer id,ModelMap mmap){
        Reply reply =replyService.getReplyById(id);
        mmap.put("reply",reply);
        return "reply/edit";
    }

    //修改文本信息保存
    @ResponseBody
    @RequestMapping("/editSave")
    public Map<String,Object> editSave(Reply reply){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        replyService.updateReply(reply);
        resultMap.put("success",true);
        return  resultMap;
    }

}