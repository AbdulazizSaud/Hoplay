package com.hoplay.kay.hoplay.Models;

/**
 * Created by Kay on 10/30/2016.
 */

public class  ChatMessage {
    private String id;
    private boolean isMe;
    private String message;
    private Object timestamp;
    private long counter;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public boolean isHotKeys()
    {
        return username.equals("_leave_") || username.equals("_join_") || username.equals("_kicked_");
    }
}
