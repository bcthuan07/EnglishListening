package com.example.dream.englishlistening.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.adapter.ArticleAdapter;
import com.example.dream.englishlistening.adapter.SelectDrawerAdapter;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.task.ArticleWorkerTask;
import com.example.dream.englishlistening.task.OnTaskComplete;
import com.example.dream.englishlistening.util.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/18/2014.
 */
public class MainActivity extends Activity implements OnTaskComplete {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mData = {"Collection", "Audio"};
    private GridView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_view);
        getActionBar().hide();

        //set title
        ((TextView) findViewById(R.id.titleTop)).setText("New Audio");

        //set drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        listView = (GridView) findViewById(R.id.listItems);
        loadNavigation();

        //init ImageLoader
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        //load new articles
        new ArticleWorkerTask(this, this).execute(Constant.AUDIO_LINK, ArticleWorkerTask.ONLINE);

        //add listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article article = (Article) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), ArticleViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ArticleViewActivity.LINK, article.getLink());
                bundle.putString(ArticleViewActivity.ARTICLE_SMALL_THUMBNAIL, article.getSmallThumbnail());
                bundle.putString(ArticleViewActivity.ARTICLE_TITLE, article.getTitle());
                bundle.putBoolean(ArticleViewActivity.DOWNLOADED, false);
                intent.putExtra(ArticleViewActivity.DATA, bundle);
                startActivity(intent);
            }
        });

        //hide actionbar
        getActionBar().hide();

    }

    /**
     * load navigation
     */
    private void loadNavigation() {
        mDrawerList.setAdapter(new SelectDrawerAdapter(mData, this));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String input = (String) adapterView.getItemAtPosition(i);
                Intent intent = null;
                if (input.equals(mData[0])) {
                    intent = new Intent(getApplicationContext(), CollectionListViewActivity.class);
                } else if (input.equals(mData[1])) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(ArticleCollectionViewActivity.LOADED, true);
                    intent = new Intent(getApplicationContext(), ArticleCollectionViewActivity.class);
                    intent.putExtra(ArticleCollectionViewActivity.DATA_BUNDLE, bundle);
                }
                startActivity(intent);
            }
        });
    }

    /**
     * when articles loaded
     *
     * @param data articles
     */
    @Override
    public void onCompleteTask(Object data) {
        BaseAdapter adapter = new ArticleAdapter(((ArrayList<Article>) data), this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
