package com.example.chess;
import android.view.Menu;
public interface Constants {
    public static final int  NUM   = 5;  //������ÿ�л�ÿ�аڷŵ���������
    public static final int  TOTAL = NUM*NUM;   //����������������
    public static final byte PLAYING = 0;    //������
    public static final byte NOCHESS = 0;    //û������
    public static final byte WHITECHESS = 6; //����Ȩֵ
    public static final byte BLACKCHESS = 1; //����Ȩֵ
    public static final byte REDCHESS = 9; //����Ȩֵ
    public static final byte WINWIN  = 3;    //����
    public static final byte REFLESH = 20;
    public static final int CHESSRADIUS     = 85; //Բ�����Ӱ뾶
    public static final int GRIDSIZE        = 220; //��������С
    public static final int MARGIN      = 99; //���̾�����ߺ��ϱߵĿ�϶
    public static final float PEN_WIDTH = 2.0f;  //���ʴ�ϸ��С
    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    public final int ANGAIN = Menu.FIRST;
    public final int SAVE  = Menu.FIRST+1;
    public final int QUIT  = Menu.FIRST+2;
}