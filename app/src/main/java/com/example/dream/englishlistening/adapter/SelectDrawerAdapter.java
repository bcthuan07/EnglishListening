package com.example.dream.englishlistening.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dream.englishlistening.R;

/**
 * Created by bcthuan07 on 8/18/2014.
 */
public class SelectDrawerAdapter extends BaseAdapter {

    private String[] data;
    private Activity activity;

    public SelectDrawerAdapter(String[] data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.drawer_selection, null);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.selectThumb);
        TextView textView = (TextView) view.findViewById(R.id.select);
        String select = (String) getItem(i);
        textView.setText(select);
        return view;
    }

}
