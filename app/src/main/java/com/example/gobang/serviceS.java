package com.example.gobang;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class serviceS extends Service {

    private static final int PORT = 8989;
    private static Point point = null;
    private static ServerSocket server;
    private static String TGA = "service";
    private Intent intent;
    private String string = "";
    private boolean isService = true;


    public serviceS() {

        new Thread(){
            @Override
            public void run() {
                try {
                    // 实例化服务器套接字 设置端口号8989
                    server = new ServerSocket(PORT);
                    while (isService) {
                        Socket socket = server.accept();

                        Log.i(TGA,"连接中...");
                        // 获取输出流
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream()));

                        writer.write("W"+string);
                        writer.flush();
                        writer.close();

                        Socket socket2 = server.accept();

                        Log.i(TGA,"发送成功");

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(socket2.getInputStream()));
                        String string2 = reader.readLine();

                        Log.i(TGA,"收到"+string2);

                            Intent intent = new Intent();
                            Log.i(TGA,"开始广播");
                            intent.putExtra("count", string2);
                            intent.setAction("com.example.gobang.serviceS");
                            sendBroadcast(intent);

                        reader.close();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.intent = intent;

        if(intent != null) {
            string = intent.getStringExtra("point");
            if (string != null)
                Log.i(TGA, "发送数据" + string);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isService = false;
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
