package tech.icoding.websocket.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import tech.icoding.websocket.data.Result;
import tech.icoding.websocket.exception.BizException;
import tech.icoding.websocket.form.ChatMsgForm;
import tech.icoding.websocket.form.IdForm;
import tech.icoding.websocket.model.core.WsChat;
import tech.icoding.websocket.model.core.WsChatMessage;
import tech.icoding.websocket.service.WsChatMessageService;
import tech.icoding.websocket.service.WsChatService;
import java.util.List;

/**
 * 私聊相关API
 * @author : Joe
 * @date : 2022/9/21
 */
@RestController
public class ChatController {

    private WsChatService wsChatService;
    private WsChatMessageService wsChatMessageService;

    public ChatController(WsChatService wsChatService, WsChatMessageService wsChatMessageService) {
        this.wsChatService = wsChatService;
        this.wsChatMessageService = wsChatMessageService;
    }

    /**
     * 获取chat列表
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}/chats")
    public Result chats(@PathVariable Long userId){
        return Result.success(wsChatService.findChatsByJpql(userId));
    }

    /**
     * 打开私聊窗口，有则返回，无则创建
     * @param userOne
     * @param userTwo
     * @returnResult
     */
    @GetMapping("/chats/_open")
    public Result open(@RequestParam Long userOne, @RequestParam Long userTwo){
        WsChat wsChat = wsChatService.get(userOne, userTwo);
        if(wsChat==null){
            wsChat = wsChatService.get(userTwo, userOne);
        }
        if(wsChat==null){
            wsChat = wsChatService.create(userTwo, userOne);
        }
        return Result.success(wsChat);
    }

    /**
     * 退出私聊窗口, 根据userId和chatId及最后一条MsgID将小于等于该msgId的消息置为已读
     * @param userId
     * @return
     */
    @PutMapping("/users/{userId}/chats/{id}/_quit")
    public Result quit(@PathVariable Long userId, @PathVariable Long id, @RequestBody IdForm idForm){
        wsChatService.markRead(id, userId, idForm.getId() );
        return Result.success();
    }

    /**
     * 获取指定chat的消息的列表，根据endId向前查询小于此id的消息
     * @param id
     * @param endId
     * @param size
     * @return
     */
    @GetMapping("/chats/{id}/messages")
    public Result open(@PathVariable Long id,@RequestParam Long endId, @RequestParam Integer size){
        final List<WsChatMessage> messages = wsChatMessageService.findMessages(id, endId, size);
        return Result.success(messages);
    }


    @PostMapping("/chats/{id}/messages")
    public Result open(@PathVariable Long id, @RequestBody ChatMsgForm chatMsgForm){
        if(!id.equals(chatMsgForm.getChatId())) throw new BizException("Chat ID 不匹配");
        WsChatMessage wsChatMessage = new WsChatMessage();
        BeanUtils.copyProperties(chatMsgForm, wsChatMessage);
        wsChatMessage.setChatId(id);
        wsChatMessageService.save(wsChatMessage);
        return Result.success();
    }
}
