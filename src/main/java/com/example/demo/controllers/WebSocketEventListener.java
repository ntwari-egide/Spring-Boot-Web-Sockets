package com.example.demo.controllers;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
// managing the events like user disconnected, user connected

@Controller
public class WebSocketEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);
    @Autowired
    private SimpMessageSendingOperations sendingOperations;


    // Verifying if the user is added
    @EventListener
    public void  handleWebSocketConnectListener(final SessionConnectedEvent event){
        LOGGER.info("We have new added connection");
    }

    // verifying if the user disconnected , then send through header sessions
    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event){
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        final ChatMessage chatMessage =  new ChatMessage(MessageType.DISCONNECT,username,"Message From Ntwari");

        sendingOperations.convertAndSend("/topic/public",chatMessage);
    }
}
