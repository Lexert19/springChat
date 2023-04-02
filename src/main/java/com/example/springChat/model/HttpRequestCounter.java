package com.example.springChat.model;

public class HttpRequestCounter {
    private int counter;
    private int longCounter;
    private long lastRequestTime;

    public HttpRequestCounter() {
        counter = 0;
        lastRequestTime = System.currentTimeMillis();
    }

    public boolean validateRequest() {
        long now = System.currentTimeMillis();

        counter -= (now - this.lastRequestTime)/1000*10;
        if(counter<0)
            counter = 0;
        this.lastRequestTime = now;
        counter++;

        if (counter > 20)
            return false;

        return true;
    }

    public long getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(long lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public int getCounter() {
        return counter;
    }
}
