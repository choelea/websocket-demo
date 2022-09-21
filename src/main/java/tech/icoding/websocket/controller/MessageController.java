package tech.icoding.websocket.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tech.icoding.websocket.form.ChatMsgForm;
import tech.icoding.websocket.model.core.WsChatMessage;
import tech.icoding.websocket.service.WsChatService;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WsChatService wsChatService;

    public MessageController(SimpMessagingTemplate messagingTemplate, WsChatService wsChatService) {
        this.messagingTemplate = messagingTemplate;
        this.wsChatService = wsChatService;
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
    public void getPrivateMessage(final ChatMsgForm chatMsgForm) {
        WsChatMessage wsChatMessage = new WsChatMessage();
        BeanUtils.copyProperties(chatMsgForm, wsChatMessage);
        final WsChatMessage saveMessage = wsChatService.saveMessage(wsChatMessage);
        messagingTemplate.convertAndSendToUser(chatMsgForm.getToUser().toString(),"/topic/private-messages", saveMessage);
    }
}
