package com.example.gobang;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class clinetS extends Service {

    private String line;
    private String HOST = null;  //虚拟机的地址
    private static final int PORT = 8989;
    private String TGA = "clinetS";
    private Intent intent;
    private String string = "hi";
    private boolean isService = true;


    public clinetS() {

        new Thread() {
            @Override
            public void run() {


                if (intent != null) {
                    HOST = intent.getStringExtra("ipA");
                   // Log.i(TGA, HOST);
                }

                while (isService) {

                    try {
                        // 实例化Socket
                        Socket socket = new Socket(HOST, PORT);
                        Log.i(TGA, "创建连接");
                        // 获得输入流
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        line = br.readLine();
                        br.close();
                        Log.i(TGA, line);


                        Intent intent = new Intent();

                        intent.putExtra("count", line);
                        intent.setAction("com.example.gobang.clinetS");
                        sendBroadcast(intent);



                        socket = new Socket(HOST, PORT);

                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()));
                        String s = "B";
                        if(string!=null){
                            s = "B"+string;
                        }
                        writer.write(s);
                        writer.flush();

                        Log.i(TGA, "信息已发送"+string);

                        writer.close();

                        sleep(1000);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.intent = intent;

        if(intent != null){

            string = intent.getStringExtra("point");
            if(string!=null)
                Log.i(TGA,"发送数据"+string);


        }

//        s = intent.getStringExtra("test");
//        if(s != null){
//            Log.i(TGA,s);
//        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isService = false;
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
