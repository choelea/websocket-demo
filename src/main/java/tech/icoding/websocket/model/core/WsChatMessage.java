package tech.icoding.websocket.model.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author : Joe
 * @date : 2022/9/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WsChatMessage extends BaseEntity<Long>{
    private static final long serialVersionUID = -2255914886441886600L;

    private Long chatId;

    private Long fromUser;

    private Long toUser;

    private String content;

    /**
     * 1 - 文本; 2 - 图片
     */
    private Integer type;

    /**
     * 是否已读, 1-已读 0-未读
     */
    private Integer readFlag = 0;
}
