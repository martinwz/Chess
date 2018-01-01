package com.example.chess;
import static com.example.chess.Constants.BLACKCHESS;
import static com.example.chess.Constants.CHESSRADIUS;
import static com.example.chess.Constants.GRIDSIZE;
import static com.example.chess.Constants.NOCHESS;
import static com.example.chess.Constants.NUM;
import static com.example.chess.Constants.PEN_WIDTH;
import static com.example.chess.Constants.PLAYING;
import static com.example.chess.Constants.REDCHESS;
import static com.example.chess.Constants.TOTAL;
import static com.example.chess.Constants.WHITECHESS;

import com.example.chess.R.color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
public class ChessView extends View  implements Runnable{
	boolean go=false;
	boolean move=false;
	boolean bool=false;
	boolean blackisgo=false;
	boolean whiteisgo=false;
	boolean blacktogo=false;
	boolean whitetogo=false;
	boolean regret=false;
    private Paint mPaint;// = new Paint();
    private static float ox=0,oy=0;
    private static int   px=0,py=0;
    private static boolean who = Constants.BLACK;   //false:����;true:����.
    private static boolean again = false;//���¿�ʼ
    private static boolean again2 = false;//ɾ��
    int sumBlack;//ͳ�ƺ��Ӹ���
    int sumWhite;//ͳ�ư��Ӹ���
    private static int[] isregret =new int[25];
    private static int[][] a=new int[5][5];
    private static int[][] b=new int[5][5];//��¼ѡ�е�����
    public static int[] c=new int[36];//��¼�������õ�����
    public static int[] d=new int[36];//��¼�������õ�����
    private static byte    result = PLAYING;  //����н����3���;֣�6������Ӯ��1������Ӯ��0���ޡ�
    private static boolean end = false;   //false�����δ�������������ӣ�true����ֽ��������������ӡ�
    static  int chess[];  //0:������;1:�����ӣ�����;6:�����ӣ�����;����:������,9,�����ӣ������ӡ�
    int yescolor=0;
    int x,y;
    public int blacksubnum=0;
    public int whitesubnum=0;
    public ChessView(Context context){
       super(context);
       mPaint = new Paint();
       chess = new int[TOTAL];
       who  = Constants.BLACK;      //��������
       again = false;               //false����������true:���¿���
       result = PLAYING;            //������
       end = false;                 //δ�������
       px = 0;
       py = 0;
       setFocusable(true);
       setFocusableInTouchMode(true);
            ClearChess();
       new Thread(this).start();
    }
    public int setMoveResult(int result){
    	return result;
    }
   public void init(){
	  again2 = false;
	   blacksubnum=0;
       whitesubnum=0;
       blacktogo=false;
	  whitetogo=false;
	  for(int i=0;i<5;i++){
		  for(int j=0;j<5;j++){
			  a[i][j]=0;
			  b[i][j]=0;
		  }
	  }
	  for(int i=0;i<25;i++){
		  chess[i]=0;
		  isregret[i]=0;
	  }
	   for(int i=0;i<36;i++){
		   c[i]=0;
		   d[i]=0;
	   }
   }
   //ͳ�ƺ��Ӹ���
   public int sumAndBackBlack(){
	   sumBlack=0;
	   for(int m=0;m<25;m++){
		   if(chess[m]==BLACKCHESS||isregret[m]==BLACKCHESS){
			   sumBlack++;
		   }
	   }
	   return sumBlack;
   }
   public int sumAndBackWhite(){
	   sumWhite=0;
	   for(int m=0;m<25;m++){
		   if(chess[m]==WHITECHESS ||isregret[m]==WHITECHESS){
			   sumWhite++;
		   }
	   }
	   return sumWhite;
   }
   public void setRegret(){
	   for(int i=0;i<25;i++){
		   if(chess[i]==9){
			   chess[i]=isregret[i];
		   }
	   }
	   go=false;
   }
    public ChessView(Context context, AttributeSet attrs){
        super(context,attrs);
        mPaint = new Paint();
       chess = new int[TOTAL];
       who  = Constants.BLACK;      //��������
       again = false;               //false����������true:���¿���
       result = PLAYING;            //������
       end = false;                 //δ�������
       px = 0;
       py = 0;
       setFocusable(true);
       setFocusableInTouchMode(true);
       ClearChess();
       new Thread(this).start();
    }
    public void Setsubnum(  int blacksubnum,int whitesubnum){
    	this.blacksubnum=blacksubnum;
    	this.whitesubnum=whitesubnum;
    }
    public void SetMove(boolean move){
    	this.move=move;
    	again2=false;
    	for(int i=0;i<5;i++){
    		for(int j=0;j<5;j++){
    			b[j][i	]=0;
    		}
    	}
    }
    public boolean GetWho(){       //�������һ��
    	return who;
    }
    public int GetPx(){    //��ô������px����
    	return px;
    }
    public int GetPy(){    //��ô������py����
    	return py;
    }
    public void SetResult(byte result){     //���ö��ĵĽ��
    	this.result = result;
    }
    //�㷨
    public byte GetResult(){      //��ö��ĵĽ��
    	 return result;
    }
    public void SetAgain(boolean again){   //�������¿��ֿ���
       ChessView.again = again;       
       blacksubnum=0;
       whitesubnum=0;
    }
    public boolean checkIsAgain(){
    	return ChessView.again;
    }
    public void SetAgain2(boolean again2){   //��ʼ���Ӳ�������
    	//ɾ�ӿ��غ���
        ChessView.again2 = again2;       
        Toast.makeText(getContext(), "��ʼɾ�Ӳ���", Toast.LENGTH_SHORT).show();
 	   	if(whitesubnum==0){
 		 bool=true;
 		 blackisgo=true;
 		Toast.makeText(getContext(), "�����ȿ�ʼ", Toast.LENGTH_SHORT).show();
 	   	}
 	   	else{
 		 whitetogo=true;
 		 Toast.makeText(getContext(), "�����ȿ�ʼ", Toast.LENGTH_SHORT).show();
 	   }
     }
    public void ClearChess(){              //���̣���ȥ����������������
    	for(int i=0;i<TOTAL;i++)
           chess[i] = NOCHESS;            //����
    		again2=false;
    }
   public void  SetChessColor(Boolean yes) {//�ж�ģʽ
	   if(yes){
		   yescolor=1;
	   }
   }
    public boolean isEnding(){             //����Ƿ������Ҳ����˫���Ƿ���壿
       for(int i=0;i<TOTAL;i++){
           if(chess[i]==NOCHESS)
              return false;
       }
       		return true;
    }
    //��(x,y)��ʼ��5x5���� 
    private void DrawChess(Canvas canvas,float x,float y){
       canvas.drawColor(Color.argb(255, 200, 200, 100));  //�Զ������̵ı�����ɫ
       mPaint.setStrokeWidth(PEN_WIDTH);
       mPaint.setColor(Color.GRAY);                      //���ú�ɫ����
       for(int i=0;i<NUM;i++)        //������
       canvas.drawLine(x, y+i*GRIDSIZE, x+(NUM-1)*GRIDSIZE, y+i*GRIDSIZE, mPaint);
       for(int i=0;i<NUM;i++)        //������
       canvas.drawLine(x+i*GRIDSIZE, y, x+i*GRIDSIZE, y+(NUM-1)*GRIDSIZE, mPaint);
       mPaint.setColor(color.black);
       canvas.drawLine(x, y+4*GRIDSIZE, x+(NUM-1)*GRIDSIZE, y+4*GRIDSIZE, mPaint);
       canvas.drawLine(x+4*GRIDSIZE, y, x+4*GRIDSIZE, y+(NUM-1)*GRIDSIZE, mPaint);
    }
    private void DrawChess(Canvas canvas,int color,float x,float y){   //��(x,y)����Բ������
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(x, y, CHESSRADIUS, mPaint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // TODO Auto-generated method stub
       px=(int)event.getX()/GRIDSIZE;
       py=(int)event.getY()/GRIDSIZE;
       int i;
       i = py*NUM+px;
       if(again2&&!move){   
    	   int moves=0;
    		   if(chess[i]==BLACKCHESS&&whitesubnum>0&&whitetogo){
    			   chess[i]=NOCHESS;
    			   whitesubnum--;
    			   whiteisgo=true;
     		   }
    		   if(chess[i]==WHITECHESS&&blacksubnum>0&&whitesubnum==0){
        			 chess[i]=NOCHESS;
        			  blacksubnum--;
        			 blackisgo=true;
    		   }
        }
       else
       switch(yescolor){
       case 0:
    	   if(py>=0&&py<=NUM-1&&!again2&&!move){
               if(chess[i] == NOCHESS&&!end)
               {
                   chess[i] = (who)?WHITECHESS:BLACKCHESS;
                   if(chess[i]==WHITECHESS){
                	   whiteisgo=true;
                   }else{
                       	   blackisgo=true;
                   }
                   a[py][px]=(who)?6:1;
                   who = !who;
                   }
           }
    	   break;
       case 1:
     	   break;
       }
       if(move){
    	    if(chess[i]!=NOCHESS &&b[py][px]==0&&go==false){
    	    	x=px;
    	    	y=py;
    	    	b[py][px]=1;
    	    	isregret[i]=chess[i];
    	    	chess[i]=9;//��� 
    	    	go=true;//����ͬʱѡ������
    	    }
    	    if(chess[i]==NOCHESS){
    	    	if(Math.abs(i-(y*NUM+x))!=4&&Math.abs(i-(y*NUM+x))!=6&&Math.abs(i-(y*NUM+x))<=6){
    	    	if((py==y+1&&px==x)||(py==y-1&&px==x)||(py==y&&Math.abs(i-(y*NUM+x))==1)){
    	    		chess[i]=a[y][x];
    	    	 if(chess[i]==WHITECHESS){
              	   whiteisgo=true;
                 }else{
              	   blackisgo=true;
                 }
    	    	a[py][px]=a[y][x];
    	    	a[y][x]=0;
    	    	isregret[y*5+x]=0;
    	    	chess[y*5+x]=NOCHESS;
    	    	go=false;
    	    	move=false;
    	    		}
    	    	}
    	    }
       }
       postInvalidate();    //��д���û�����������¼�
       return true;
    }
    //ͳ�ƺ���ȥ���Ӹ���
    public int BlackBack(){
    	if(ChessView.again){//���¿���
    		return blacksubnum;
    	}
    	if(again2){//���Ӳ�����
    		return blacksubnum;
    	}
    	blacksubnum=Subblack();
    	return blacksubnum;
    }
    //ͳ�ư���ȥ���Ӹ���
    public int WhiteBack(){
    	if(ChessView.again){//���¿���
    		return whitesubnum;
    	}
    	if(again2){//���ӹ�����
    		return whitesubnum;
    	}
    	whitesubnum=Subwhite();
    	return whitesubnum;
    }
    //������Ӽ�����
	public int Subblack() {
		// TODO Auto-generated method stub
		int num = 0;
		int n=0;
		if((chess[0]==BLACKCHESS&&chess[6]==BLACKCHESS&&
			    chess[12]==BLACKCHESS&&chess[18]==BLACKCHESS&&chess[24]==BLACKCHESS)&&c[0]==0){
		 c[0]=1;
		num=num+5;
		}
		if((chess[4]==BLACKCHESS&&chess[8]==BLACKCHESS&&
			    chess[12]==BLACKCHESS&&chess[16]==BLACKCHESS&&chess[20]==BLACKCHESS&&c[1]==0)){		
		 c[1]=1;
		num=num+5;
		}
		for(int i=0;i<21;i=i+5){
			if(chess[i]==BLACKCHESS&&chess[i+1]==BLACKCHESS&&
			    chess[i+2]==BLACKCHESS&&chess[i+3]==BLACKCHESS&&chess[i+4]==BLACKCHESS
			    &&c[2+n]==0){//��
			 c[2+n]=1;
			num=num+3;
		}
		n++;
		}
		for(int i=0;i<5;i++){
			if(chess[i]==BLACKCHESS&&chess[i+5]==BLACKCHESS&&
			    chess[i+10]==BLACKCHESS&&chess[i+15]==BLACKCHESS&&chess[i+20]==BLACKCHESS
			    &&c[7+i]==0 ){//��
			 c[7+i]=1;
			num=num+3;
			}
		}
		if(chess[3]==BLACKCHESS&&chess[7]==BLACKCHESS&&
			    chess[11]==BLACKCHESS&&chess[15]==BLACKCHESS&&c[12]==0 ){
			c[12]=1;
			num=num+2;
		}
		if(chess[1]==BLACKCHESS&&chess[7]==BLACKCHESS&&
			    chess[13]==BLACKCHESS&&chess[19]==BLACKCHESS&&c[13]==0 ){
			c[13]=1;
			num=num+2;
		}
		if(chess[9]==BLACKCHESS&&chess[13]==BLACKCHESS&&
			   chess[17]==BLACKCHESS&&chess[21]==BLACKCHESS &&c[14]==0){
			c[14]=1;
			num=num+2;
		}
		if(chess[5]==BLACKCHESS&& chess[11]==BLACKCHESS&&
			    chess[17]==BLACKCHESS&&chess[23]==BLACKCHESS&&c[15]==0 ){
			c[15]=1;
			num=num+2;
		}
		if(chess[2]==BLACKCHESS&&chess[6]==BLACKCHESS&&
			    chess[10]==BLACKCHESS &&c[16]==0){
			c[16]=1;
			num=num+1;
		}
		if(chess[2]==BLACKCHESS&&chess[8]==BLACKCHESS&&
			    chess[14]==BLACKCHESS &&c[17]==0){
			c[17]=1;
			num=num+1;
		}
		if(chess[10]==BLACKCHESS&&chess[16]==BLACKCHESS&&
			    chess[22]==BLACKCHESS&&c[18]==0 ){
			c[18]=1;
			num=num+1;
		}
		if(chess[14]==BLACKCHESS&&    chess[18]==BLACKCHESS&&
			    chess[22]==BLACKCHESS&&c[19]==0 ){
			c[19]=1;
			num=num+1;
		}
		int bn=0;
		for(int i=0;i<16;i=i+5){	
			for(int j=i;j<i+4;j++){
				if(chess[j]==BLACKCHESS&&chess[j+1]==BLACKCHESS&&chess[j+5]==BLACKCHESS
					&&chess[j+6]==BLACKCHESS&&c[20+bn]==0){
				 c[20+bn]=1;
				num=num+1;
			}
			bn++;
		}
	}
	return num;
	}
	//������Ӽ�����
	public int Subwhite() {
		// TODO Auto-generated method stub
		int num = 0;
		int n=0;
		if((chess[0]==WHITECHESS&&chess[6]==WHITECHESS&&
			    chess[12]==WHITECHESS&&chess[18]==WHITECHESS&& chess[24]==WHITECHESS&&d[0]==0)){
			d[0]=1;
			num=num+5;
		}
		if((chess[4]==WHITECHESS&&chess[8]==WHITECHESS&&
			    chess[12]==WHITECHESS&&chess[16]==WHITECHESS&&chess[20]==WHITECHESS)&&d[1]==0){
			d[1]=1;
			num=num+5;
	}
	for(int i=0;i<21;i=i+5){
		if(chess[i]==WHITECHESS&&chess[i+1]==WHITECHESS&&
			    chess[i+2]==WHITECHESS&&chess[i+3]==WHITECHESS&&chess[i+4]==WHITECHESS
			    &&d[2+n]==0){	
			d[2+n]=1;
			num=num+3;
		}
		n++;
	}
	for(int i=0;i<5;i++){
		if(chess[i]==WHITECHESS&&chess[i+5]==WHITECHESS&&
			   chess[i+10]==WHITECHESS&&chess[i+15]==WHITECHESS&&chess[i+20]==WHITECHESS  &&d[7+i]==0){	
			d[7+i]=1;
			num=num+3;
			}
		}
		if(chess[3]==WHITECHESS&&chess[7]==WHITECHESS&&
			   chess[11]==WHITECHESS&&chess[15]==WHITECHESS&&d[12]==0){	
			d[12]=1;
			num=num+2;
		}
		if(chess[1]==WHITECHESS&&chess[7]==WHITECHESS&&
			  chess[13]==WHITECHESS&&chess[19]==WHITECHESS&&d[13]==0 ){
		d[13]=1;
			num=num+2;
		}
		if(chess[9]==WHITECHESS&&chess[13]==WHITECHESS&&
			    chess[17]==WHITECHESS&&chess[21]==WHITECHESS&&d[14]==0 ){
			d[14]=1;
			num=num+2;
		}
		if(chess[5]==WHITECHESS&&chess[11]==WHITECHESS&&
			    chess[17]==WHITECHESS&&chess[23]==WHITECHESS &&d[15]==0){
			d[15]=1;
			num=num+2;
		}
		if(chess[2]==WHITECHESS&&chess[6]==WHITECHESS&&
			    chess[10]==WHITECHESS &&d[16]==0){
			d[16]=1;
			num=num+1;
		}
		if(chess[2]==WHITECHESS&&chess[8]==WHITECHESS&&
			    chess[14]==WHITECHESS &&d[17]==1){	
			d[17]=1;
			num=num+1;
		}
		if(chess[10]==WHITECHESS&&    chess[16]==WHITECHESS&&
			    chess[22]==WHITECHESS &&d[18]==0){	
			d[18]=1;
			num=num+1;
		}
		if(chess[14]==WHITECHESS&&    chess[18]==WHITECHESS&&
			    chess[22]==WHITECHESS &&d[19]==0){
			d[19]=1;
			num=num+1;
		}
		int wn=0;//������
		for(int i=0;i<16;i=i+5){
			for(int j=i;j<i+4;j++){
				if(chess[j]==WHITECHESS&&chess[j+1]==WHITECHESS&&chess[j+5]==WHITECHESS
					&&chess[j+6]==WHITECHESS&&d[20+wn]==0){
				d[20+wn]=1;
			   	num=num+1;
			}
			wn++;
		}
	}
	return num;
	}
    @Override
    protected void onDraw(Canvas canvas) {
        ox = (float)(Constants.MARGIN);
       oy = (float)(Constants.MARGIN);
       DrawChess(canvas,ox,oy);    //������
       for(int i=0;i<TOTAL;i++){
           int row = (int)i/NUM;
           int column = i%NUM;
           switch(chess[i]){
           case BLACKCHESS:   
              DrawChess(canvas,Color.BLACK,ox+column*GRIDSIZE,oy+row*GRIDSIZE);
              break;
           case WHITECHESS:
              DrawChess(canvas,Color.WHITE,ox+column*GRIDSIZE,oy+row*GRIDSIZE);
              break;
           case REDCHESS:
        	   DrawChess(canvas,Color.RED,ox+column*GRIDSIZE,oy+row*GRIDSIZE);
        	   break;
           }
       }
       // TODO Auto-generated method stub
       super.onDraw(canvas);
    }
    @Override
    public void run() {
       // TODO Auto-generated method stub
       while(!Thread.currentThread().isInterrupted()) {         
              if(again){
                  ClearChess();
                  who  = false;
                  end  = false;
                  result =PLAYING;
                  again = false;
              }
              if(result!=PLAYING){
                  end = true;
                   }
           try{
              Thread.sleep(50);
           }catch(InterruptedException e){
              Thread.currentThread().interrupt();
           }
           postInvalidate();
       }
    }
}
