package com.arisusantolie.springwebsocketrealtimechatapp.service;

import com.arisusantolie.springwebsocketrealtimechatapp.dto.MessageDTO;
import com.arisusantolie.springwebsocketrealtimechatapp.dto.MessageGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void sendMessage(String to, MessageDTO message) {
        System.out.println("sendMessage msg:"+ message + " to:"+ to);
//        jdbcTemplate.update("insert into messages (message_text,message_from,message_to,created_datetime) " +
//                "values (?,?,?,current_time )",message,message,"1Dd",to);
        System.out.println(">>>>"+message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);

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
//        jdbcTemplate.update("INSERT INTO `group_messages`(`group_id`, `user_id`, `messages`, `created_datetime`) " +
//                "VALUES (?,?,?,current_timestamp )",to,message.getFromLogin(),message.getMessage());
        message.setGroupId(to);
        simpMessagingTemplate.convertAndSend("/topic/messages/group/" + to, message);

    }

    public List<Map<String,Object>> getMessagesById(String id) {
        return jdbcTemplate.queryForList("select * from messages where (message_to=?) " +
                "order by created_datetime asc",id);
    }
}
