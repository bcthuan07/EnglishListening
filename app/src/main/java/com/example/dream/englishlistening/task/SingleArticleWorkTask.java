package com.example.dream.englishlistening.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dream.englishlistening.domain.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by bcthuan07 on 8/4/2014.
 */
public class SingleArticleWorkTask extends AsyncTask<String, Void, Article> {

    private OnTaskComplete onTaskComplete;

    public SingleArticleWorkTask(OnTaskComplete onTaskComplete) {
        this.onTaskComplete = onTaskComplete;
    }

    @Override
    protected Article doInBackground(String... strings) {
        Article article = new Article();
        String url = strings[0];
        Log.e("detail url", url);
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
            Element largetThumbE = doc.select("div.watermark > a > img.photo").first();
            String largeThumbnail = largetThumbE == null ? "" : largetThumbE.attr("src");
            String date = doc.select("div.dateblock p.article_date").text();
            String content = "";
            String thumbnailCaption = largeThumbnail.equals("") ? "" : doc.select("span.imageCaption").first().text();
            Elements elements = doc.select("div.articleContent div.zoomMe p");
            for (int i = 0; i < elements.size() - 2; i++) {
                if (elements.get(i).text().isEmpty() || elements.get(i).text().contains("_____")) {
                    break;
                }
                content += "   " + elements.get(i).text() + "\n";
            }
            Log.e("ARTICLE CONTENT", content);
            String sound = doc.select("div.zoomMe > ul li.downloadlinkstatic > a").first().attr("href");

            article.setThumbnailCaption(thumbnailCaption);
            article.setContent(content);
            article.setDate(date);
            article.setLargeThumbnail(largeThumbnail);
            Log.e("Sound URL", sound);
            article.setSound(sound);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }

    @Override
    protected void onPostExecute(Article article) {
        super.onPostExecute(article);
        onTaskComplete.onCompleteTask(article);
    }
}
