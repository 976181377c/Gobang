package com.example.gobang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class mService extends AppCompatActivity {

    private BoardS board ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_service);

        board = findViewById(R.id.boardS);

        board.gameIs = false;
        board.pieceIs = true;

        Intent intent  = new Intent(mService.this,serviceS.class);
        board.intent = intent;

        board.createService();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("cc","服务销毁");
        if(board.receiver != null){
            unregisterReceiver(board.receiver);
        }
        if(board.intent !=null){
            stopService(board.intent);
        }


    }

}
