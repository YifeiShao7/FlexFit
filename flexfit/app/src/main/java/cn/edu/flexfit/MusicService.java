package cn.edu.flexfit;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    public class MusicBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
    }

    public void startMusic(){
        if (mediaPlayer != null && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }
}
