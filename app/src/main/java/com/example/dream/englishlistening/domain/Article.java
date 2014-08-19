package com.example.dream.englishlistening.domain;

import java.io.Serializable;

/**
 * Created by bcthuan07 on 8/3/2014.
 */
public class Article implements Serializable {

    private String title;
    private String link;
    private String content;
    private String smallThumbnail;
    private String largeThumbnail;
    private String date;
    private String sound;
    private String thumbnailCaption;

    public Article(String title, String link, String content, String smallThumbnail, String largeThumbnail, String date, String sound, String thumbnailCaption) {
        this.title = title;
        this.link = link;
        this.content = content;
        this.smallThumbnail = smallThumbnail;
        this.largeThumbnail = largeThumbnail;
        this.date = date;
        this.sound = sound;
        this.thumbnailCaption = thumbnailCaption;
    }

    public Article() {
        this("", "", "", "", "", "", "", "");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getLargeThumbnail() {
        return largeThumbnail;
    }

    public void setLargeThumbnail(String largeThumbnail) {
        this.largeThumbnail = largeThumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getThumbnailCaption() {
        return thumbnailCaption;
    }

    public void setThumbnailCaption(String thumbnailCaption) {
        this.thumbnailCaption = thumbnailCaption;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", content='" + content + '\'' +
                ", smallThumbnail='" + smallThumbnail + '\'' +
                ", largeThumbnail='" + largeThumbnail + '\'' +
                ", date='" + date + '\'' +
                ", sound='" + sound + '\'' +
                ", thumbnailCaption='" + thumbnailCaption + '\'' +
                '}';
    }
}
