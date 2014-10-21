package com.example.dream.englishlistening.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.adapter.ArticleAdapter;
import com.example.dream.englishlistening.adapter.SelectDrawerAdapter;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.task.ArticleWorkerTask;
import com.example.dream.englishlistening.task.OnTaskComplete;
import com.example.dream.englishlistening.util.Constant;

import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/18/2014.
 */
public class MainActivity extends FragmentActivity implements OnTaskComplete, ArticleViewFragment.OnBack, ArticleCollectionViewFragment.OnArticleSelected {
    private static boolean SAVED = false;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mData;
    private ArticleViewFragment articleViewFragment;
    private ArticleCollectionViewFragment articleCollectionViewFragment;
    private ActionBarDrawerToggle barDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        mData = getResources().getStringArray(R.array.drawer_options);

        //load new articles
        articleCollectionViewFragment = new ArticleCollectionViewFragment();
        articleViewFragment = new ArticleViewFragment();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        barDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawerLayout.setDrawerListener(barDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        loadNavigation();


        new ArticleWorkerTask(this, this).execute(Constant.AUDIO_LINK, ArticleWorkerTask.ONLINE);

    }

    /**
     * load navigation
     */
    private void loadNavigation() {
        mDrawerList.setAdapter(new SelectDrawerAdapter(mData, this));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                Log.d("Item Selected", item);
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
        ArrayList<Article> list = (ArrayList<Article>) data;
        ArticleAdapter articleAdapter = new ArticleAdapter(list, this);
        articleCollectionViewFragment.setAdapterView(articleAdapter);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, articleCollectionViewFragment).addToBackStack(null).commit();
    }


    @Override
    public void onBack() {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        barDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        barDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (barDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleSelected(Article article) {
        articleViewFragment.download(SAVED);
        articleViewFragment.setArticle(article);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, articleViewFragment).addToBackStack(null).commit();
    }

}
