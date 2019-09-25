package com.qfant.wx.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qfant.utils.DateUtils;
import com.qfant.utils.IpUtils;
import com.qfant.utils.StringUtils;
import com.qfant.wx.entity.Order;
import com.qfant.wx.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/wx/payment")
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMpService wxService;
    private final WxPayService wxPayService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private Environment env;
    @GetMapping("/toPay")
    public String toPay(String code, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "toPay", request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }

        map.put("openId", session.getAttribute("openId").toString());
        return "wx/pay";
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

            Order order = new Order();
            order.setIp(IpUtils.getHostIp());
            order.setSubmittime(new Date());
            order.setPrice(money);
            order.setOpenid(openId);
            orderService.saveOrder(order);
            String orderNo = genOrderNo(order.getId(), "C");
            order.setOrderno(orderNo);
            orderService.updateOrder(order);

            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody("微信二维码支付");
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

    private String init(String code, String method, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (code != null && !"".equals(code)) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
            try {
                wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
                session.setAttribute("openId", wxMpOAuth2AccessToken.getOpenId());
                return null;
            } catch (WxErrorException e) {
                String url = wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/payment/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
                return "redirect:"+url;
            }
        } else {
            String url = wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/payment/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
            return "redirect:"+url;
        }
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
}
