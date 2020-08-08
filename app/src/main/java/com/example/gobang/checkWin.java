package com.example.gobang;

import android.graphics.Point;

import java.util.List;

import android.graphics.Point;

import java.util.List;

public class checkWin {
    private static int MAX_COUNT_IN_LINE = 5;

    public static boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            }else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            }else{
                break;
            }
        }
        return false;
    }

    public static boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            }else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            }else{
                break;
            }
        }
        return false;
    }

    public static boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            }else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            }else{
                break;
            }
        }
        return false;
    }

    public static boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            }else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            }else{
                break;
            }
        }
        return false;
    }
}
