package com.example.dream.englishlistening.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.database.FeedReaderDbHelper;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.task.DownloadTask;
import com.example.dream.englishlistening.task.OnTaskComplete;
import com.example.dream.englishlistening.task.SingleArticleWorkTask;
import com.example.dream.englishlistening.util.Constant;
import com.example.dream.englishlistening.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by bcthuan07 on 8/6/2014.
 */
public class ArticleViewActivity extends Activity implements OnTaskComplete, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {

    public static final String LINK = "link_article";
    public static final String DATA = "data_article";
    public static final String DOWNLOADED = "sound_downloaded";
    public static final String ARTICLE = "article_downloaded";
    public static final String ARTICLE_SMALL_THUMBNAIL = "article_small_thumbnail";
    public static final String ARTICLE_TITLE = "article_title";
    private MediaPlayer mediaPlayer;
    private ImageButton playBtn, pauseBtn;
    private SeekBar progressSb;
    private ImageView thumbnail;
    private TextView content, timeLeft, timeTotal, thumbnailCaption;
    private ProgressBar loadSoundPg, loadArticle;
    private Article article;
    private Thread thread;
    private int currentPositionMedia = 0;
    private Runnable run;
    private boolean isRunning, downloaded;
    private Button saveArticleBtn, backBtn;
    private String smallThumbnailLink;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_view);
        getActionBar().hide();
        mediaPlayer = new MediaPlayer();

        Bundle bundle = getIntent().getBundleExtra(DATA);
        downloaded = bundle.getBoolean(DOWNLOADED);
        smallThumbnailLink = bundle.getString(ARTICLE_SMALL_THUMBNAIL);
        title = bundle.getString(ARTICLE_TITLE);
        if (!downloaded) {
            new SingleArticleWorkTask(this).execute(bundle.getString(LINK), String.valueOf(downloaded));
        } else {
            this.article = (Article) bundle.getSerializable(ARTICLE);
            articleLoaded();
        }
        initComponent();
        run = new Runnable() {
            @Override
            public void run() {
                int total = mediaPlayer.getDuration();
                currentPositionMedia = mediaPlayer.getCurrentPosition();
                while (true)
                    while (mediaPlayer != null && currentPositionMedia < total && !Thread.interrupted() && isRunning) {
                        try {
                            Thread.sleep(1000);
                            currentPositionMedia = mediaPlayer.getCurrentPosition();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                        }
                        progressSb.setProgress(currentPositionMedia);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeLeft.setText(Util.convertTime(currentPositionMedia));
                            }
                        });
                    }
            }
        };
        addListener();

    }


    private void initComponent() {
        backBtn = (Button) findViewById(R.id.backBtn);
        playBtn = (ImageButton) findViewById(R.id.playBtn);
        thumbnail = (ImageView) findViewById(R.id.largeThumbnailDetail);
        content = (TextView) findViewById(R.id.contentDetail);
        progressSb = (SeekBar) findViewById(R.id.seekBarLength);
        loadSoundPg = (ProgressBar) findViewById(R.id.loadSoundPg);
        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        loadArticle = (ProgressBar) findViewById(R.id.articleLoadPb);
        timeTotal = (TextView) findViewById(R.id.timeTotal);
        timeLeft = (TextView) findViewById(R.id.timeLeft);

        saveArticleBtn = (Button) findViewById(R.id.saveArticleBtn);
        if (!downloaded) {
            saveArticleBtn.setVisibility(View.VISIBLE);
        }


        thumbnailCaption = (TextView) findViewById(R.id.thumbnailCaption);


    }

    /**
     * Save article
     */
    private void saveArticle() {

        Log.e("DOWNLOAD LINK", article.getSound());
        Log.e("DOWNLOAD LINK", article.getLargeThumbnail());
        Log.e("DOWNLOAD LINK", article.getSmallThumbnail());
        new DownloadTask(getApplicationContext(), this).execute(this.article.getSound(), Constant.TYPE_SOUND, this.article.getTitle() + ".mp3");
        new DownloadTask(getApplicationContext(), this).execute(this.article.getLargeThumbnail(), Constant.TYPE_IMAGE, this.article.getTitle() + "-small.jpg");
        new DownloadTask(getApplicationContext(), this).execute(this.article.getSmallThumbnail(), Constant.TYPE_IMAGE, this.article.getTitle() + "-large.jpg");

        //Luu xuong co so du lieu
        new FeedReaderDbHelper(this).saveArticle(article);
    }


    /**
     * Add listener for component
     */
    private void addListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPositionMedia == 0) {
                    playSound(article.getSound());
                } else {
                    mediaPlayer.seekTo(currentPositionMedia);
                    mediaPlayer.start();
                }
                playBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
                timeLeft.setVisibility(View.VISIBLE);
                timeTotal.setVisibility(View.VISIBLE);
                isRunning = true;
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                pauseBtn.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
                isRunning = false;
            }
        });

        progressSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i >= mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                    pauseBtn.setVisibility(View.GONE);
                    playBtn.setVisibility(View.VISIBLE);
                    return;
                }
                if (null != mediaPlayer && b) {
                    mediaPlayer.seekTo(i);
                    currentPositionMedia = mediaPlayer.getCurrentPosition();
                    timeLeft.setText(Util.convertTime(currentPositionMedia));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress >= mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                    pauseBtn.setVisibility(View.GONE);
                    playBtn.setVisibility(View.VISIBLE);
                    return;
                }
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    currentPositionMedia = mediaPlayer.getCurrentPosition();
                    timeLeft.setText(Util.convertTime(currentPositionMedia));
                }
            }
        });
        saveArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveArticle();
            }
        });
    }

    private void playSound(String link) {
        mediaPlayer.reset();
        if (!downloaded)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(link);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            if (downloaded)
                mediaPlayer.prepare();
            else
                mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onCompleteTask(Object data) {
        Log.v("COMPLETE LOAD ARTICLE", "LOADED");
        if (data instanceof Article) {
            this.article = (Article) data;
            this.article.setSmallThumbnail(smallThumbnailLink);
            this.article.setTitle(title);

            articleLoaded();
        } else if (data instanceof File) {
            if (this.article != null) {
                File file = (File) data;
                if (file.getPath().endsWith(".mp3")) {
                    this.article.setSound(file.getPath());
                } else if (file.getPath().endsWith("-large.jpg")) {
                    this.article.setLargeThumbnail(file.getPath());
                } else if (file.getPath().endsWith("-small.jpg")) {
                    this.article.setSmallThumbnail(file.getPath());
                }
            }
        }

    }

    private void articleLoaded() {
        ImageLoader.getInstance().displayImage(article.getLargeThumbnail(), thumbnail);
        this.content.setText(article.getContent());
        this.thumbnailCaption.setText(article.getThumbnailCaption());
        this.loadArticle.setVisibility(View.GONE);
        this.thumbnail.setVisibility(View.VISIBLE);
        this.content.setVisibility(View.VISIBLE);
        this.thumbnailCaption.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.pauseBtn.setVisibility(View.GONE);
        this.playBtn.setVisibility(View.VISIBLE);
        mediaPlayer.seekTo(0);
        this.timeLeft.setText("00:00");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (!mediaPlayer.isPlaying()) {
            Log.e("Media Player Duration", String.valueOf(mediaPlayer.getDuration()));
            progressSb.setVisibility(View.INVISIBLE);
            timeTotal.setText(Util.convertTime(mediaPlayer.getDuration()));
            mediaPlayer.start();
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            progressSb.setVisibility(View.VISIBLE);
            progressSb.setMax(mediaPlayer.getDuration());
            isRunning = true;
            thread = new Thread(run);
            thread.start();
        } else {

        }
    }
}
