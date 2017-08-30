package com.hoplay.kay.hoplay.Models;

public class RequestModelReference {


    private String requestId ;
    private String platform;
    private String gameId;
    private String region ;

    public RequestModelReference()
    {

    }
    public RequestModelReference(String requestId, String gameId, String platform, String region) {
        this.requestId = requestId;
        this.platform = platform;
        this.region = region;
        this.gameId = gameId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPlatform() {
        return platform;
    }

    public String getRegion() {
        return region;
    }

    public String getGameId() {
        return gameId;
    }
}
