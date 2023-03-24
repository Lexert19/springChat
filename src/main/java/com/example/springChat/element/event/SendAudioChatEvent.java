package com.example.springChat.element.event;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SendAudioChatEvent {
    public final static char ID = 'v';
    private DataBuffer audio;
    private WebSocketSession session;
    private int address;

    public SendAudioChatEvent(DataBuffer dataBuffer, WebSocketSession session) {
        this.address = Integer.parseInt(dataBuffer.toString(1,10,Charset.defaultCharset()));
        audio = dataBuffer;
        audio = dataBuffer.readPosition(11);
        this.session = session;
        //super(message, session);
        //audio = message.substring(11).getBytes(StandardCharsets.US_ASCII);
    }

    public DataBuffer getAudio() {
        return audio;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public int getAddress() {
        return address;
    }
}
