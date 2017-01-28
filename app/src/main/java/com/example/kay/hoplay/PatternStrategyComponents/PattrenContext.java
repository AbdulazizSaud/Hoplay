package com.example.kay.hoplay.PatternStrategyComponents;

import android.util.Log;

/**
 * Created by azoz-pc on 1/25/2017.
 */

public class PattrenContext {

    private PattrenStrategyInterface strategy;

    public void setStrategy(PattrenStrategyInterface strategy){
        this.strategy = strategy;
    }

    public String excute(DataCommon ... args){
        String message = strategy.excute(args);
        return  message;
    }



}
