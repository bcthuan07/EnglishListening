package com.example.dream.englishlistening.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.database.FeedReaderDbHelper;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.util.Util;

import java.io.File;

/**
 * Created by bcthuan07 on 8/12/2014.
 */
public class DownloadTask extends AsyncTask<Article, Void, Void> {

    private Activity context;

    public DownloadTask(Activity context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Article... articles) {
        Article article = articles[0];
        checkFileDir("image");
        checkFileDir("sound");
        Util.downloadFile(article.getSound(), context.getFilesDir().getPath() + "/sound/" + article.getTitle().trim() + ".mp3");
        Util.downloadFile(article.getLargeThumbnail(), context.getFilesDir().getPath() + "/image/" + article.getTitle().trim() + "-large.jpg");
        Util.downloadFile(article.getSmallThumbnail(), context.getFilesDir().getPath() + "/image/" + article.getTitle().trim() + "-small.jpg");
        article.setSound(context.getFilesDir().getPath() + "/sound/" + article.getTitle().trim() + ".mp3");
        article.setSmallThumbnail(context.getFilesDir().getPath() + "/image/" + article.getTitle().trim() + "-small.jpg");
        article.setLargeThumbnail(context.getFilesDir().getPath() + "/image/" + article.getTitle().trim() + "-large.jpg");
        new FeedReaderDbHelper(context).saveArticle(article);
        return null;
    }

    private void checkFileDir(String type) {
        File file = new File(context.getFilesDir().getPath() + "/" + type);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((ProgressBar) context.findViewById(R.id.downloadProgressBar)).setVisibility(View.GONE);
    }
}
