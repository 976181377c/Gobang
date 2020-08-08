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

public class Board extends View {

    private String TGA = "Board";
    private int boardW;
    private float lineW;
    private int MAX_LINE = 10;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private Boolean pieceIs = false;
    private Boolean gameIs = false;
    private List<Point> mWList = new ArrayList<>();
    private List<Point> mBList = new ArrayList<>();

    private Paint paint;

    public Context context;


    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
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

            if (pieceIs) {
                mBList.add(point);
                invalidate();
                gameIs = checkFiveInLine(mBList);
            } else {
                mWList.add(point);
                invalidate();
                gameIs = checkFiveInLine(mWList);
                if(gameIs == true){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提醒");
                    builder.setMessage("白棋胜利");
                    builder.show();
                }

            }
            pieceIs = !pieceIs;

            return true;
        }

        return true;
    }

    private boolean checkFiveInLine(List<Point> points) {
    //    Point p = points.get(points.size()-1);
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


}
