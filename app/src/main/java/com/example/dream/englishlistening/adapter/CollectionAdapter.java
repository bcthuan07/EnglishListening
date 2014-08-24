package com.example.dream.englishlistening.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.domain.ArticleCollection;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/18/2014.
 */
public class CollectionAdapter extends BaseAdapter {

    private ArrayList<ArticleCollection> list;
    private Activity activity;

    public CollectionAdapter(ArrayList<ArticleCollection> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.article_view, null);
        }

        ImageView thumbnail = (ImageView) view.findViewById(R.id.smallThumbnail);
//        TextView content = (TextView) view.findViewById(R.id.content);
        ArticleCollection articleCollection = (ArticleCollection) getItem(i);
        ImageLoader.getInstance().displayImage(articleCollection.getImageThumbnail(), thumbnail);
//        content.setText(articleCollection.getTitle());
        return view;
    }
}
