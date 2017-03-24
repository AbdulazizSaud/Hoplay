package com.example.kay.hoplay.Models;

/**
 * Created by khaledAlhindi on 11/23/2016 AD.
 */

public class SavedRequestsList extends DefaultListModel {

    // here it should be a behivors something like a opponent id , my id and so long.
   private String gameName,gamePhotoURL,requestDescription,numberOfPlayers;

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }



    public SavedRequestsList(String gameName, String gamePhotoURL,String requestDescription, String numberOfPlayers) {
        super(gameName,gamePhotoURL,requestDescription);

        this.numberOfPlayers = numberOfPlayers;
    }
}
