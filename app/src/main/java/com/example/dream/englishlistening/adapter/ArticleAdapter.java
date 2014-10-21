package com.example.dream.englishlistening.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        ArticleViewHolder holder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.article_view, null);
            holder = new ArticleViewHolder();
            holder.thumbnail = (ImageView) view.findViewById(R.id.smallThumbnail);
            holder.content = (TextView) view.findViewById(R.id.titleArticle);
            view.setTag(holder);
        } else {
            holder = (ArticleViewHolder) view.getTag();
        }
        Article article = (Article) getItem(position);
        holder.content.setText(article.getTitle());
        ImageLoader.getInstance().displayImage(article.getSmallThumbnail(), holder.thumbnail);
        return view;
    }

    static class ArticleViewHolder {
        ImageView thumbnail;
        TextView content;

    }
}
