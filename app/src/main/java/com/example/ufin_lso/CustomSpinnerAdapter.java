package com.example.ufin_lso;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> items;
    private List<Integer> items_bgcolor;

//    public CustomSpinnerAdapter(Context context, List<String> items) {
//        super(context, 0, items);
//        this.context = context;
//        this.items = items;
//    }
    public CustomSpinnerAdapter(Context context, List<String> items, List<Integer> items_bgcolor) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.items_bgcolor = items_bgcolor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

//    private View createItemView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_custom, parent, false);
//        }
//
//        TextView textView = convertView.findViewById(R.id.spinnerItemTextView);
//        textView.setText(items.get(position));
//
//        // Set different colors based on position or item value
//        if (position == 0) {
//            textView.setTextColor(Color.RED);
//        } else if (position == 1) {
//            textView.setTextColor(Color.BLUE);
//        } else {
//            textView.setTextColor(Color.BLACK); // Default color
//        }
//
//        return convertView;
//    }
    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_custom, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinnerItemTextView);
        textView.setText(items.get(position));

//        // Set different colors based on position or item value
//        if (position == 0) {
//            textView.setTextColor(Color.RED);
//        } else if (position == 1) {
//            textView.setTextColor(Color.BLUE);
//        } else {
//            textView.setTextColor(Color.BLACK); // Default color
//        }
//        textView.setTextColor(items_color.get(position));
        textView.setBackgroundColor(items_bgcolor.get(position));

        return convertView;
    }
}
