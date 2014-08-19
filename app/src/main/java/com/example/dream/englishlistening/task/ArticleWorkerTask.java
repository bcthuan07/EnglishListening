package com.example.dream.englishlistening.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dream.englishlistening.database.FeedReaderDbHelper;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.util.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/3/2014.
 */
public class ArticleWorkerTask extends AsyncTask<String, Void, ArrayList<Article>> {

    public static final String DATABASE = "database_article";
    public static final String ONLINE = "online_article";
    private Activity activity;
    private OnTaskComplete handler;

    public ArticleWorkerTask(Activity activity, OnTaskComplete handler) {
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        String url = strings[0];
        String type = strings[1];
        ArrayList<Article> list = new ArrayList<Article>();
        Log.e("Article LInk", url);
        if (type.equals(ONLINE)) {
            list = loadArticleOnline(url);
        } else if (type.equals(DATABASE)) {
            loadArticleFromDatabase();
        }
        return list;
    }

    private ArrayList<Article> loadArticleFromDatabase() {
        return new FeedReaderDbHelper(activity).getArticles();
    }

    private ArrayList<Article> loadArticleOnline(String url) {
        ArrayList<Article> list = new ArrayList<Article>();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36")
                    .get();
            Elements elements = doc.select("div.archive_rowmm");
            for (Element e : elements) {
                Article article = new Article();
                article.setLink(Constant.LINK + e.select("h4.black > a").first().attr("href"));
                Log.e("Link article", e.select("h4.black a span.underlineLink").first() + "");
                Element element = e.select("h4.black > a").first();
                article.setTitle(element.text());
                article.setSmallThumbnail(e.select("img.imgsmall.aligned").first().attr("src"));
                list.add(article);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        Log.e("articles", articles.toString());
//        ListView listView = (ListView) activity.findViewById(R.id.listItems);
//        listView.setAdapter(new ArticleAdapter(articles, activity));
        handler.onCompleteTask(articles);
    }
}
