package tech.icoding.websocket.model.view;

import lombok.Data;

import javax.persistence.Entity;

/**
 *
 * 视图
 * @author : Joe
 * @date : 2022/9/21
 */
@Data
public class WsChatView {
    private Long chatId;
    private Long user;
    private String nickName;
    private String headimgUrl;
//    private Long lastMsgId;
//    private String lastText;
}
