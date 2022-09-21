package tech.icoding.websocket.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import tech.icoding.websocket.data.Result;
import tech.icoding.websocket.exception.BizException;
import tech.icoding.websocket.form.ChatMsgForm;
import tech.icoding.websocket.model.core.WsChat;
import tech.icoding.websocket.model.core.WsChatMessage;
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

    public ChatController(WsChatService wsChatService) {
        this.wsChatService = wsChatService;
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
     * 获取指定chat的消息的列表，根据endId向前查询小于此id的消息
     * @param id
     * @param endId
     * @param size
     * @return
     */
    @GetMapping("/chats/{id}/messages")
    public Result open(@PathVariable Long id,@RequestParam Long endId, @RequestParam Integer size){
        final List<WsChatMessage> messages = wsChatService.findMessages(id, endId, size);
        return Result.success(messages);
    }


    @PostMapping("/chats/{id}/messages")
    public Result open(@PathVariable Long id, @RequestBody ChatMsgForm chatMsgForm){
        if(!id.equals(chatMsgForm.getChatId())) throw new BizException("Chat ID 不匹配");
        WsChatMessage wsChatMessage = new WsChatMessage();
        BeanUtils.copyProperties(chatMsgForm, wsChatMessage);
        wsChatMessage.setChatId(id);
        wsChatService.saveMessage(wsChatMessage);
        return Result.success();
    }
}
