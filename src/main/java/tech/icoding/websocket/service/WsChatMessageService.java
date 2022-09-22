package tech.icoding.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.icoding.websocket.model.WsChatMessage_;
import tech.icoding.websocket.model.core.WsChat;
import tech.icoding.websocket.model.core.WsChatMessage;
import tech.icoding.websocket.repo.WsChatMessageRepository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author : Joe
 * @date : 2022/9/21
 */
@Service
@Slf4j
public class WsChatMessageService extends BaseService<WsChatMessageRepository, WsChatMessage, Long> {

    /**
     * 查找最后一条记录的ID
     * @param chatId
     * @param endId 减少搜索范围提高效率
     * @return
     */
    public WsChatMessage findLastMessage(Long chatId, Long endId){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WsChatMessage> criteriaQuery = criteriaBuilder.createQuery(WsChatMessage.class);
        Root<WsChatMessage> root = criteriaQuery.from(WsChatMessage.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get(WsChatMessage_.CHAT_ID), chatId),
                criteriaBuilder.ge(root.get(WsChatMessage_.ID), endId));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(WsChatMessage_.ID)));
        TypedQuery<WsChatMessage> query = entityManager.createQuery(criteriaQuery);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 将小于等于 lastMsgId的消息置为已读
     * @param chatId
     * @param userId
     * @param lastMsgId
     */
    public int markRead(Long chatId, Long userId, Long lastMsgId) {
        final TypedQuery<WsChat> query = entityManager.createQuery(
                "UPDATE WsChatMessage m SET m.readFlag = 1 WHERE m.chatId = :chatId AND m.toUser = :userId AND m.readFlag = 0 AND m.id <= :lastMsgId", WsChat.class);
        query.setParameter("chatId", chatId);
        query.setParameter("userId", userId);
        query.setParameter("lastMsgId", lastMsgId);
        final int i = query.executeUpdate();
        log.info("Mark Chat {}'s {} to user {} messages whose id <= {} as read ", chatId, i, userId, lastMsgId);
        return i;
    }

    /**
     * 查找目标用户的未读消息
     * @param toUser  目标用户ID
     * @param endDateTime  最大时间
     * @param size
     * @return
     */
    public List<WsChatMessage> findUnReadMessages(Long toUser, LocalDateTime endDateTime, int size){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WsChatMessage> criteriaQuery = criteriaBuilder.createQuery(WsChatMessage.class);
        Root<WsChatMessage> root = criteriaQuery.from(WsChatMessage.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get(WsChatMessage_.TO_USER), toUser),
                criteriaBuilder.lessThan(root.get(WsChatMessage_.CREATED_TIME), endDateTime),
                criteriaBuilder.equal(root.get(WsChatMessage_.READ_FLAG),  0));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(WsChatMessage_.ID)));

        TypedQuery<WsChatMessage> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(size);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
