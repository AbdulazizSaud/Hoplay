package com.example.kay.hoplay.PatternStrategyComponents;

import com.example.kay.hoplay.PatternStrategyComponents.PattrenStrategyInterface;

import java.util.HashMap;

/**
 * Created by Kay on 2/7/2017.
 */

public abstract class PattrenStrategy implements PattrenStrategyInterface {

    protected HashMap<String ,String> results;

    public PattrenStrategy(){
        results = new HashMap<String,String>();
    }
}
