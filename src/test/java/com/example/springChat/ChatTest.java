package com.example.springChat;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatTest {
    private String jwtToken;
    private HttpClient httpClient;

    public ChatTest() {
        httpClient = HttpClient.newHttpClient();
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException, IOException {
        for(int i=0; i<100; i++){
            new Thread(){
                @Override
                public void run() {
                    ChatTest chatTest = new ChatTest();
                   /* try {
                        //chatTest.register();
                        //chatTest.login();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    try {
                        chatTest.httpFlood();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    super.run();
                }
            }.start();
        }



        ChatTest chatTest = new ChatTest();
        //chatTest.register();
        //chatTest.login();
        chatTest.httpFlood();





        chatTest.register();


        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    ChatTest chatTest = new ChatTest();
                    chatTest.login();
                    chatTest.getUserId("nwm1");
                    chatTest.searchName("nw");
                    //chatTest.joinAndSend();
                }catch (Exception e){}

            }
        }.start();


       /* new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    ChatTest chatTest = new ChatTest();
                    chatTest.login();
                    chatTest.joinAndSend();
                }catch (Exception e){}

            }
        }.start();*/
        /*ChatTest chatTest = new ChatTest();
        chatTest.register();
        chatTest.login();
        chatTest.joinAndSend();*/
    }

    public void joinAndSend() throws URISyntaxException, InterruptedException {
       createClient();

    }

    public void httpFlood() throws IOException, URISyntaxException, InterruptedException {
        for(int i=0; i<100; i++){
            //URL url = new URL("http://localhost:8080/auth/register?name=nwm"+i+"&email=nwm"+i+"&password=1234");
            //URL url = new URL("http://localhost:8080/auth/login?name=nwm1999&password=12345");
            //URL url = new URL("http://localhost:8080/auth/login?name=nwm1&password=1234");
            URL url = new URL("http://localhost:8080/test");
            //URL url = new URL("http://localhost:8080/searchUser?name=nwm1");
            HttpRequest request = HttpRequest.newBuilder()
                    //.header("token", this.jwtToken)
                    .uri(url.toURI()).build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            if(i%50==0){
                System.out.println(i);
            }
        }

    }

    public void login() throws IOException {
        URL url = new URL("http://localhost:8080/auth/login?name=nwm1&password=1234");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //this.jwtToken = con.getHeaderField("token");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        this.jwtToken = content.toString();
        //System.out.println(this.jwtToken);



    }

    public void getUserId(String name) throws IOException, URISyntaxException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URL url = new URL("http://localhost:8080/searchUserId?name="+name);
        HttpRequest request = HttpRequest.newBuilder()
                .header("token", jwtToken)
                .uri(url.toURI()).build();
        //HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //con.setRequestMethod("GET");
        //this.jwtToken = con.getHeaderField("token");
        System.out.println(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body());

    }

    public void searchName(String name) throws IOException, URISyntaxException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URL url = new URL("http://localhost:8080/searchUser?name="+name);
        HttpRequest request = HttpRequest.newBuilder()
                .header("token", jwtToken)
                .uri(url.toURI()).build();
        //HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //con.setRequestMethod("GET");
        //this.jwtToken = con.getHeaderField("token");
        System.out.println(httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }

    public void register() throws IOException {
        URL url = new URL("http://localhost:8080/auth/register?name=nwm1&email=nwm1&password=1234");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.getContent();
    }

    public void createClient() throws URISyntaxException, InterruptedException {
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:8080/chat"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                send("c0000000001");
                send("l0000000001"+jwtToken);
                send("j0000000001");
                send("a00000000013");
                send("u0000000001");
                /*for(int i=0; i<3; i++){
                    send("s0000000001sendAndReceive");
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                /*for(int i=0; i<1; i++){
                    send("d0000000001");
                }
                send("d0000000001");*/
                System.out.println("done");
                //send("u0000000001");

            }

            @Override
            public void onMessage(String message) {
                System.out.print(message);
                if(!message.equals("")){
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
        webSocketClient.addHeader("token", jwtToken);
        webSocketClient.connectBlocking();
        while (webSocketClient.isOpen()){
            Thread.sleep(1);
        }
    }
}
