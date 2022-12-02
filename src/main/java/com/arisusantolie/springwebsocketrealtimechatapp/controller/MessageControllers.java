package com.arisusantolie.springwebsocketrealtimechatapp.controller;

import com.arisusantolie.springwebsocketrealtimechatapp.dto.*;
import com.arisusantolie.springwebsocketrealtimechatapp.service.MessageService;
import com.arisusantolie.springwebsocketrealtimechatapp.service.UserAndGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Slf4j
@RestController
@CrossOrigin
public class MessageControllers {

    @Autowired
    MessageService messageService;

    @Autowired
    UserAndGroupService userAndGroupService;

    @MessageMapping("/chat/{to}")
    public void sendMessagePersonal(@DestinationVariable String to, MessageDTO message) {
//        log.debug(String.format("WS RQ <<< /chat/%s msg:%s", to, message));
        messageService.sendMessage(to, message);

    }

    @MessageMapping("/events/{to}")
    public void sendEvent(@DestinationVariable String to, EventDTO eventDTO){
//        log.debug(String.format("WS RQ <<< /events/%s %s", to, eventDTO));
        messageService.sendEvent(to, eventDTO);
    }

    @MessageMapping("/prevEvents/{to}")
    public void sendEvent(@DestinationVariable String to, CEvent cEvent){
//        log.debug(String.format("WS RQ <<< /prevEvents/%s event:%s",to, cEvent));
        messageService.sendEvent(cEvent);
    }

    @MessageMapping("/log/{from}")
    public void logFromApp(@DestinationVariable String from, String appLog) {
//        log.debug(String.format("LOGAPP [%s] %s", from, appLog));
    }

    @GetMapping("getAllEvents")
    public List<CEvent> getAllEvents(){
//        log.debug(String.format("REST RQ <<< /getAllEvents"));
        return messageService.getAllEventsV1();
    }

    @GetMapping("/v1/getAllEvents")
    public List<CEvent> getAllEventsV1(){
//        log.debug(String.format("REST RQ <<< /v1/getAllEvents"));
        return messageService.getAllEventsV1();
    }

    @GetMapping("getMessagesById")
    public List getMessagesById(@RequestParam("id")String id) {
//        log.debug(String.format("REST RQ <<< /getMessagesById id=%s", id));
        return messageService.getMessagesById(id);
    }

    @GetMapping("getChatBySubscriberId")
    public List getChatBySubscriberId(@RequestParam("id")String id) {
//        log.debug(String.format("REST RQ <<< /getChatBySubscriberId id=%s", id));
        return messageService.getChatBySubscriberId(id);
    }

    @PostMapping("postNewChat")
    public void postNewChat(@RequestBody CChat chat) {
//        log.debug(String.format("REST RQ <<< /postNewChat chat:%s", chat));
        messageService.postNewChat(chat);
    }

    @GetMapping("/")
    public  String getMainPage(){
        return "Hello!";
    }

    @GetMapping("/fetchAllUsers/{myId}")
    public List<Map<String,Object>> fetchAll(@PathVariable("myId") String myId) {
        System.out.println("<<<<<<fetchAllUsers/"+myId);
        return userAndGroupService.fetchAll(myId);
    }

    @GetMapping("/fetchAllGroups/{groupid}")
    public List<Map<String,Object>> fetchAllGroup(@PathVariable("groupid") String groupId) {
        System.out.println("<<<<<<fetchAllGroup");
        return  userAndGroupService.fetchAllGroup(groupId);
    }

}
