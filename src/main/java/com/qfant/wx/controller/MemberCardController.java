package com.qfant.wx.controller;

import com.aliyuncs.exceptions.ClientException;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qfant.gjp.entity.VipCard;
import com.qfant.gjp.service.VipCardService;
import com.qfant.utils.*;
import com.qfant.wx.entity.Member;
import com.qfant.wx.entity.Order;
import com.qfant.wx.service.MemberService;
import com.qfant.wx.service.OrderService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardActivatedMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/wx/member")
public class MemberCardController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMpService wxService;
    private WxPayService wxPayService = new WxPayServiceImpl();
    @Autowired
    private OrderService orderService;
    @Autowired
    private Environment env;

    @Autowired
    VipCardService vipCardService;
    @Autowired
    MemberService memberService;

    public MemberCardController(WxMpService wxService) {
        this.wxService = wxService;
    }

    @GetMapping("/charge")
    public String charge(String code, ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "charge", request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }

        map.put("openId", session.getAttribute("openId").toString());
        return "wx/charge";
    }

    @ResponseBody
    @PostMapping("/wxpay")
    public Map wxpay(Double money, String openId) throws WxPayException {
        Map result = new HashMap();
        if (StringUtils.isEmpty(openId)) {
            result.put("code", -2);
            result.put("message", "openId获取失败");
        }
        if (money < 0) {
            result.put("code", -1);
            result.put("message", "金额有误");
        }  else {
            WxPayConfig payConfig = new WxPayConfig();
            payConfig.setAppId(StringUtils.trimToNull(env.getProperty("wx.mp.appId")));
            payConfig.setMchId(StringUtils.trimToNull(env.getProperty("wx.pay.mchId")));
            payConfig.setMchKey(StringUtils.trimToNull(env.getProperty("wx.pay.mchKey")));
            payConfig.setKeyPath(StringUtils.trimToNull(env.getProperty("wx.mp.appId")));
            Order order = new Order();
            order.setIp(IpUtils.getHostIp());
            order.setSubmittime(new Date());
            order.setPrice(money);
            order.setOpenid(openId);
            orderService.saveOrder(order);
            String orderNo = genOrderNo(order.getId(), "C");
            order.setOrderno(orderNo);
            orderService.updateOrder(order);
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            wxPayService.setConfig(payConfig);
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody("微信会员卡充值");
            orderRequest.setTradeType("JSAPI");
            orderRequest.setOutTradeNo(orderNo);
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(money.toString()));//元转成分
//            orderRequest.setTotalFee(1);//元转成分
            orderRequest.setOpenid(openId);
            orderRequest.setSpbillCreateIp(IpUtils.getHostIp());
            orderRequest.setTimeStart(DateUtils.dateTimeNow("yyMMddHHmm"));
            orderRequest.setNotifyUrl("http://htgy.qfant.com/wx/member/notify");
            WxPayMpOrderResult wxPayMpOrderResult = wxPayService.createOrder(orderRequest);
            result.put("code", 0);
            result.put("result", wxPayMpOrderResult);
        }
        return result;
    }


    /**
     * 生成订单号
     *
     * @return
     */
    private String genOrderNo(int orderId, String type) {
        /*//时间(8位) + 6位订单id*/
        String time = DateUtils.parseDateToStr("yyyyMMddHHmmss", new Date());
        String orderIdStr = String.valueOf(orderId);
        for (int i = 0; i < 6 - orderIdStr.length(); i++) {
            time += "0";
        }
        time += orderIdStr;
        return type + time;
    }

    @PostMapping("/notify")
    @ResponseBody
    public String notify(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
        Order order = orderService.getOrderByOrderno(notifyResult.getOutTradeNo());
        if (notifyResult.getReturnCode().equals("SUCCESS")) {

            order.setNotifytime(new Date());
            order.setTimeend(notifyResult.getTimeEnd());
            order.setTransactionid(notifyResult.getTransactionId());
            order.setStatus(1);
            orderService.updateOrder(order);
            Member member=memberService.getMemberByOPenId(order.getOpenid());
            member.setBalance(member.getBalance()+order.getPrice());

            /***更新会员卡余额***/
            memberService.updateMember(member);
            VipCard vipCard=vipCardService.getVipCardByCardNo(member.getCardno());
            vipCard.setCz(vipCard.getCz().add(new BigDecimal(order.getPrice())));
            vipCardService.updateVipCard(vipCard);
            /**会员卡余额更新结束**/
            return WxPayNotifyResponse.success("成功");
        } else {
            order.setErrcode(notifyResult.getErrCode());
            order.setErrcodedes(notifyResult.getErrCodeDes());
            order.setResultcode(notifyResult.getResultCode());
            order.setStatus(2);
            orderService.updateOrder(order);
            return WxPayNotifyResponse.fail("失败");
        }

    }

    @GetMapping("/chargeRecord")
    public String chargeRecord(String code, ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "chargeRecord", request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }
        String openId = session.getAttribute("openId").toString();
        List<Order> orders = orderService.getOrderByOpenId(openId);
        map.put("orders", orders);
        return "wx/chargeRecord";
    }
    @GetMapping("/bind")
    public String bind(String code, ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "bind", request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }
        String openId = session.getAttribute("openId").toString();
        map.put("openId", openId);
        return "wx/bind";
    }
    @GetMapping("/balance")
    public String balance(String code, ModelMap map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "balance", request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }
        String openId = session.getAttribute("openId").toString();
        Member member=memberService.getMemberByOPenId(openId);
        if(member==null){
            return "redirect:https://mp.weixin.qq.com/s/bht6fI6Vy9nbkRKZW9Fe5g";
        }else {
            VipCard vipCard=vipCardService.getVipCardByCardNo(member.getCardno());
//            member.setName(vipCard.getName());
//            member.setBalance(vipCard.getCz().doubleValue());
//            member.setBonus(vipCard.getIntegralTotal().intValue());
//            map.put("vipCard", vipCard);
            map.put("name", vipCard.getName());
            map.put("cardNo", vipCard.getCardNo());
            map.put("balance", vipCard.getCz().setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("integral", vipCard.getIntegralTotal().intValue());
            return "wx/balance";
        }
    }
    private String init(String code, String method, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (code != null && !"".equals(code)) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
            try {
                wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
                session.setAttribute("openId", wxMpOAuth2AccessToken.getOpenId());
                return null;
            } catch (WxErrorException e) {
                String url = wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/member/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
                return "redirect:"+url;
            }
        } else {
            String url = wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/member/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
            return "redirect:"+url;
        }
    }

    @GetMapping("/getCode")
    @ResponseBody
    public Map getCode(String cardNo) {
        Map result = new HashMap();
        if (StringUtils.isEmpty(cardNo)) {
            result.put("code", -1);
            result.put("msg", "卡号不能为空");
        } else {
            VipCard vipCard=vipCardService.getVipCardByCardNo(cardNo);
            if(vipCard==null){
                result.put("code", -1);
                result.put("msg", "此会员卡号不存在");
            }else {
                if(StringUtils.isEmpty(vipCard.getTel())){
                    result.put("code", -1);
                    result.put("msg", "此会员卡关联手机号为空");
                }else {
                    String regex = "0{0,1}(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(vipCard.getTel());
                    boolean isMatch = m.matches();
                    if (!isMatch) {
                        result.put("code", -1);
                        result.put("msg", "此会员卡关联手机号有误");
                    } else {
                        String code = SmsVerificationCodeUtil.randomCode();
                        SmsVerificationCodeVo smsVerificationCodeVo = new SmsVerificationCodeVo(vipCard.getTel(), code, new Date());
                        SmsVerificationCodeUtil.setVerificationCode(vipCard.getTel(), smsVerificationCodeVo);
                        try {
                            SmsAliyun.sendSms(vipCard.getTel(),code);
                        } catch (ClientException e) {
                            e.printStackTrace();
                            logger.error("发送验证码失败："+e.getMessage());
                            result.put("code", 0);
                            result.put("msg", "验证码发送成功");
                        }
                        result.put("code", 0);
                        result.put("msg", "验证码发送成功");
                    }
                }
            }
        }
        return result;
    }

    @PostMapping("/bindCard")
    @ResponseBody
    public Map bindCard(String cardno, String code,String openId) throws WxErrorException {
        Map result = new HashMap();
        if(StringUtils.isEmpty(cardno)&&StringUtils.isEmpty(code)&&StringUtils.isEmpty(openId)){
            result.put("code", -1);
            result.put("msg", "绑定信息不能为空");
        }else {
            VipCard vipCard=vipCardService.getVipCardByCardNo(cardno);
            if(vipCard==null){
                result.put("code", -1);
                result.put("msg", "此会员卡号不存在");
            }else {
                if (StringUtils.isEmpty(cardno)||StringUtils.isEmpty(code)) {
                    result.put("code", -1);
                    result.put("msg", "验证码和卡号不能为空");
                }else {
                    if(code.equals(SmsVerificationCodeUtil.getVerificationCode(vipCard.getTel()))){
                        Member member=memberService.getMemberByOPenId(openId);//获取mysql数据库会员信息
                        if(member!=null){
                            vipCardService.deleteVipCardByCardNo(member.getCardcode(),member.getPhone());
                            String cardCode=member.getCardcode();
                            member.setName(vipCard.getName());
                            member.setPhone(vipCard.getTel());
                            if(vipCard.getSex()==0){
                                member.setGender("男");
                            }else if(vipCard.getSex()==1){
                                member.setGender("女");
                            }else {
                                member.setGender("其他");
                            }
                            if(vipCard.getBirthday()!=null){
                                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                member.setBirthday(sdf.format(vipCard.getBirthday()));
                            }
                            member.setCardno(cardno);//关联管家婆会员卡
                            member.setType(2);//老会员
                            member.setBonus(vipCard.getIntegralTotal().doubleValue());
                            member.setBalance(vipCard.getCz().doubleValue());
                            member.setBindtime(new Date());
                            member.setCardcode(cardno);
                            memberService.updateMember(member);//更新会员信息并激活会员卡

                            WxMpMemberCardActivatedMessage cardActivatedMessage=new WxMpMemberCardActivatedMessage();
                            cardActivatedMessage.setMembershipNumber(cardno);
                            cardActivatedMessage.setCode(cardCode);
                            cardActivatedMessage.setInitBonus((int)member.getBonus());
                            cardActivatedMessage.setInitBalance(member.getBalance());
                            wxService.getMemberCardService().activateMemberCard(cardActivatedMessage);

                            result.put("code", 0);
                            result.put("msg", "绑定成功");
                        }else {
                            result.put("code", -1);
                            result.put("msg", "微信会员卡领取信息不存在");
                        }
                    }else {
                        result.put("code", -1);
                        result.put("msg", "验证码输入有误");
                    }
                }
            }
        }

        return result;
    }

}
