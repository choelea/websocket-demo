package tech.icoding.websocket.config;

/**
 * @author : Joe
 * @date : 2022/9/22
 */
public interface Constants {
    interface Ws{
        /**
         * 私有消息的目标地址
         */
        String privateMsgDest = "/topic/private-messages";
    }
}
