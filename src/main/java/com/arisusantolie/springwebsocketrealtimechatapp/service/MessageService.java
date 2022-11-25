package com.arisusantolie.springwebsocketrealtimechatapp.service;

import com.arisusantolie.springwebsocketrealtimechatapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private List<ChatDTO> chats = new ArrayList<ChatDTO>();
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void sendMessage(String to, MessageDTO message) {
        jdbcTemplate.update("insert into messages (message_text,message_from,message_to,created_datetime) " +
                "values (?,?,?,current_time )",message.getMessage_text(),message.getMessage_from(),message.getMessage_to());

        var list = jdbcTemplate.queryForList("select * from chats where id_chat='" + message.getMessage_to()+"'");
        var nList = list.stream()
                .map(el-> { var dto = new ChatDTO();
                    dto.chatId = el.get("id_chat").toString();
                    dto.subscribers = el.get("subscribers").toString()
                            .replace("[","")
                            .replace("]","")
                            .split(", ");
                    return dto;
                }).collect(Collectors.toList());
        nList.forEach( chat -> {
            Arrays.stream(chat.subscribers).forEach(subscriberId -> {
                System.out.println(">>>>>>/topic/messages/"+subscriberId + " msg:"+message);
                simpMessagingTemplate.convertAndSend("/topic/messages/" + subscriberId, message);
            });
        });
    }

    public List<Map<String,Object>> getListMessage(@PathVariable("from") Integer from, @PathVariable("to") Integer to){
        System.out.println("getListMessage from:"+from + " to:"+to);
        return jdbcTemplate.queryForList("select * from messages where (message_from=? and message_to=?) " +
                "or (message_to=? and message_from=?) order by created_datetime asc",from,to,from,to);
    }


    public List<Map<String,Object>> getListMessageGroups(@PathVariable("groupid") Integer groupid){
        System.out.println("getListMessageGroups grId:" + groupid);
        return jdbcTemplate.queryForList("select gm.*,us.name as name from group_messages gm " +
                "join users us on us.id=gm.user_id " +
                "where gm.group_id=? order by created_datetime asc",groupid);
    }


    public void sendMessageGroup(Integer to, MessageGroupDTO message) {
        System.out.println("sendMessageGroup to:"+ to + " mes:"+message);
        jdbcTemplate.update("INSERT INTO `group_messages`(`group_id`, `user_id`, `messages`, `created_datetime`) " +
                "VALUES (?,?,?,current_timestamp )",to,message.getMessage_from(),message.getMessage_text());
        message.setGroupId(to);
        simpMessagingTemplate.convertAndSend("/topic/messages/group/" + to, message);

    }

    public List<MessageDTO> getMessagesById(String id) {
        var list = jdbcTemplate.queryForList("select * from messages where (message_to=?) " +
                "order by created_datetime asc",id);
        var nList = list.stream()
                .map(el-> { var dto = new MessageDTO();
                dto.setId(el.get("id").toString());
                dto.setMessage_text(el.get("message_text").toString());
                dto.setMessage_from(el.get("message_from").toString());
                dto.setMessage_to(el.get("message_to").toString());
                dto.setCreated_datetime(el.get("created_datetime").toString());
                    System.out.println(">>>>>>getMessagesById "+dto);
                return dto;
        }).collect(Collectors.toList());
        return nList;
    }

    public List getChatBySubscriberId(String id) {
        var list = jdbcTemplate.queryForList("select * from chats where subscribers like '%" + id + "%'");
        var nList = list.stream()
                .map(el-> { var dto = new ChatDTO();
                    dto.chatId = el.get("id_chat").toString();
                    dto.subscribers = el.get("subscribers").toString()
                            .replace("[","")
                            .replace("]","")
                            .split(", ");
                    System.out.println(">>>>>>getChatBySubscriberId "+dto);
                    return dto;
                }).collect(Collectors.toList());
        return nList;
    }

    public void postNewChat(ChatDTO chat) {
        jdbcTemplate.update("insert into chats (id_chat,subscribers) " +
                "values (?,?)",chat.chatId , Arrays.toString(chat.subscribers));
    }

    public void sendEvent(String to, EventDTO eventDTO) {
        jdbcTemplate.update("insert into events (id, title, description, location, likes, timestampStar) " +
                "values (?,?,?,current_time )",eventDTO.id, eventDTO.title, eventDTO.description, eventDTO.location, eventDTO.likes);
//        var list = jdbcTemplate.queryForList("select * from events" );
//        var nList = list.stream()
//                .map(el-> { var dto = new EventDTO();
//                    dto.id = el.get("id").toString();
//                    dto.title = el.get("title").toString();
//                    dto.description = el.get("description").toString();
//                    dto.location = el.get("location").toString();
//                    dto.likes = el.get("likes").toString();
//                    dto.timestampStart = el.get("timestampStar").toString();
//                    return dto;
//                }).collect(Collectors.toList());
                System.out.println(">>>>>>/topic/events/"+to+" event:"+eventDTO);
                simpMessagingTemplate.convertAndSend("/topic/events/"+to, eventDTO);
    }

    public void sendPrevEvent(PreviewEventDTO pEventDTO) {
        jdbcTemplate.update("insert into prevEvents (ownerId, id, createrId, status, duration, title, body, dateStart, isPublicEvent) " +
                "values (?,?,?,?,?,?,?,?,?)", pEventDTO.ownerId, pEventDTO.id, pEventDTO.createrId, pEventDTO.status, pEventDTO.duration, pEventDTO.title, pEventDTO.body, pEventDTO.dateStart, pEventDTO.isPublicEvent);
        System.out.println(">>>>>>/topic/prevEvents/1 prevEvent:" + pEventDTO);
        simpMessagingTemplate.convertAndSend("/topic/prevEvents/1", pEventDTO);
    }

    public List<PreviewEventDTO> getAllEvents() {
        var list = jdbcTemplate.queryForList("select * from prevEvents" );
        var nList = list.stream()
                .map(el-> { var dto = new PreviewEventDTO();
                    dto.id = el.get("id").toString();
                    dto.ownerId = el.get("ownerId").toString();
                    dto.title = el.get("title").toString();
                    dto.createrId = el.get("createrId").toString();
                    dto.status = el.get("status").toString();
                    dto.duration = el.get("duration").toString();
                    dto.body = el.get("body").toString();
                    dto.dateStart = el.get("dateStart").toString();
                    dto.isPublicEvent = el.get("isPublicEvent").toString();
                    System.out.println(">>>>>>getAllEvents event:"+dto);
                    return dto;
                }).collect(Collectors.toList());
        return nList;
    }

    public void postNewEvent(PreviewEventDTO pEventDTO) {
        jdbcTemplate.update("insert into prevEvents (ownerId, id, createrId, status, duration, title, body, dateStart, isPublicEvent) " +
                "values (?,?,?,?,?,?,?,?,?)", pEventDTO.ownerId, pEventDTO.id, pEventDTO.createrId, pEventDTO.status, pEventDTO.duration, pEventDTO.title, pEventDTO.body, pEventDTO.dateStart, pEventDTO.isPublicEvent);
        System.out.println(">>>>>>/topic/prevEvents/1 prevEvent:" + pEventDTO);
        simpMessagingTemplate.convertAndSend("/topic/prevEvents/1", pEventDTO);
    }
}
