package com.example.dream.englishlistening.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.domain.Article;


public class ArticleCollectionViewFragment extends Fragment {

    private ListView gridItems;
    private OnArticleSelected listener;
    private Article article;
    private BaseAdapter adapter;

    public void setListener(OnArticleSelected listener) {
        this.listener = listener;
    }


    public void setAdapterView(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnArticleSelected)) {
            throw new IllegalArgumentException("Not implement interface");
        }
        setListener((OnArticleSelected) activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.items_grid_view, container, false);
        gridItems = (ListView) view.findViewById(R.id.grid_items);
        gridItems.setAdapter(adapter);
        gridItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                article = (Article) adapterView.getItemAtPosition(i);
                listener.onArticleSelected(article);
            }
        });
        return view;
    }

    public interface OnArticleSelected {
        void onArticleSelected(Article article);
    }


}
