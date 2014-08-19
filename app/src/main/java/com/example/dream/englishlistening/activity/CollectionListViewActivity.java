package com.example.dream.englishlistening.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.adapter.CollectionAdapter;
import com.example.dream.englishlistening.domain.ArticleCollection;
import com.example.dream.englishlistening.task.ArticleCollectionTask;
import com.example.dream.englishlistening.task.OnTaskComplete;
import com.example.dream.englishlistening.util.Constant;

import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/17/2014.
 */
public class CollectionListViewActivity extends Activity implements OnTaskComplete {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_view);
        getActionBar().hide();
        listView = (ListView) findViewById(R.id.listItems);
        //load collection
        new ArticleCollectionTask(this).execute(Constant.COLLECTION_LINK);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArticleCollection articleCollection = (ArticleCollection) adapterView.getItemAtPosition(i);
                String link = articleCollection.getLink();
                Intent intent = new Intent(getApplicationContext(), ArticleCollectionViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ArticleCollectionViewActivity.DATA_LINK, link);
                intent.putExtra(ArticleCollectionViewActivity.DATA_BUNDLE, bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCompleteTask(Object data) {
        listView.setAdapter(new CollectionAdapter(((ArrayList<ArticleCollection>) data), this));
    }
}
