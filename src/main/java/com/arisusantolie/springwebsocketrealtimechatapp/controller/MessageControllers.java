package com.arisusantolie.springwebsocketrealtimechatapp.controller;

import com.arisusantolie.springwebsocketrealtimechatapp.dto.*;
import com.arisusantolie.springwebsocketrealtimechatapp.service.MessageService;
import com.arisusantolie.springwebsocketrealtimechatapp.service.UserAndGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
public class MessageControllers {

    @Autowired
    MessageService messageService;

    @Autowired
    UserAndGroupService userAndGroupService;

    @MessageMapping("/chat/{to}")
    public void sendMessagePersonal(@DestinationVariable String to, MessageDTO message) {
        System.out.println("<<<<<</chat/"+to+" msg:"+message);
        messageService.sendMessage(to, message);

    }

    @MessageMapping("/events/{to}")
    public void sendEvent(@DestinationVariable String to, EventDTO eventDTO){
        System.out.println("<<<<<</events/" + to + " event:" + eventDTO);
        messageService.sendEvent(to, eventDTO);
    }

    @MessageMapping("/prevEvents/{to}")
    public void sendEvent(@DestinationVariable String to, PreviewEventDTO pEventDTO){
        System.out.println("<<<<<</prevEvents event:" + pEventDTO);
        messageService.sendPrevEvent(pEventDTO);
    }

    @GetMapping("getAllEvents")
    public List getAllEvents(){
        System.out.println("<<<<<<getAllEvents");
        return messageService.getAllEvents();
    }

//    @PostMapping("postNewEvent")
//    public void postNewEvent(@RequestBody PreviewEventDTO prevEventDTO) {
//        System.out.println("<<<<<<postNewEvent event:"+ prevEventDTO);
//        messageService.postNewEvent(prevEventDTO);
//    }

    @GetMapping("getMessagesById")
    public List getMessagesById(@RequestParam("id")String id) {
        System.out.println("<<<<<<getMessagesById id="+id);
        return messageService.getMessagesById(id);
    }

    @GetMapping("getChatBySubscriberId")
    public List getChatBySubscriberId(@RequestParam("id")String id) {
        System.out.println("<<<<<<getChatBySubscriberId id="+id);
        return messageService.getChatBySubscriberId(id);
    }

    @PostMapping("postNewChat")
    public void postNewChat(@RequestBody ChatDTO chat) {
        System.out.println("<<<<<<postNewChat chat:"+chat);
        messageService.postNewChat(chat);
    }

    @GetMapping("listmessage/{from}/{to}")
    public List<Map<String,Object>> getListMessageChat(@PathVariable("from") Integer from, @PathVariable("to") Integer to){
        System.out.println("<<<<<<getChatBySubscriberId/"+from+"/"+to);
        return messageService.getListMessage(from, to);
    }

    @MessageMapping("/chat/group/{to}")
    public void sendMessageToGroup(@DestinationVariable Integer to, MessageGroupDTO message) {
        System.out.println("<<<<<<chat/group/"+to+" mes:"+message);
        messageService.sendMessageGroup(to,message);
    }

    @MessageMapping("/log/{from}")
    public void logFromApp(@DestinationVariable String from, String appLog) {
        log.debug(String.format("LOGAPP [%s] %s", from, appLog));
    }

    @GetMapping("listmessage/group/{groupid}")
    public List<Map<String,Object>> getListMessageGroupChat(@PathVariable("groupid") Integer groupid){
        System.out.println("<<<<<<listmessage/group/"+groupid);
        return messageService.getListMessageGroups(groupid);
    }

    @GetMapping("/v1/getAllEvents")
    public List<CEvent> getAllEventsV1(){
        log.debug(String.format("REST RQ <<< /v1/getAllEvents"));
        return messageService.getAllEventsV1();
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
