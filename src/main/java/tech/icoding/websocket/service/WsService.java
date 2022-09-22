package tech.icoding.websocket.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tech.icoding.websocket.config.Constants;
import tech.icoding.websocket.model.core.WsChatMessage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Joe
 * @date : 2022/9/22
 */
@Service
public class WsService {
    private final SimpMessagingTemplate messagingTemplate;
    private final WsChatMessageService wsChatMessageService;

    public WsService(SimpMessagingTemplate messagingTemplate, WsChatMessageService wsChatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.wsChatMessageService = wsChatMessageService;
    }

    /**
     * 推送未读消息
     * @param toUser
     * @param endDateTime
     */
    public void sendUnreadMessages(Long toUser, LocalDateTime endDateTime){
        final List<WsChatMessage> unReadMessages = wsChatMessageService.findUnReadMessages(toUser, endDateTime, 100);
        unReadMessages.forEach(wsChatMessage -> {
            messagingTemplate.convertAndSendToUser(toUser.toString(), Constants.Ws.privateMsgDest, wsChatMessage);
        });
    }
}
