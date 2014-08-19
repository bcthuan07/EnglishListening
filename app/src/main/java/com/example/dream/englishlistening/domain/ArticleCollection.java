package com.example.dream.englishlistening.domain;

/**
 * Created by bcthuan07 on 8/3/2014.
 */
public class ArticleCollection {
    private String title;
    private String content;
    private String imageThumbnail;
    private String link;

    public ArticleCollection(String title, String content, String imageThumbnail, String link) {
        this.title = title;
        this.content = content;
        this.imageThumbnail = imageThumbnail;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }
}
