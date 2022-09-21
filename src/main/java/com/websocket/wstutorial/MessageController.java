package com.websocket.wstutorial;

import com.websocket.wstutorial.dto.Message;
import com.websocket.wstutorial.dto.PrivateMessage;
import com.websocket.wstutorial.dto.ResponseMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getContent()));
    }

    @MessageMapping("/private-message")
//    @SendToUser("/topic/private-messages")
    public void getPrivateMessage(final PrivateMessage privateMessage) throws InterruptedException {
        messagingTemplate.convertAndSendToUser(privateMessage.getToUser(),"/topic/private-messages", privateMessage);
    }
}
