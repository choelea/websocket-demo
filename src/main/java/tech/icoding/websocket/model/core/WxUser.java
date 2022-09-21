package tech.icoding.websocket.model.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;

/**
 * 此表的数据在其他库中维护
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
public class WxUser{

    private static final long serialVersionUID = -7874585038954327317L;

    @Id
    private Long id;

    private String nickName;


    private String headimgUrl;

}