package com.example.mp3freeforyou.Model;

import android.media.MediaPlayer;

import java.io.IOException;

public class CustomMediaPlayer extends MediaPlayer {
    String dataSource;

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException
    {
        super.setDataSource(path);
        dataSource = path;
    }

    public String getDataSource()
    {
        return dataSource;
    }
}
