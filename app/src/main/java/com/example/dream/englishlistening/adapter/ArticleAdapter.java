package com.example.dream.englishlistening.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.domain.Article;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/4/2014.
 */
public class ArticleAdapter extends BaseAdapter {

    private ArrayList<Article> list;
    private Activity activity;

    public ArticleAdapter(ArrayList<Article> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.article_view, null);
        }
        ImageView thumbnail = (ImageView) view.findViewById(R.id.smallThumbnail);
//        TextView content = (TextView) view.findViewById(R.id.content);
        Article article = (Article) getItem(position);
        ImageLoader.getInstance().displayImage(article.getSmallThumbnail(), thumbnail);
//        content.setText(article.getTitle());
        return view;
    }
}
