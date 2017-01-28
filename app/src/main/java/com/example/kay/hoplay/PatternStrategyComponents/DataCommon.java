package com.example.kay.hoplay.PatternStrategyComponents;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by azoz-pc on 1/25/2017.
 */

public class DataCommon<T> {

    private T[] data;


    public DataCommon(@NonNull T... data){
        this.data = data;
    }

    public T[] getData(){
        return data;
    }


}
