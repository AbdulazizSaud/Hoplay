package com.hoplay.kay.hoplay.FirebaseControllers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.hoplay.kay.hoplay.Models.ChatMessage;

/**
 * Created by BOXTECH on 9/14/2017.
 */

public class MessagePack {


    public MessagePack(DatabaseReference refMessages, String messageText, String username, long currentCounter)
    {

        String messageKey = refMessages.push().getKey();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(messageKey);
        chatMessage.setMessage(messageText);
        chatMessage.setUsername(username);
        chatMessage.setTimestamp(ServerValue.TIMESTAMP);
        chatMessage.setCounter(currentCounter);

        DatabaseReference messageRef = refMessages.child("_packets_").child(messageKey);
        messageRef.setValue(chatMessage);
        DatabaseReference lastMessageRef = refMessages.child("_last_message_");
        lastMessageRef.setValue(chatMessage);

    }

    public MessagePack(DatabaseReference refMessages, String messageText, String username)
    {

        String messageKey = refMessages.push().getKey();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(messageKey);
        chatMessage.setMessage(messageText);
        chatMessage.setUsername(username);
        chatMessage.setTimestamp(ServerValue.TIMESTAMP);
        chatMessage.setCounter(0);

        DatabaseReference messageRef = refMessages.child("_packets_").child(messageKey);
        messageRef.setValue(chatMessage);


    }
}
