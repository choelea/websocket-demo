package tech.icoding.websocket.form;

import lombok.Data;

/**
 * 聊天信息
 * @author : Joe
 * @date : 2022/9/21
 */
@Data
public class ChatMsgForm {
    private Long chatId;
    private Long fromUser;
    private Long toUser;
    private Integer type;
    private String content;
}
