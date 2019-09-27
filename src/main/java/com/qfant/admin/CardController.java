package com.qfant.admin;

import com.qfant.wx.api.ApiResult;
import com.qfant.wx.entity.Card;
import com.qfant.wx.service.CardService;
import com.qfant.wx.service.UserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.card.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/card")
public class CardController extends BaseController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMpService wxService;

    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;
    @Autowired
    private Environment env;
    public CardController(WxMpService wxService) {
        this.wxService = wxService;
    }


    @RequestMapping("/index")
    public String index(ModelMap map) {
        Card card=cardService.getCardById(1);
        map.put("card",card);
        return "card";
    }
    @PostMapping("/update")
    public String update (ModelMap map,Card card) throws WxErrorException {
       cardService.updateCard(card);
        map.put("card",card);

//        File file=new File("e:/logo.png");
//        File file2=new File("e:/vipcard.jpg");
//        WxMediaImgUploadResult res = wxService.getMaterialService().mediaImgUpload(file);
//        System.out.println(res.getUrl());
//        res = wxService.getMaterialService().mediaImgUpload(file2);
//        System.out.println(res.getUrl());
        return "card";
    }

    @GetMapping("/delete")
    @ResponseBody
    public ApiResult delete () throws WxErrorException {
        Card card=cardService.getCardById(1);
        WxMpCardDeleteResult result=wxService.getCardService().deleteCard(card.getWxcardId());

        return ApiResult.create("{'code':0}");
    }

    @GetMapping("/generate")
    @ResponseBody
    public ApiResult generate (ModelMap map) throws WxErrorException {
        Card card=cardService.getCardById(1);
        WxMpCardCreateResult result=wxService.getMemberCardService().createMemberCard(card.getCardJson());
        if(result.isSuccess()){
            card.setWxcardId(result.getCardId());
            cardService.updateCard(card);


            MemberCardActivateUserFormRequest memberCardActivateUserFormRequest=new MemberCardActivateUserFormRequest();
            memberCardActivateUserFormRequest.setCardId(result.getCardId());

            memberCardActivateUserFormRequest.setBindOldCard("老会员绑定",env.getProperty("payment.domain")+"/wx/member/bind");

            MemberCardUserForm required=new MemberCardUserForm();
            required.setCanModify(true);
            List<String> commonFieldList=new ArrayList<>();
            commonFieldList.add("USER_FORM_INFO_FLAG_MOBILE");
            commonFieldList.add("USER_FORM_INFO_FLAG_SEX");
            commonFieldList.add("USER_FORM_INFO_FLAG_NAME");
            commonFieldList.add("USER_FORM_INFO_FLAG_BIRTHDAY");
            required.setWechatFieldIdList(commonFieldList);
            memberCardActivateUserFormRequest.setRequiredForm(required);
            MemberCardActivateUserFormResult formResult=wxService.getMemberCardService().setActivateUserForm(memberCardActivateUserFormRequest);
        }

        return ApiResult.create("{'code':0}");
    }
}