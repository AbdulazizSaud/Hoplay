package com.example.kay.hoplay.PatternStrategyComponents;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by azoz-pc on 1/25/2017.
 */

public class PattrenContext {


    public HashMap<String, String> executeStratgy(PattrenStrategyInterface strategy){
        strategy.execute();
        return strategy.get();

    }


}
