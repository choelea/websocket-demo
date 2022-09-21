package tech.icoding.websocket.model.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WsChat extends  BaseEntity<Long>{

    private static final long serialVersionUID = 981677910418186165L;


    @Column(length = 64,unique=true)
    private String name;

    private Long userOne;

    private Long userTwo;

}