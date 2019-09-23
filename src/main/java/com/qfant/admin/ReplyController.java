package com.qfant.admin;

import com.qfant.wx.api.ApiResult;
import com.qfant.wx.entity.Card;
import com.qfant.wx.service.CardService;
import com.qfant.wx.service.UserService;
import com.qfant.wx.service.WeixinService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.card.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/reply")
public class ReplyController extends BaseController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
}