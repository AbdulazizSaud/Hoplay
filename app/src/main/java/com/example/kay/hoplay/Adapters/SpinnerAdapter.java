package com.example.kay.hoplay.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kay.hoplay.App.App;
import com.example.kay.hoplay.R;

import java.util.List;

/**
 * Created by khaledAlhindi on 12/2/2016 AD.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    final Typeface playregular = Typeface.createFromAsset(App.getInstance().getAssets(), "playregular.ttf");

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setTypeface(playregular);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(16);
        v.setTextColor(ContextCompat.getColor(getContext(), R.color.hint_color));
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getDropDownView(position, convertView, parent);
        v.setTypeface(playregular);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(16);
        v.setTextColor(ContextCompat.getColor(getContext(),R.color.hint_color));
        return v;
    }
}