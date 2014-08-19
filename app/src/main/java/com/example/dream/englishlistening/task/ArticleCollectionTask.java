package com.example.dream.englishlistening.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dream.englishlistening.domain.ArticleCollection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/3/2014.
 */
public class ArticleCollectionTask extends AsyncTask<String, Void, ArrayList<ArticleCollection>> {

    private OnTaskComplete task;

    public ArticleCollectionTask(OnTaskComplete task) {
        this.task = task;
    }

    @Override
    protected ArrayList<ArticleCollection> doInBackground(String... strings) {
        ArrayList<ArticleCollection> list = new ArrayList<ArticleCollection>();
        String url = strings[0];
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36")
                    .get();
            Elements elements = doc.select("div.programlist_row");
            for (Element e : elements) {
                if (e.select("div.programlist_rowimagedesc h4 a").first() != null) {
                    String title = e.select("div.programlist_rowimagedesc h4 a").first().text();
                    String content = e.select("div.programlist_rowimagedesc p").text();
                    String imageThumbnail = e.select("a img.programlist_rowimage").attr("src").toString();
                    String link = e.select("div.media_links > ul > li.playlistlink > a.linksmall.listico").attr("href").toString();
                    list.add(new ArticleCollection(title, content, imageThumbnail, link));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            Log.e("Get Collection", "Cannot get content");
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<ArticleCollection> articleCollections) {
        task.onCompleteTask(articleCollections);

    }

}
