package tech.icoding.websocket.repo;

import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.websocket.model.core.WsChat;

@Repository
public interface WsChatRepository extends JpaSpecificationExecutor<Long>, JpaRepository<WsChat, Long> {
}
