package com.qfant.wx.controller;

import com.qfant.wx.entity.Member;
import com.qfant.wx.service.MemberService;
import com.qfant.wx.service.WeixinService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.membercard.NameValues;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardActivatedMessage;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUserInfoResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/wx/portal")
public class WeixinMsgController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WeixinService wxService;
    @Autowired
    private MemberService memberService;
    @Autowired
    public WeixinMsgController(WeixinService wxService) {
        this.wxService = wxService;
    }
    @ResponseBody
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (this.wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @ResponseBody
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody, @RequestParam(name="signature", required = false) String signature,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name="timestamp", required = false) String timestamp, @RequestParam("nonce") String nonce) throws WxErrorException {
        this.logger.info("\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            saveMember(inMessage);
            WxMpXmlOutMessage outMessage = this.wxService.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
                    this.wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            saveMember(inMessage);
            this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.wxService.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(this.wxService.getWxMpConfigStorage());
        }

        this.logger.debug("\n组装回复信息：{}", out);

        return out;
    }

    private void saveMember(WxMpXmlMessage inMessage)  throws WxErrorException {
        if(inMessage.getEvent().equals("submit_membercard_user_info")){
            WxMpMemberCardUserInfoResult userInfoResult=wxService.getMemberCardService().getUserInfo(inMessage.getCardId(),inMessage.getUserCardCode());
            if(userInfoResult.getErrorCode().equals("0")){
                Member member=memberService.getMemberByOPenId(userInfoResult.getOpenId());
                member.setCreatetime(new Date());
                member.setNickname(userInfoResult.getNickname());
                member.setOpenid(userInfoResult.getOpenId());
                member.setStatus(userInfoResult.getUserCardStatus());
                member.setName(userInfoResult.getNickname());
                member.setCardcode(inMessage.getUserCardCode());
                member.setCardno(inMessage.getUserCardCode());
                member.setType(1);
                NameValues[] nameValues=userInfoResult.getUserInfo().getCommonFieldList();
                if(nameValues.length>0){
                    for(int i=0;i<nameValues.length;i++){
                        if(nameValues[i].getName().equals("USER_FORM_INFO_FLAG_NAME")){
                            member.setName(nameValues[i].getValue());
                        }else if (nameValues[i].getName().equals("USER_FORM_INFO_FLAG_MOBILE")){
                            member.setPhone(nameValues[i].getValue());
                        }else if (nameValues[i].getName().equals("USER_FORM_INFO_FLAG_SEX")){
                            member.setGender(nameValues[i].getValue());
                        }else if (nameValues[i].getName().equals("USER_FORM_INFO_FLAG_BIRTHDAY")){
                            member.setBirthday(nameValues[i].getValue());
                        }
                    }
                }
                memberService.saveAllMember(member);
                WxMpMemberCardActivatedMessage cardActivatedMessage=new WxMpMemberCardActivatedMessage();
                cardActivatedMessage.setMembershipNumber(member.getCardcode());
                cardActivatedMessage.setCode(member.getCardcode());
                cardActivatedMessage.setInitBonus(0);
                cardActivatedMessage.setInitBalance(0d);
                wxService.getMemberCardService().activateMemberCard(cardActivatedMessage);

            }else {
                this.logger.error("获取用户信息失败,错误码："+userInfoResult.getErrorCode()+" 错误信息："+userInfoResult.getErrorMsg());
            }

        }else if(inMessage.getEvent().equals("user_get_card")){
            Member member=memberService.getMemberByOPenId(inMessage.getFromUser());
            if(member!=null){//如果存在就更新会员卡信息
                member.setOpenid(inMessage.getFromUser());
                member.setCardcode(inMessage.getUserCardCode());
                member.setCreatetime(new Date());
                memberService.updateMember(member);
            }else {
                member=new Member();
                member.setOpenid(inMessage.getFromUser());
                member.setCardcode(inMessage.getUserCardCode());
                member.setCreatetime(new Date());
                memberService.insertMember(member);
            }
        }
    }
}
