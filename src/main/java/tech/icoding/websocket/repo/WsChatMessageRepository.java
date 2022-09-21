package tech.icoding.websocket.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.icoding.websocket.model.core.WsChatMessage;

@Repository
public interface WsChatMessageRepository extends JpaSpecificationExecutor<Long>, JpaRepository<WsChatMessage, Long> {
}
