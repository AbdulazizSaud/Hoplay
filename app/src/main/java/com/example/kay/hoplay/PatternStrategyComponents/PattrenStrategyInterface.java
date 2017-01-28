package com.example.kay.hoplay.PatternStrategyComponents;

/**
 * Created by azoz-pc on 1/25/2017.
 */

public interface PattrenStrategyInterface {

     void sendData(DataCommon data);
     DataCommon receiveData();
     String excute(DataCommon ... args);

}
