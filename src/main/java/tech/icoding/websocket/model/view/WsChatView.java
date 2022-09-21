package tech.icoding.websocket.model.view;

import lombok.Data;

/**
 *
 * 视图
 * @author : Joe
 * @date : 2022/9/21
 */
@Data
public class WsChatView {
    private Long chatId;
    private String user;
    private String nickName;
    private String headimgUrl;
}
