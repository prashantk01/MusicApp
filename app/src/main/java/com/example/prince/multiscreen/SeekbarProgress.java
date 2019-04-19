package com.example.prince.multiscreen;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.prince.multiscreen.Fragments.NumbersFragment;

public class SeekbarProgress extends AppCompatActivity {
    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 4000;
    TextView song_name;
    public Handler mHandler;
    Runnable mRunnable;
    int seekbar_progress;
    String x;
    public static SeekBar seekBar;
    Button pause;
    Button next;
    Button prev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        String name_song=NumbersFragment.songlist.getMsongName();
        //   String x=name_song.substring(0,name_song.length()-4);
        song_name=(TextView) findViewById(R.id.song_name);
        song_name.setText(name_song);
        song_name.setSelected(true);

        pause=(Button)findViewById(R.id.pause);
        next=(Button)findViewById(R.id.next);
        prev=(Button)findViewById(R.id.prev);
        seekBar=(SeekBar)findViewById(R.id.song_progress);

        seekBar.setMax(MusicService.mMediaPlayer.getDuration());
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                int min, sec;
                /*Checking if the  music player is null or not otherwise it may throw an exception*/
                if (MusicService.mMediaPlayer != null ) {
                    int mCurrentPosition = seekBar.getProgress();
                    min = mCurrentPosition / 60;
                    sec = mCurrentPosition % 60;
                    Log.e("Music Player Activity", "Minutes : "+min +" Seconds : " + sec);
                                     /*currentime_mm.setText("" + min);
                                     currentime_ss.setText("" + sec);*/
                    seekBar.setProgress(MusicService.mMediaPlayer.getCurrentPosition());
                }
                mHandler.postDelayed(this,UPDATE_FREQUENCY);
            }
        };

        mHandler.postDelayed(mRunnable,0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_progress=progress;
                if(fromUser){
                    if (MusicService.mMediaPlayer!=null)
                    MusicService.mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (MusicService.mMediaPlayer!=null)
                MusicService.mMediaPlayer.seekTo(seekbar_progress);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicService.mMediaPlayer!=null) {
                    if (MusicService.mMediaPlayer.isPlaying()) {
                        pause.setBackgroundResource(R.drawable.play);
                        MusicService.mMediaPlayer.pause();
                    } else {
                        pause.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
                            MusicService.mMediaPlayer.start();
                    }
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekto = MusicService.mMediaPlayer.getCurrentPosition() + STEP_VALUE;
                if (seekto > MusicService.mMediaPlayer.getDuration())
                    seekto = MusicService.mMediaPlayer.getDuration();
                MusicService.mMediaPlayer.pause();
                MusicService.mMediaPlayer.seekTo(seekto);
                MusicService.mMediaPlayer.start();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekto = MusicService.mMediaPlayer.getCurrentPosition() - STEP_VALUE;
                if (seekto < 0)
                    seekto = 0;
                MusicService.mMediaPlayer.pause();
                MusicService.mMediaPlayer.seekTo(seekto);
                MusicService.mMediaPlayer.start();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
