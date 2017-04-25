package com.example.kay.hoplay.Models;

import java.util.List;
import java.util.Map;

/**
 * Created by Kay on 4/25/2017.
 */

public class RequestModel {

    private String requestId ;
    private String requestTitle ;
    private String admin ;
    private String description ;
    private String region ;

    private List<String> users;


    public RequestModel(String requestId, String requestTitle, String admin, String description, String region) {
        this.requestId = requestId;
        this.requestTitle = requestTitle;
        this.admin = admin;
        this.description = description;
        this.region = region;

    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public String getAdmin() {
        return admin;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }





    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegion(String region) {
        this.region = region;
    }



}
