package com.qfant.wx.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qfant.utils.DateUtils;
import com.qfant.utils.IpUtils;
import com.qfant.utils.StringUtils;
import com.qfant.wx.api.ApiResult;
import com.qfant.wx.entity.Order;
import com.qfant.wx.service.OrderService;
import com.qfant.wx.service.WeixinService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wx/member")
public class MemberCardController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WeixinService wxService;
    private WxMpService wxMpService=new WxMpServiceImpl();
    private WxPayService wxPayService=new WxPayServiceImpl();
    @Autowired
    private OrderService orderService;
    @Autowired
    private Environment env;
    @GetMapping("/bind")
    public String bind(){
        return "bind";
    }
//    @GetMapping("/charge")
//    public String charge(){
//        return "wx/charge";
//    }
    @GetMapping("/charge")
    public String charge(String code, ModelMap map, HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        if(session.getAttribute("openId")!=null){
            map.put("openId",session.getAttribute("openId").toString());
        }else {
            init(code,"charge",request,response);
        }
        return "wx/charge";
    }
    @ResponseBody
    @PostMapping("/wxpay")
    public Map wxpay(Double money,String openId) throws WxPayException {
        Map result=new HashMap();
        if(StringUtils.isEmpty(openId)){
            result.put("code",-2);
            result.put("message","openId获取失败");
        }
        if(money<0){
            result.put("code",-1);
            result.put("message","金额有误");
        }else if ((money-100)!=0 && (money-200)!=0 && (money-500)!=0){
            result.put("code",-1);
            result.put("message","金额有误");
        }else {
            WxPayConfig payConfig = new WxPayConfig();
            payConfig.setAppId(StringUtils.trimToNull(env.getProperty("wx.mp.appId")));
            payConfig.setMchId(StringUtils.trimToNull(env.getProperty("wx.pay.mchId")));
            payConfig.setMchKey(StringUtils.trimToNull(env.getProperty("wx.pay.mchKey")));
            payConfig.setKeyPath(StringUtils.trimToNull(env.getProperty("wx.mp.appId")));
            Order order= new Order();
            order.setIp(IpUtils.getHostIp());
            order.setSubmittime(new Date());
            order.setPrice(money);
            order.setOpenid(openId);
            orderService.saveOrder(order);
            String orderNo=genOrderNo(order.getId(),"C");
            order.setOrderno(orderNo);
            orderService.updateOrder(order);
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            wxPayService.setConfig(payConfig);
            WxPayUnifiedOrderRequest orderRequest=new WxPayUnifiedOrderRequest();
            orderRequest.setBody("微信会员卡充值");
            orderRequest.setTradeType("JSAPI");
            orderRequest.setOutTradeNo(orderNo);
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(money.toString()));//元转成分
            orderRequest.setOpenid(openId);
            orderRequest.setSpbillCreateIp(IpUtils.getHostIp());
            orderRequest.setTimeStart(DateUtils.dateTimeNow("yyMMddHHmm"));
            orderRequest.setNotifyUrl("http://htgy.qfant.com/wx/member/notify");
            WxPayUnifiedOrderResult orderResult=wxPayService.unifiedOrder(orderRequest);
            result.put("code",0);
            result.put("orderResult",orderResult);
        }
        return result;
    }


    /**
     * 生成订单号
     * @return
     */
   private String genOrderNo(int orderId,String type) {
        /*//时间(8位) + 6位订单id*/
        String time = DateUtils.parseDateToStr("yyyyMMddHHmmss",new Date());
        String orderIdStr = String.valueOf(orderId);
        for (int i = 0; i < 6 - orderIdStr.length() ; i++) {
            time += "0";
        }
        time += orderIdStr;
        return type+time;
    }
    @PostMapping("/notify")
    public String notify (@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(xmlData);
        Order order=orderService.getOrderByOrderno(notifyResult.getOutTradeNo());
        if(notifyResult.getReturnCode().equals("SUCCESS")){

            order.setNotifytime(new Date());
            order.setTimeend(notifyResult.getTimeEnd());
            order.setTransactionid(notifyResult.getTransactionId());
            order.setStatus(1);
            orderService.updateOrder(order);
            return WxPayNotifyResponse.success("成功");
        }else {
            order.setErrcode(notifyResult.getErrCode());
            order.setErrcodedes(notifyResult.getErrCodeDes());
            order.setResultcode(notifyResult.getResultCode());
            order.setStatus(2);
            orderService.updateOrder(order);
            return WxPayNotifyResponse.fail("失败");
        }

    }

    @GetMapping("/chargeRecord")
    public String chargeRecord(String code,ModelMap map, HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        if(session.getAttribute("openId")!=null){
            String openId=session.getAttribute("openId").toString();
            List<Order> orders=orderService.getOrderByOpenId(openId);
            map.put("orders",orders);
        }else {
            init(code,"charge",request,response);
        }
        return "wx/chargeRecord";
    }

    private void init(String code, String method,HttpServletRequest request,HttpServletResponse response) throws IOException {
        HttpSession session=request.getSession();
        if(session.getAttribute("openId")==null){
            if(code!=null && !"".equals(code)){
                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
                try {
                    wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
                    session.setAttribute("openId",wxMpOAuth2AccessToken.getOpenId());
                } catch (WxErrorException e) {
                    String url=wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/member/"+method, WxConsts.OAuth2Scope.SNSAPI_BASE,null);
                    response.sendRedirect(url);
                }
            }else {
                String url=wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/member/"+method, WxConsts.OAuth2Scope.SNSAPI_BASE,null);
                response.sendRedirect(url);
            }
        }
    }
}
