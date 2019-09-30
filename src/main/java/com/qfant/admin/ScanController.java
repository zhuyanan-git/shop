package com.qfant.admin;

import com.qfant.config.WxMpConfig;
import com.qfant.utils.*;
import com.qfant.wx.entity.MemberAndOrder;
import com.qfant.wx.entity.Order;
import com.qfant.wx.entity.User;
import com.qfant.wx.service.OrderService;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;

/**
 * @author 乐乐
 * @site www.javanood.com
 * @create 2019-09-29 15:14
 */
@Controller
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    private OrderService orderService;



    @Value("${wx.pay.url}")
    private  String url;

    @Value("${wx.mp.appId}")
    private  String appid;

    @Value("${wx.mp.secret}")
    private  String appSecret;

    @Value("${wx.pay.mchKey}")
    private  String mchKey;

    @Value("${wx.pay.mchId}")
    private  String mchid;//商家id


    @GetMapping()
    public String scan(){
        return "scan/scan";
    }



    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value="limit") Integer limit,Order order){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        order.setType(2);
        order.setStart((page-1)*limit);
        order.setPageSize(limit);
        List<Order> orderList=orderService.selectList(order);
        resultMap.put("code",0);
        resultMap.put("count",orderService.getTotal(order));
        resultMap.put("data",orderList);
        return resultMap;
    }


    @PostMapping("/refund")
    @ResponseBody
    public Map<String,Object> refund(Integer id,HttpSession session) throws Exception{
        Map<String,Object> resultMap = new HashMap<String, Object>();
        //根据订单id查询订单详情
        Order order = orderService.selectById(id);
        //设置退款单号
        order.setOutrefundno(DateUtils.dateTimeNow());
        //设置退款信息参数
        Map<String,Object> map=new HashMap<String,Object>();
        //判断是否已经退款
        Map<String,Object> resultInfo =null;
        //支付成功才能退款
        if (order.getStatus()==1){
            map.put("appid", appid); // 公众账号ID
            map.put("mch_id", mchid); // 商户号
            //  map.put("transaction_id", ""); // 微信订单号
            map.put("out_trade_no", order.getOrderno()); // 商户订单号
            map.put("nonce_str", UUIDUtils.getUUID32Bits()); // 随机字符串
            map.put("out_refund_no",order.getOutrefundno()); // 商户退款单号
            map.put("total_fee", (int)(order.getPrice()*100)); // 订单金额
            map.put("refund_fee", (int)(order.getPrice()*100)); // 退款金额
            map.put("sign", getSign(map)); // 签名
            String xml= XmlUtil.genXml(map);
            InputStream in= HttpClientUtil.sendXMLDataByHttpsPost(url, xml,mchid).getEntity().getContent(); // 发现xml消息
            //返回退款信息
           resultInfo=getElementValue(in);
        }else {
            resultMap.put("errorInfo","已退款不能重复退款");
            resultMap.put("success",false);
            return resultMap;
        }
       //判断退出是否成功
        if (("SUCCESS".equals(resultInfo.get("return_code")))&&("SUCCESS".equals(resultInfo.get("result_code")))){
            //获取当前用户
            User currentUser=(User)session.getAttribute("user");
            order.setRefundid(currentUser.getId());
            order.setRefunduser(currentUser.getUsername());
            order.setRefundtime(new Date());
            order.setStatus(3);
            //更新退款信息
            orderService.updateOrder(order);
            resultMap.put("success",true);
            return resultMap;
        }else {
            resultMap.put("errorInfo",resultInfo.get("err_code_des"));
            resultMap.put("success",false);
            return resultMap;
        }
    }


    /**
     * 通过返回IO流获取支付地址
     * @param in
     * @return
     */
    private static Map<String,Object> getElementValue(InputStream in){
        SAXReader reader = new SAXReader();
        Document document=null;
        try {
            document = reader.read(in);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        List<Element> childElements = root.elements();
        Map<String,Object> map = new HashMap<>();
        for (Element child : childElements) {
            map.put(child.getName(),child.getStringValue());
        }
        return map;
    }




    /**
     * 微信支付签名算法sign
     */
    private  String getSign(Map<String,Object> map) {
        StringBuffer sb = new StringBuffer();
        String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);//获取map中的key转为array
        Arrays.sort(keyArr);//对array排序
        for (int i = 0, size = keyArr.length; i < size; ++i) {
            if ("sign".equals(keyArr[i])) {
                continue;
            }
            sb.append(keyArr[i] + "=" + map.get(keyArr[i]) + "&");
        }
        sb.append("key="+mchKey);
        String sign = Md5Util.string2MD5(sb.toString());
        return sign;
    }

}
