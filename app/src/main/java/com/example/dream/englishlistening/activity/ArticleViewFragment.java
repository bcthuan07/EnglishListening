package com.example.dream.englishlistening.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dream.englishlistening.R;
import com.example.dream.englishlistening.domain.Article;
import com.example.dream.englishlistening.task.DownloadTask;
import com.example.dream.englishlistening.task.SingleArticleWorkTask;
import com.example.dream.englishlistening.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by bcthuan07 on 8/6/2014.
 */
public class ArticleViewFragment extends Fragment implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SingleArticleWorkTask.ArticleLoaded {

    private MediaPlayer mediaPlayer;
    private ImageButton playBtn, pauseBtn;
    private SeekBar progressSb;
    private ImageView thumbnail;
    private TextView content, timeLeft, timeTotal, thumbnailCaption;
    private ProgressBar loadSoundPg, loadArticle, downloadProgessBar;
    private Article article;
    private int currentPositionMedia = 0;
    private boolean isRunning, downloaded;
    private Button saveArticleBtn, backBtn;
    private OnBack listener;
    private TimerTask updateTime;

    public void download(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnBack) {
            listener = (OnBack) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement ArticleViewFragment.OnBack");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_detail_view, null);
        mediaPlayer = new MediaPlayer();
        initComponent(view);

        updateTime = new TimerTask() {
            @Override
            public void run() {
                timeLeft.setText(Util.convertTime(mediaPlayer.getCurrentPosition()));
            }
        };

        addListener();
//        articleLoaded();
        return view;
    }

    private void initComponent(View view) {
        backBtn = (Button) view.findViewById(R.id.backBtn);
        playBtn = (ImageButton) view.findViewById(R.id.playBtn);
        thumbnail = (ImageView) view.findViewById(R.id.largeThumbnailDetail);
        content = (TextView) view.findViewById(R.id.contentDetail);
        progressSb = (SeekBar) view.findViewById(R.id.seekBarLength);
        loadSoundPg = (ProgressBar) view.findViewById(R.id.loadSoundPg);
        downloadProgessBar = (ProgressBar) view.findViewById(R.id.downloadProgressBar);
        pauseBtn = (ImageButton) view.findViewById(R.id.pauseBtn);
        loadArticle = (ProgressBar) view.findViewById(R.id.articleLoadPb);
        timeTotal = (TextView) view.findViewById(R.id.timeTotal);
        timeLeft = (TextView) view.findViewById(R.id.timeLeft);

        saveArticleBtn = (Button) view.findViewById(R.id.saveArticleBtn);
        if (!downloaded) {
            new SingleArticleWorkTask(this).execute(this.article.getLink());
            saveArticleBtn.setVisibility(View.VISIBLE);
        } else {
            articleLoaded();
        }
        thumbnailCaption = (TextView) view.findViewById(R.id.thumbnailCaption);
    }

    /**
     * Save article
     */
    private void saveArticle() {
        saveArticleBtn.setVisibility(View.GONE);
        downloadProgessBar.setVisibility(View.VISIBLE);
        new DownloadTask(getActivity()).execute(article);
    }

    /**
     * Add listener for component
     */
    private void addListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBack();
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
                    updateTime.run();
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
                    updateTime.run();
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
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
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
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.pauseBtn.setVisibility(View.GONE);
        this.playBtn.setVisibility(View.VISIBLE);
        mediaPlayer.seekTo(0);
        this.timeLeft.setText("00:00");
    }

    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {
        if (!mediaPlayer.isPlaying()) {
            progressSb.setVisibility(View.INVISIBLE);
            timeTotal.setText(Util.convertTime(mediaPlayer.getDuration()));
            mediaPlayer.start();
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            progressSb.setVisibility(View.VISIBLE);
            progressSb.setMax(mediaPlayer.getDuration());
            isRunning = true;
//            thread = new Thread(run);

//            thread.start();

            TimerTask play = new TimerTask() {
                @Override
                public void run() {
                    int total = mediaPlayer.getDuration();
                    currentPositionMedia = mediaPlayer.getCurrentPosition();
                    while (true) {
                        while (mediaPlayer != null && currentPositionMedia < total && !Thread.interrupted() && isRunning) {
                            try {
                                Thread.sleep(1000);
                                currentPositionMedia = mediaPlayer.getCurrentPosition();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            progressSb.setProgress(currentPositionMedia);
                            timeLeft.setText(Util.convertTime(currentPositionMedia));
                        }
                    }
                }
            };
            play.run();

        } else {

        }
    }

    @Override
    public void getArticle(Article article) {
        this.article = article;
        articleLoaded();
    }

    public interface OnBack {
        void onBack();
    }
}
