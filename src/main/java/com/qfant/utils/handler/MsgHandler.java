package com.qfant.utils.handler;

import com.qfant.utils.builder.TextBuilder;
import com.qfant.wx.entity.Reply;
import com.qfant.wx.service.ReplyService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang
 */
@Component
public class MsgHandler extends AbstractHandler {
    @Autowired
    private ReplyService replyService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        String content = "";
//    if (wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
//      content="";
//    }else
        if (wxMessage.getMsgType().equals(XmlMsgType.TEXT)) {

            Reply reply = replyService.getReplyByKeyword(wxMessage.getContent());
            //TODO 组装回复消息
//        content = "收到信息内容：" + JsonUtils.toJson(wxMessage);
            if (reply != null) {
                content = reply.getContent();
            }

        }

//    //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
//    try {
//      if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
//              && weixinService.getKefuService().kfOnlineList()
//              .getKfOnlineList().size() > 0) {
//        return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
//                .fromUser(wxMessage.getToUser())
//                .toUser(wxMessage.getFromUser()).build();
//      }
//    } catch (WxErrorException e) {
//      e.printStackTrace();
//    }


        return new TextBuilder().build(content, wxMessage, weixinService);

    }

}
