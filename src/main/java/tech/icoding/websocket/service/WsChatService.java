package tech.icoding.websocket.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tech.icoding.websocket.model.core.WsChat;
import tech.icoding.websocket.model.core.WsChatMessage;
import tech.icoding.websocket.repo.WsChatMessageRepository;
import tech.icoding.websocket.repo.WsChatRepository;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Joe
 * @date : 2022/9/21
 */
@Service
public class WsChatService extends BaseService<WsChatRepository, WsChat, Long> {

    private WsChatMessageRepository wsChatMessageRepository;

    public WsChatService(WsChatMessageRepository wsChatMessageRepository) {
        this.wsChatMessageRepository = wsChatMessageRepository;
    }

    /**
     * 获取/开启聊天窗口
     * @param userOne
     * @param userTwo
     */
    public WsChat get(Long userOne, Long userTwo){
        final TypedQuery<WsChat> query = entityManager.createQuery("SELECT c FROM WsChat c  WHERE c.userOne = :userOne AND  c.userTwo = :userTwo", WsChat.class);
        query.setParameter("userOne", userOne);
        query.setParameter("userTwo", userTwo);
        final List<WsChat> resultList = query.getResultList();
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 创建私聊Chat
     * @param userOne
     * @param userTwo
     * @return
     */
    public WsChat create(Long userOne, Long userTwo){
        WsChat wsChat = new WsChat();
        wsChat.setName("私聊");
        wsChat.setUserOne(userOne);
        wsChat.setUserTwo(userTwo);
        return save(wsChat);
    }

    /**
     * 获取指定Chat的消息列表
     * @param id
     * @param endId
     * @param size
     * @return
     */
    public List<WsChatMessage> findMessages(Long id, Long endId, Integer size){
        final TypedQuery<WsChatMessage> query = entityManager.createQuery("SELECT c FROM WsChatMessage c  WHERE c.chatId= :chatId AND  c.id < :endId", WsChatMessage.class);
        query.setParameter("chatId", id);
        query.setParameter("endId", endId);
        query.setMaxResults(size);
        return query.getResultList();
    }
    /**
     * https://vladmihalcea.com/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/
     * @param productId
     * @return
     */
//    public List<WsChatView> findChats(Long productId) {
//        final TypedQuery<Tuple> query = entityManager.createQuery("SELECT ps.code as code, p.id as id, ps.value as value, p.name as name "
//                + "FROM WsChat c JOIN Product p ON ps.product = p WHERE p.id = :productId", Tuple.class);
//
//
//        query.setParameter("productId", productId);
//        final List<Tuple> resultList = query.getResultList();
//        List<ProdSpecView> targetList = new ArrayList<>();
//        if(!resultList.isEmpty()){
//            resultList.forEach(tuple -> {
//                ProdSpecView prodSpecView = new ProdSpecView();
//                copy(tuple, prodSpecView);
//                targetList.add(prodSpecView);
//            });
//        }
//        return targetList;
//    }

    public void copy(Tuple source, Object target, @Nullable String... ignoreProperties){
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        final List<TupleElement<?>> elements = source.getElements();
        elements.forEach(tupleElement -> {
            final String alias = tupleElement.getAlias();
            final PropertyDescriptor targetPd = BeanUtils.getPropertyDescriptor(target.getClass(), alias);
            if(targetPd!=null){
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                    try {
                        Object value = source.get(targetPd.getName());
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    }
                    catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                    }
                }
            }
        });
    }

    public WsChatMessage saveMessage(WsChatMessage wsChatMessage) {
        return wsChatMessageRepository.save(wsChatMessage);
    }
}
