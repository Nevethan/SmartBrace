package com.example.nevethan.smartbrace;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CustomList extends ArrayAdapter<String>{

    //private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public CustomList(Context context, String[] web, Integer[] imageId) {
        super(context, R.layout.customlist, web);
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View row = layoutInflater.inflate(R.layout.customlist, parent, false);


        TextView title = (TextView) row.findViewById(R.id.textView6);

        ImageView imgView = (ImageView) row.findViewById(R.id.img);
        title.setText(web[position]);

        imgView.setImageResource(imageId[position]);
        return row;
    }
}
