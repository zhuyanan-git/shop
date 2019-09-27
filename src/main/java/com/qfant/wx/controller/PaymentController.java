package com.qfant.wx.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qfant.utils.DateUtils;
import com.qfant.utils.IpUtils;
import com.qfant.utils.StringUtils;
import com.qfant.wx.entity.Order;
import com.qfant.wx.entity.Seller;
import com.qfant.wx.entity.Store;
import com.qfant.wx.service.OrderService;
import com.qfant.wx.service.SellerService;
import com.qfant.wx.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private StoreService storeService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private Environment env;
    @GetMapping("/toPay")
    public String toPay(Integer storeid,String code, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "toPay?storeid="+storeid, request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }
        Store store=storeService.selectStoreById(storeid);
        map.put("openId", session.getAttribute("openId").toString());
        map.put("store", store);
         return "wx/pay";
    }


    @ResponseBody
    @PostMapping("/storepay")
    public Map storepay(Double money, String openId,Integer storeid) throws WxPayException {
        Map result = new HashMap();
        if (StringUtils.isEmpty(openId)) {
            result.put("code", -2);
            result.put("message", "openId获取失败");
            return result;
        }
        if (storeid==null || storeid<=0) {
            result.put("code", -3);
            result.put("message", "支付信息不正确");
            return result;
        }
        if (money <= 0) {
            result.put("code", -1);
            result.put("message", "金额有误");
            return result;
        }
        Order order = new Order();
        order.setIp(IpUtils.getHostIp());
        order.setSubmittime(new Date());
        order.setPrice(money);
        order.setOpenid(openId);
        order.setType(2);//设置订单为扫码支付类型
        order.setStoreid(storeid);
        Store store=storeService.selectStoreById(storeid);
        order.setStorename(store.getName());//设置门店信息
        orderService.saveOrder(order);
        String orderNo = genOrderNo(order.getId(), "C");
        order.setOrderno(orderNo);
        orderService.updateOrder(order);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(store.getName()+"门店二维码收款");
        orderRequest.setTradeType("JSAPI");
        orderRequest.setOutTradeNo(orderNo);
        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(money.toString()));//元转成分
            orderRequest.setTotalFee(1);//元转成分
        orderRequest.setOpenid(openId);
        orderRequest.setGoodsTag(store.getName());//门店名称
        orderRequest.setAttach(storeid+"");
        orderRequest.setSpbillCreateIp(IpUtils.getHostIp());
        orderRequest.setTimeStart(DateUtils.dateTimeNow("yyMMddHHmm"));
        orderRequest.setNotifyUrl(env.getProperty("payment.domain")+"/wx/payment/notify");

        WxPayMpOrderResult wxPayMpOrderResult = wxPayService.createOrder(orderRequest);
        result.put("code", 0);
        result.put("result", wxPayMpOrderResult);
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
                String url = wxService.oauth2buildAuthorizationUrl(env.getProperty("payment.domain")+"/wx/payment/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
                return "redirect:"+url;
            }
        } else {
            String url = wxService.oauth2buildAuthorizationUrl(env.getProperty("payment.domain")+"/wx/payment/"  + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
            return "redirect:"+url;
        }
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
            Store store =storeService.selectStoreById(order.getStoreid());
            List<Seller> sellers=sellerService.selectSellerByStoreId(order.getStoreid());
            if(sellers!=null&&sellers.size()>0){
                String msgId;
                try {
                    for (Seller s:sellers){
                        /**发送模板消息**/
                        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder().toUser("").templateId(env.getProperty("wx.mp.tplMessageId")).url("").build();
                        templateMessage.addData(new WxMpTemplateData("first", store.getName(), "#FF00FF"));
                        templateMessage.addData(new WxMpTemplateData("keyword1", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,order.getNotifytime()), "#FF00FF"));
                        templateMessage.addData(new WxMpTemplateData("keyword2", new BigDecimal(order.getPrice()/100).setScale(2).toString(), "#FF00FF"));
//                        templateMessage.addData(new WxMpTemplateData("remark", "如需查看账单明细，请点击详情！", "#FF00FF"));
                        msgId = this.wxService.getTemplateMsgService().sendTemplateMsg(templateMessage);
                    }
                    order.setIsnotice(1);
                    order.setNoticetime(new Date());
                    orderService.updateOrder(order);
                } catch (WxErrorException e) {
                    e.printStackTrace();
                    logger.error("订单通知失败："+e.getMessage());
                }
            }

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
