package com.example.springChat;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Assert;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;


public class ChatTest {

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        ChatTest chatTest = new ChatTest();
        chatTest.joinAndSend();
    }

    public void joinAndSend() throws URISyntaxException, InterruptedException {
       createClient();

    }

    public void createClient() throws URISyntaxException, InterruptedException {
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("j1234567890");
                send("s1234567890sendAndReceive");
                send("s1234567890sendAndReceive");
            }

            @Override
            public void onMessage(String message) {
                System.out.println(""+message);
                //Assert.assertEquals("sendAndReceive", message);
                if(!message.equals("")){
                    //Assert.assertEquals("sendAndReceive", message);
                    //close();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println(reason);
            }

            @Override
            public void onError(Exception ex) {

            }
        };
        webSocketClient.connectBlocking();
        while (webSocketClient.isOpen()){}
    }
}
