package com.example.dream.englishlistening.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.adapter.ArticleAdapter;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.task.ArticleWorkerTask;
import com.example.dream.englishlistening.task.OnTaskComplete;

import java.util.ArrayList;


public class ArticleCollectionViewActivity extends Activity implements OnTaskComplete {

    public static final String DATA_LINK = "data_link";
    public static final String DATA_BUNDLE = "data_bundle_send";
    public static final String LOADED = "data_loaded";
    private ListView listView;

    private boolean loaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.item_list_view);
        listView = (ListView) findViewById(R.id.listItems);

        Bundle bundle = getIntent().getBundleExtra(DATA_BUNDLE);
        loaded = bundle.getBoolean(LOADED);
        if (loaded) {
            new ArticleWorkerTask(this, this).execute("", ArticleWorkerTask.DATABASE);
        } else {
            new ArticleWorkerTask(this, this).execute("http://learningenglish.voanews.com/archive/as-it-is/latest/3521/3521.html", ArticleWorkerTask.ONLINE);
        }
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
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompleteTask(Object data) {
        listView.setAdapter(new ArticleAdapter(((ArrayList<Article>) data), this));
    }


}
