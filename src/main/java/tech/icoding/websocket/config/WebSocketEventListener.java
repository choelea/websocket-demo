package tech.icoding.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import tech.icoding.websocket.service.WsService;

import java.time.LocalDateTime;

@Component
@Slf4j
public class WebSocketEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private WsService wsService;

    public WebSocketEventListener(WsService wsService) {
        this.wsService = wsService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        final String name = event.getUser().getName();
        log.info("SessionSubscribeEvent from user {}" + name);
        final String simpDestination = (String)event.getMessage().getHeaders().get("simpDestination");
        if(simpDestination.equals("/user"+Constants.Ws.privateMsgDest)){
            Long userId = Long.parseLong(name);
            wsService.sendUnreadMessages(userId, LocalDateTime.now());
        }

    }
}