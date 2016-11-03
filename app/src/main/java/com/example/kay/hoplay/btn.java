package com.example.kay.hoplay;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Kay on 9/26/2016.s
 */

public class btn extends Button {
    public btn(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.color.yellow);
        setText("hello");
    }
}
