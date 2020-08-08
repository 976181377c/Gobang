package com.example.gobang;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Boolean Is = false;
    private Board board;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        intent = new Intent(this,mBgm.class);
//        startService(intent);


        board = findViewById(R.id.board);

        board.context = this;

        findViewById(R.id.but1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.reStart();
            }
        });

        findViewById(R.id.but2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.rePiece();
            }
        });

        findViewById(R.id.but3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Is){
                    mediaPlayer.pause();
                }else {
                    Log.i("player","播放");
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.cure);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
                Is = !Is;
            }
        });

        findViewById(R.id.but4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,mService.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.but5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,mClinet.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null &&mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Is){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null &&mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

}
