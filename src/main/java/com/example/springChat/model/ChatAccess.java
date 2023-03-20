package com.example.springChat.model;

public class ChatAccess {
    private int userId;
    public Role role;
    public enum Role{
        USER,
        OWNER,
        ADMIN
    }

    public ChatAccess(int userId, Role role) {
        this.userId = userId;
        this.role = role;
    }
}
