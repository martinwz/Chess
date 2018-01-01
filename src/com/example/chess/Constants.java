package com.example.chess;
import android.view.Menu;
public interface Constants {
    public static final int  NUM   = 5;  //棋盘上每行或每列摆放的棋子数量
    public static final int  TOTAL = NUM*NUM;   //整个棋盘棋子数量
    public static final byte PLAYING = 0;    //对弈中
    public static final byte NOCHESS = 0;    //没有棋子
    public static final byte WHITECHESS = 6; //白子权值
    public static final byte BLACKCHESS = 1; //黑子权值
    public static final byte REDCHESS = 9; //红子权值
    public static final byte WINWIN  = 3;    //和棋
    public static final byte REFLESH = 20;
    public static final int CHESSRADIUS     = 85; //圆形棋子半径
    public static final int GRIDSIZE        = 220; //方形棋格大小
    public static final int MARGIN      = 99; //棋盘距离左边和上边的空隙
    public static final float PEN_WIDTH = 2.0f;  //画笔粗细大小
    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    public final int ANGAIN = Menu.FIRST;
    public final int SAVE  = Menu.FIRST+1;
    public final int QUIT  = Menu.FIRST+2;
}