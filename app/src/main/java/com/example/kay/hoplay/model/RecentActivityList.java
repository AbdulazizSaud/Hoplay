package com.example.kay.hoplay.model;

/**
 * Created by khaledAlhindi on 11/22/2016 AD.
 */

public class RecentActivityList extends DefaultListModel{


    // here it should be a behivors something like a opponent id , my id and so long.
    private String activityDate;

    public  RecentActivityList(String gameName, String gamePhotoURL,String activityDescription, String activityDate){
        super(gameName,gamePhotoURL,activityDescription);
        this.activityDate = activityDate;
    }





    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }


}
