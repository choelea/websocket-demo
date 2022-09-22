package tech.icoding.websocket.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tech.icoding.websocket.config.Constants;
import tech.icoding.websocket.form.ChatMsgForm;
import tech.icoding.websocket.model.core.WsChatMessage;
import tech.icoding.websocket.service.WsChatMessageService;
import tech.icoding.websocket.service.WsChatService;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WsChatService wsChatService;
    private final WsChatMessageService wsChatMessageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, WsChatService wsChatService, WsChatMessageService wsChatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.wsChatService = wsChatService;
        this.wsChatMessageService = wsChatMessageService;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public WsChatMessage getMessage(final WsChatMessage message) {
        return new WsChatMessage(); // TODO 全局消息可以用
    }

    /**
     * 收到私信后处理
     * @param chatMsgForm
     */
    @MessageMapping("/private-message")
    public void privateMessage(final ChatMsgForm chatMsgForm) {
        WsChatMessage wsChatMessage = new WsChatMessage();
        BeanUtils.copyProperties(chatMsgForm, wsChatMessage);
        final WsChatMessage saveMessage = wsChatMessageService.save(wsChatMessage);
        messagingTemplate.convertAndSendToUser(chatMsgForm.getToUser().toString(), Constants.Ws.privateMsgDest, saveMessage);
    }

    /**
     * 已读确认, 暂时不同，暂时采用退出聊天框的时候设置
     * @param chatMsgForm
     */
    @MessageMapping("/message-read")
    public void readNotify(final ChatMsgForm chatMsgForm) {
        WsChatMessage wsChatMessage = new WsChatMessage();
        BeanUtils.copyProperties(chatMsgForm, wsChatMessage);
        final WsChatMessage saveMessage = wsChatMessageService.save(wsChatMessage);
        messagingTemplate.convertAndSendToUser(chatMsgForm.getToUser().toString(),Constants.Ws.privateMsgDest, saveMessage);
    }

    /**
     * 上线通知
     * @param chatMsgForm
     */
    @MessageMapping("/notify-online")
    public void retrieveUnreadMsg(final ChatMsgForm chatMsgForm) {
        WsChatMessage wsChatMessage = new WsChatMessage();
        BeanUtils.copyProperties(chatMsgForm, wsChatMessage);
        final WsChatMessage saveMessage = wsChatMessageService.save(wsChatMessage);
        messagingTemplate.convertAndSendToUser(chatMsgForm.getToUser().toString(),"/topic/private-messages", saveMessage);
    }

}
