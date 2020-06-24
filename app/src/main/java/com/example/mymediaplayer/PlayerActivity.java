package com.example.mymediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
Button btn_next,btn_previous,btn_pause_play;
TextView songTextLabel;
SeekBar seekBar;
static MediaPlayer myMediaPlayer;
int position;
ArrayList<File> mySongs;
Thread updateseekBar;
String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    btn_next = (Button)findViewById(R.id.button_next);
        btn_pause_play = (Button)findViewById(R.id.button_play_pause);
        btn_previous = (Button)findViewById(R.id.button_previous);
    songTextLabel = (TextView)findViewById(R.id.textView_song_name_display);
    seekBar = (SeekBar)findViewById(R.id.seekBar);



    updateseekBar = new Thread()
    {
        @Override
        public void run() {

            int totalDuration = myMediaPlayer.getDuration();
            int currentPosition =0;
            while(currentPosition<totalDuration)
            {try{
                sleep(500);
                currentPosition = myMediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);

            }
            catch (InterruptedException e)
            { e.printStackTrace();  }

            }


            //super.run();
        }
    };


    if(myMediaPlayer!=null){
        myMediaPlayer.stop();
        myMediaPlayer.release();
    }

        Intent i = getIntent();
    Bundle bundle = i.getExtras();
    mySongs = (ArrayList)bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");
        songTextLabel.setText(songName);
        position = bundle.getInt("pos",0);
        Uri u = Uri.parse(mySongs.get(position).toString());
        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        seekBar.setMax(myMediaPlayer.getDuration());
        updateseekBar.start();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setMax(myMediaPlayer.getDuration());
                if (myMediaPlayer.isPlaying()) {
                    btn_pause_play.setBackgroundResource(R.drawable.play);
                    myMediaPlayer.pause();
                } else {
btn_pause_play.setBackgroundResource(R.drawable.pause);
myMediaPlayer.start();

                }

            }

        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position= ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();

            }
        });


        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();


            }
        });

    }



}
