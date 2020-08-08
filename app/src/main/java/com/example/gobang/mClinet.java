package com.example.gobang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class mClinet extends AppCompatActivity {

    private Boolean isService;
    private BoardS board;
    private Intent intent;
    //private MyReceiver receiver;
    private String IP ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_clinet);







        final EditText editText = findViewById(R.id.eT);
        board = findViewById(R.id.boardC);
        findViewById(R.id.butClinet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boardS.pieceIs = true;
                IP = String.valueOf(editText.getText());

                intent =  new Intent(mClinet.this, clinetS.class);
                board.intent = intent;

                board.intent.putExtra("ipA",IP);



//
//                createClinet();
                board.pieceIs = false;
                board.createClinet();
            }
        });
        findViewById(R.id.butClinet2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.putExtra("test","测试成功");
//                startService(intent);
            }
        });
    }
//    public void createClinet(){
//
//        isService = true;
//
//        boardS.context = mClinet.this;
//        boardS.intent =intent;
//
//        receiver = new MyReceiver();
//        IntentFilter filter=new IntentFilter();
//        filter.addAction("com.example.gobang.clinetS");
//        registerReceiver(receiver,filter);
//    }
//    private class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle=intent.getExtras();
//            String count = bundle.getString("count");
//            Log.i("Main", count);
//            String x =  count.substring(1,count.indexOf("."));
//            String y =  count.substring(count.indexOf(".")+1,count.length());
//            Log.i("mC",x+" "+y);
//            Point point = new Point(Integer.valueOf(x),Integer.valueOf(y));
//            if(!boardS.mWList.contains(point)){
//                boardS.mWList.add(point);
//                boardS.invalidate();
//            }
//        }
//    }
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
