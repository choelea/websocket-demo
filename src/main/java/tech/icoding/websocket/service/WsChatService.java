package tech.icoding.websocket.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tech.icoding.websocket.model.core.WsChat;
import tech.icoding.websocket.model.core.WsChatMessage;
import tech.icoding.websocket.model.view.WsChatView;
import tech.icoding.websocket.repo.WsChatRepository;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : Joe
 * @date : 2022/9/21
 */
@Service
public class WsChatService extends BaseService<WsChatRepository, WsChat, Long> {

    private WsChatMessageService wsChatMessageService;

    public WsChatService(WsChatMessageService wsChatMessageService) {
        this.wsChatMessageService = wsChatMessageService;
    }

    /**
     * https://vladmihalcea.com/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/
     * @return
     */
    public List<WsChatView> findChatsByJpql(Long userId) {
        final TypedQuery<Tuple> query = entityManager.createQuery(
                "SELECT c.id AS chatId, " +
                        "(case when c.userOne=:userId then u2.nickName else u1.nickName end) AS nickName, "+
                        "(case when c.userOne=:userId then u2.headimgUrl else u1.headimgUrl end) AS headimgUrl, " +
                        "(case when c.userOne=:userId then u2.id else u1.id end) AS user " +
                        "FROM WsChat c JOIN WxUser u1 ON c.userOne = u1.id " +
                        "JOIN WxUser u2 ON c.userTwo = u2.id " +
                        "WHERE c.userOne = :userId or c.userTwo = :userId", Tuple.class);

        query.setParameter("userId", userId);
        query.setMaxResults(100);
        List<WsChatView> resultList = convert(query.getResultList());
        return resultList;
    }

    /**
     * 根据用户寻找chat 列表
     * @param userId
     * @return
     */
    public List<WsChatView> findChats(Long userId){
        final List<WsChatView> resultList = findByFirstUser(userId);
        resultList.addAll(findBySecondUser(userId));
        return resultList;
    }
    private List<WsChatView> findByFirstUser(Long userId){
        final TypedQuery<Tuple> query = entityManager.createQuery(
                "SELECT c.id AS chatId,c.userTwo as user, u.nickName as nickName, u.headimgUrl as headimgUrl " +
                        "FROM WsChat c JOIN WxUser u ON c.userTwo = u.id WHERE c.userOne= :userId", Tuple.class);
        query.setParameter("userId", userId);
        query.setMaxResults(100);
        final List<Tuple> resultList = query.getResultList();
        return convert(resultList);
    }

    private List<WsChatView> findBySecondUser(Long userId){
        final TypedQuery<Tuple> query = entityManager.createQuery(
                "SELECT c.id AS chatId,c.userOne as user, u.nickName as nickName, u.headimgUrl as headimgUrl " +
                        "FROM WsChat c JOIN WxUser u ON c.userOne = u.id WHERE c.userTwo= :userId", Tuple.class);
        query.setParameter("userId", userId);
        query.setMaxResults(100);
        final List<Tuple> resultList = query.getResultList();
        return convert(resultList);
    }

    private List<WsChatView> convert(List<Tuple> resultList){
        List<WsChatView> targetList = new ArrayList<>();
        if(!resultList.isEmpty()){
            resultList.forEach(tuple -> {
                WsChatView view = new WsChatView();
                copy(tuple, view);
                targetList.add(view);
            });
        }
        return targetList;
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


    /**
     * 更新日志
     * @param chatId
     * @param userId
     * @param lastMsgId
     * @return
     */
    public void markRead(Long chatId, Long userId, Long lastMsgId) {
        final int i = wsChatMessageService.markRead(chatId, userId, lastMsgId);

    }
}
