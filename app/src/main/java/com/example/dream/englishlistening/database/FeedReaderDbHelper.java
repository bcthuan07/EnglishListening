package com.example.dream.englishlistening.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dream.englishlistening.domain.Article;

import java.util.ArrayList;

/**
 * Created by bcthuan07 on 8/4/2014.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Article.db";
    public static final String[] projection = {
            ArticleEntry.COLUMN_NAME_TITLE,
            ArticleEntry.COLUMN_NAME_THUMBNAIL_CAPTION,
            ArticleEntry.COLUMN_NAME_THUMBNAIL_LARGE,
            ArticleEntry.COLUMN_NAME_CONTENT,
            ArticleEntry.COLUMN_NAME_THUMBNAIL_SMALL,
            ArticleEntry.COLUMN_NAME_SOUND_URL
    };
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                    ArticleEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ArticleEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_SOUND_URL + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_THUMBNAIL_SMALL + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_THUMBNAIL_LARGE + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_THUMBNAIL_CAPTION + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    ArticleEntry.COLUMN_NAME_NULLABLE + TEXT_TYPE
                    + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Article> getArticles() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Article> list = new ArrayList<Article>();


        Cursor cursor = db.query(ArticleEntry.TABLE_NAME, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(new Article(cursor.getString(1), "", cursor.getString(4), cursor.getString(5), cursor.getString(3),
                    "", cursor.getString(6), cursor.getString(2)));
        }
        cursor.close();
        return list;
    }

    public Article saveArticle(Article article) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ArticleEntry.COLUMN_NAME_CONTENT, article.getContent());
        values.put(ArticleEntry.COLUMN_NAME_DATE, article.getDate());
        values.put(ArticleEntry.COLUMN_NAME_SOUND_URL, article.getSound());
        values.put(ArticleEntry.COLUMN_NAME_THUMBNAIL_CAPTION, article.getThumbnailCaption());
        values.put(ArticleEntry.COLUMN_NAME_THUMBNAIL_LARGE, article.getLargeThumbnail());
        values.put(ArticleEntry.COLUMN_NAME_THUMBNAIL_SMALL, article.getSmallThumbnail());
        values.put(ArticleEntry.COLUMN_NAME_TITLE, article.getTitle());

        long newRowId;
        newRowId = db.insert(ArticleEntry.TABLE_NAME, ArticleEntry.COLUMN_NAME_NULLABLE, values);
        if (newRowId == -1) {
            Log.e("DATABASE", "SAVED");
        } else {
            Log.e("DATABASE", "NOT SAVE");
        }
        Cursor cur = db.query(ArticleEntry.TABLE_NAME, projection, ArticleEntry.COLUMN_NAME_TITLE + " = '" + article.getTitle() + "'", null, null, null, null);
        cur.moveToFirst();
        Article ret = toArticle(cur);
        return ret;
    }

    private Article toArticle(Cursor cur) {
        Article article = new Article();
        article.setTitle(cur.getString(0));
        article.setThumbnailCaption(cur.getString(1));
        article.setLargeThumbnail(cur.getString(2));
        article.setContent(cur.getString(3));
        article.setSmallThumbnail(cur.getString(4));
        article.setSound(cur.getString(5));
        return article;
    }
}
