package com.example.gobang;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoardS extends View {

    public boolean pieceIs = false;

    public Intent intent;
    private String TGA = "Board";
    private int boardW;
    private float lineW;
    private int MAX_LINE = 10;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    public Boolean gameIs = true;
    public List<Point> mWList = new ArrayList<>();
    public List<Point> mBList = new ArrayList<>();

    private Paint paint;

    public MyReceiver receiver;

    public Boolean isService = false;
    public Context context;


    public BoardS(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {

        paint = new Paint();
        paint.setColor(0xff000000);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int Size = MeasureSpec.makeMeasureSpec(widthSpecSize, widthSpecMode);

        setMeasuredDimension(Size, Size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardW = w;
        lineW = boardW * 1.0f / MAX_LINE;
        int pieceW = (int) (lineW * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceW, pieceW, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceW, pieceW, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction();

        if (gameIs){
            return false;
        }

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) (event.getX() / lineW);
            int y = (int) (event.getY() / lineW);
            Point point = new Point(x, y);

            if (mWList.contains(point) || mBList.contains(point)) {
                return false;
            }
            Log.i(TGA, x + " " + y);

            if (!pieceIs) {
                mBList.add(point);

                intent.putExtra("point",point.x+"."+point.y);

                gameIs  = !gameIs;

                context.startService(intent);
                invalidate();

                if(checkFiveInLine(mBList)){

                    closeService();
                    gameIs = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提醒");
                    builder.setMessage("黑棋胜利");
                    builder.show();

                }

                //gameIs = checkFiveInLine(mBList);
            } else {
                mWList.add(point);

                intent.putExtra("point",point.x+"."+point.y);

                context.startService(intent);

                gameIs  = !gameIs;
                invalidate();

                if(checkFiveInLine(mWList)){
                    closeService();
                    gameIs = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提醒");
                    builder.setMessage("白棋胜利");
                    builder.show();
                }

                //gameIs = checkFiveInLine(mWList);
            }
            //pieceIs = !pieceIs;

            return true;
        }

        return true;
    }

    private boolean checkFiveInLine(List<Point> points) {
       // Point p = points.get(points.size()-1);
        for (Point p : points)
        {
            int x = p.x;
            int y = p.y;
            boolean win = checkWin.checkHorizontal(x, y, points);
            if (win) {  //如果横向5连成功
                return true;
            }
            win = checkWin.checkVertical(x, y, points);
            if (win) {  //如果纵向5连成功
                return true;
            }
            win = checkWin.checkLeftDiagonal(x, y, points);
            if (win) {  //如果左斜5连成功
                return true;
            }
            win = checkWin.checkRightDiagonal(x, y, points);
            if (win) {  //如果右斜5连成功
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBoard(canvas);
        DrawPiece(canvas);
    }

    private void DrawPiece(Canvas canvas) {

        for (int i = 0; i < mWList.size(); i++) {

            Point point = mWList.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (point.x + (1 - ratioPieceOfLineHeight) / 2) * lineW,
                    (point.y + (1 - ratioPieceOfLineHeight) / 2) * lineW,
                    null);
        }
        for (int i = 0; i < mBList.size(); i++) {

            Point point = mBList.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (point.x + (1 - ratioPieceOfLineHeight) / 2) * lineW,
                    (point.y + (1 - ratioPieceOfLineHeight) / 2) * lineW,
                    null);
        }

    }

    private void DrawBoard(Canvas canvas) {
        Log.i(TGA, "开始画");
        for (int i = 0; i < MAX_LINE; i++) {

            int startX = (int) (lineW / 2);
            int endX = (int) (boardW - lineW / 2);
            int y = (int) (lineW / 2 + lineW * i);
            canvas.drawLine(startX, y, endX, y, paint);
            canvas.drawLine(y, startX, y, endX, paint);

        }
    }


    public void reStart() {
        //清空数据
        mWList.clear();
        mBList.clear();
        gameIs = false;
        pieceIs = false;
        //重新绘制View
        invalidate();
    }

    public void rePiece() {
        if((mWList.size() !=0 || mBList.size() != 0 )&& !gameIs){
            if(pieceIs){
                mWList.remove(mWList.size()-1);
            }else {
                mBList.remove(mBList.size()-1);
            }
            pieceIs = !pieceIs;
            invalidate();
        }

    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String count = bundle.getString("count");
            Log.i("Main", count+""+count.length());

            if(count.length()>2){

                String p = count.substring(0,1);
                String x =  count.substring(1,count.indexOf("."));
                String y =  count.substring(count.indexOf(".")+1,count.length());
                Log.i("mC",p+""+x+" "+y);
                Point point = new Point(Integer.valueOf(x),Integer.valueOf(y));
                if(!mWList.contains(point)&&p.equals("W")){
                    mWList.add(point);
                    gameIs = !gameIs;
                    invalidate();
                    if(checkFiveInLine(mWList)){

                        closeService();

                        gameIs = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提醒");
                        builder.setMessage("白棋胜利");
                        builder.show();

                    }

                }
                if(!mBList.contains(point)&&p.equals("B")){
                    mBList.add(point);
                    gameIs = !gameIs;
                    invalidate();

                    if(checkFiveInLine(mBList)){
                        closeService();
                        gameIs = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提醒");
                        builder.setMessage("黑棋胜利");
                        builder.show();
                    }
                }

            }


        }
    }
    public void createClinet(){

        isService = true;

        context.startService(intent);

        receiver = new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.gobang.clinetS");
        context.registerReceiver(receiver,filter);

    }
    public void createService(){

        isService = true;

        context.startService(intent);

        receiver = new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.gobang.serviceS");
        context.registerReceiver(receiver,filter);

    }

    private void closeService(){

        context.unregisterReceiver(receiver);
        receiver =null;

        context.stopService(intent);
        intent = null;

    }

}
