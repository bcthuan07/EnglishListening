package com.example.dream.englishlistening.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.dream.englishlistening.activity.ArticleViewActivity;
import com.example.dream.englishlistening.util.HttpRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by bcthuan07 on 8/12/2014.
 */
public class DownloadTask extends AsyncTask<String, Void, File> {

    private Context context;
    private OnTaskComplete task;

    public DownloadTask(Context context, OnTaskComplete task) {
        this.context = context;
        this.task = task;
    }

    @Override
    protected File doInBackground(String... strings) {

        String url = strings[0];
        String type = strings[1];
        String name = strings[2];
        File file = null;
        checkFileDir(type);
        try {
            file = new File(context.getFilesDir().getPath() + "/" + type + "/" + name);
            HttpRequest request = HttpRequest.get(url);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            request.receive(out);
            out.flush();
            out.close();
            return file;
        } catch (HttpRequest.HttpRequestException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void checkFileDir(String type) {
        File file = new File(context.getFilesDir().getPath() + "/" + type);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    protected void onPostExecute(File file) {
        Log.e("FILE DOWNLOAD", file.getPath());
        if (file != null) {
            ArticleViewActivity.DOWNLOAD_PROGRESS++;
            task.onCompleteTask(file);
            Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(context, "Can't download", Toast.LENGTH_LONG);
        }
    }
}
