package com.example.chess;
import static com.example.chess.Constants.BLACKCHESS;
import java.math.*;
import java.util.Random;
import static com.example.chess.Constants.CHESSRADIUS;
import static com.example.chess.Constants.GRIDSIZE;
import static com.example.chess.Constants.NOCHESS;
import static com.example.chess.Constants.NUM;
import static com.example.chess.Constants.PEN_WIDTH;
import static com.example.chess.Constants.PLAYING;
import static com.example.chess.Constants.TOTAL;
import static com.example.chess.Constants.WHITECHESS;
import static com.example.chess.Constants.REDCHESS;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
public class ChessViewPvsC extends View  implements Runnable{
	public int xz=-1;
	public int k=0;
	boolean sim=false;
	boolean go=false;
	boolean move=false;
	boolean bool=false;
	boolean blackisgo=false;
	boolean whiteisgo=false;
	boolean blacktogo=false;
	boolean whitetogo=false;
	boolean regret=false;
	public boolean begin=false;
    private Paint mPaint;// = new Paint();
    private static float ox=0,oy=0;
    private static int   px=0,py=0;
    private static boolean who = Constants.BLACK;   //false:����;true:����.
    private static boolean again = false;//���¿�ʼ
    public static boolean again2 = false;//ɾ��
    int sumBlack;//ͳ�ƺ��Ӹ���
    int sumWhite;//ͳ�ư��Ӹ���
    private static int[] isregret =new int[25];
    private static int[][] a=new int[5][5];
    private static int[][] b=new int[5][5];//��¼ѡ�е�����
    public static int[] c=new int[36];//��¼���õ�����
    public static int[] d=new int[36];//��¼���õ�����
    private static int bstepnum=0;//���Ӳ���
    private static int wstepnum=0;//���Ӳ��� 
    private static byte    result = PLAYING;  //����н����3���;֣�6������Ӯ��1������Ӯ��0���ޡ�
    private static boolean end = false;       //false�����δ�������������ӣ�true����ֽ��������������ӡ�
    static  int chess[];// = new byte[TOTAL];   //0:������;1:�����ӣ�����;6:�����ӣ�����;����:������,9,�����ӣ������ӡ�
    public  int yescolor=0;
    int x,y;
    public static int blacksubnum=0;
    public static int whitesubnum=0;
    public ChessViewPvsC(Context context){
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
    public int returnXz(){
    	return this.xz;
    }
   public void init(){
	    again = false;
	    again2 = false;
	    k=0;
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
		  isregret[i]=0;
		  chess[i]=0;
	  }
	   for(int i=0;i<36;i++){
		   c[i]=0;
		   d[i]=0;
	   }
   }
   //ͳ�ƺ��Ӹ���
   public int sumAndBackBlack(){//ͳ�ƺ�������
	   sumBlack=0;
	   for(int m=0;m<25;m++){
		   if(chess[m]==BLACKCHESS||isregret[m]==BLACKCHESS){
			   sumBlack++;
		   }
	   }
	   return sumBlack;
   }
   public int sumAndBackWhite(){//ͳ�ư�����������
	   sumWhite=0; 
	   for(int m=0;m<25;m++){
		   if(chess[m]==WHITECHESS ){
			   sumWhite++;
		   }
	   }
	   return sumWhite;
   }
   public void setRegret(){//ȡ��ѡ����
	   for(int i=0;i<25;i++){
		   if(chess[i]==9){
			   chess[i]=isregret[i];
		   }
	   }
	   go=false;
   }
    public ChessViewPvsC(Context context, AttributeSet attrs){
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
    public void setWhiteIsGo(){
  
    	 whiteisgo=false;
    }
    public void setBlackIsGo(){
    	 blackisgo=false;
    }
    public Boolean BlackIsGo(){
    	 return blackisgo;
    }
    public Boolean WhiteIsGo(){
         return whiteisgo;
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
        ChessViewPvsC.again = again;       
        bstepnum=0;
        wstepnum=0;
        blacksubnum=0;
        whitesubnum=0;
    }
    public boolean checkIsAgain(){
    	 return ChessViewPvsC.again;
    }
    public void cMove2(){
    	 int j=1;
    	 Random random=new Random();//���������
    	 int i=random.nextInt(25);
    	 while(chess[i]!=WHITECHESS){
    		 i=random.nextInt(25);
    	}
    	 if(i==24){
			 if(chess[19]==NOCHESS){
				chess[19]=chess[24];
				chess[24]=0;
				j++;
			 }else if(chess[23]==NOCHESS){
				chess[23]=chess[24];
				chess[24]=0;
				j++;
			 }
		 }
		 else {
			if(i==6||i==7||i==8||i==11||i==12||i==13||i==16||i==17||i==18){
				if(chess[i-1]==0){
					chess[i-1]=chess[i];
					chess[i]=0;
					j++;
				}else	if(chess[i+1]==0){
					chess[i+1]=chess[i];
					chess[i]=0;
					j++;
				}else 	if(chess[i-5]==0){
					chess[i-5]=chess[i];
					chess[i]=0;
					j++;
				}else if(chess[i+5]==0){
					chess[i+5]=chess[i];
					chess[i]=0;
					j++;
				}
			}
			switch(i){
			case 0:if(chess[1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 4:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 20:if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 24:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 1:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 2:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 3:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 9:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 14:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 19:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 21:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 22:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 23:if(chess[i-1]==0){
				chess[i-1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 5:if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 10:if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			case 15:if(chess[i+1]==0){
				chess[i+1]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i-5]==0){
				chess[i-5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			if(chess[i+5]==0){
				chess[i+5]=chess[i];
				chess[i]=0;
				j++;
				break;
			}
			  break;
			}
		}
    }
    public void SetAgain2(boolean again2){   //ɾ�ӿ���
        ChessViewPvsC.again2 = again2;       
        Toast.makeText(getContext(), "��ʼɾ�Ӳ���", Toast.LENGTH_SHORT).show();
 		 blacktogo=true;
      }
    public void ClearChess(){              //���̣���ȥ����������������
    	for(int i=0;i<TOTAL;i++)
           chess[i] = NOCHESS;            //����
    	   again2=false;
    }
   public void  SetChessColor(int  yes) {
	   		this.yescolor=yes;
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
    	mPaint.setColor(Color.BLACK);
      	canvas.drawLine(x, y+4*GRIDSIZE, x+(NUM-1)*GRIDSIZE, y+4*GRIDSIZE, mPaint);
      	canvas.drawLine(x+4*GRIDSIZE, y, x+4*GRIDSIZE, y+(NUM-1)*GRIDSIZE, mPaint);
    }
    private void DrawChess(Canvas canvas,int color,float x,float y){   //��(x,y)����Բ������
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(x, y, CHESSRADIUS, mPaint);
    }
    public void deleteBlack(int sum){
		int sum2=0;
	   for(int j=0;j<25&&sum2<=sum&&this.whitesubnum>0;j++){
		   if(chess[j]==BLACKCHESS){
			   chess[j]=NOCHESS;
			   sum2++;
			   this.whitesubnum--;
			   if(whitesubnum<0){
				   this.whitesubnum=0;
			   }
			   sim=false;
			    if(j<5){
			    	a[0][j]=0;
			   }
			    else if(j>=5&&j<=9){
			    	a[1][j-5]=0;
			    }
			    else if(j>=10&&j<=14){
			    	a[2][j-10]=0;
			    }
			    else if(j>=15&&j<=19){
			    	a[3][j-15]=0;
			    }
			    else {
			    	a[4][j-20]=0;
			    }
		   }
	   }
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
    		   if(chess[i]==WHITECHESS&&blacksubnum>0&&blacktogo){
    			   chess[i]=NOCHESS;
    			   blacksubnum--;
    		   }
       }
       if(begin){
	   switch(yescolor){
       case 1:
    	   if(py>=0&&py<=NUM-1&&!again2&&!move)	//�˻���ս��ͨģʽ
           {
    		   if(chess[i] == NOCHESS&&!end &&i<=24)
               {
                   chess[i] = BLACKCHESS;
                   a[py][px]=1;
                   sim=true;
                   if(i==24){//���½ǵ㴦��
                	   if(chess[19]==NOCHESS){
                		   chess[19]=WHITECHESS;
                		   a[3][4]=6;
                	   }
                	   else if(chess[23]==NOCHESS){
                		   chess[23]=WHITECHESS;
                		   a[4][3]=6;
                	   }
                	   else {
                		   for(int j=0;j<25&&sim;j++){
                			   if(chess[j]==NOCHESS){
                				   chess[j]=WHITECHESS;
                				   sim=false;
                				    if(j<5){
                				    	a[0][j]=6;
                				   }
                				    else if(j>=5&&j<=9){
                				    	a[1][j-5]=6;
                				    }
                				    else if(j>=10&&j<=14){
                				    	a[2][j-10]=6;
                				    }
                				    else if(j>=15&&j<=19){
                				    	a[3][j-15]=6;
                				    }
                				    else {
                				    	a[4][j-20]=6;
                				    }
                			   }
                		   }
                	   }
                   }
                   else { 
                	   if(chess[i+1]==NOCHESS){
                	   if(px==4||px==9||px==14||px==19){
                		  a[py+1][px-4]=6;
                	   chess[i+1]=WHITECHESS;
                	   	}
                	   else{
                		  chess[i+1]=WHITECHESS;
                		  a[py][px+1]=6;
                	   	}            	   
                	   }
                	   else if(chess[i+1]!=NOCHESS) {
                	   for(int j=0;j<25&&sim;j++){
            			   if(chess[j]==NOCHESS){
            				   chess[j]=WHITECHESS;
            				   sim=false;
            				    if(j<5){
            				    	a[0][j]=6;
            				   } else if(j>=5&&j<=9){
            				    	a[1][j-5]=6;
            				    } else if(j>=10&&j<=14){
            				    	a[2][j-10]=6;
            				    } else if(j>=15&&j<=19){
            				    	a[3][j-15]=6;
            				    }else {
            				    	a[4][j-20]=6;
            				    }
            			   }
                	   }
                   }
                   }
               }
           }
    	   break;
       case 2:
    	   if(py>=0&&py<=NUM-1&&!again2&&!move)	//�˻���ս����ģʽ
           {
               if(chess[i] == NOCHESS&&!end &&i<=24)
               {
                   chess[i] = BLACKCHESS;
                   a[py][px]=1;
                   k++;
                   sim=true;
                   if(k<9){
                	   if(chess[12]==NOCHESS){
                		   chess[12]=WHITECHESS;
                	   }
                	   else   if(chess[0]==NOCHESS){
                		   chess[0]=WHITECHESS;
                	   }
                	   else  if(chess[4]==NOCHESS){
                		   chess[4]=WHITECHESS;
                	   }
                	   else if(chess[24]==NOCHESS){
                		   chess[24]=WHITECHESS;
                	   }
                	   else   if(chess[20]==NOCHESS){
                		   chess[20]=WHITECHESS;
                	   }
                	   else   if(chess[6]==NOCHESS){
                		   chess[6]=WHITECHESS;
                	   }
                	   else   if(chess[16]==NOCHESS){
                		   chess[16]=WHITECHESS;
                	   }
                	   else   if(chess[18]==NOCHESS){
                		   chess[18]=WHITECHESS;
                	   }
                	   else   if(chess[8]==NOCHESS){
                		   chess[8]=WHITECHESS;
                	   }
                	   else{
                		   for(int j=0;j<25&&sim ;j++){
                	 if(Math.abs(j-i)!=1&&Math.abs(j-i)!=4&&Math.abs(j-i)!=6&&Math.abs(j-i)!=5&&
                			 Math.abs(j-i)>5&&chess[j]==0){
                		 chess[j]=6;
                		 sim=false;
                	 }
                   }
               }	    
              }
               else{
                	for(int j=0;j<25&&sim;j++){
                      if(chess[j]==0){
                    	  chess[j]=6;
                      		sim=false;
                      	 }
                	   }
                   } 
               }
           }
    	   break;
       }
       }
       if(move){//���Ӳ���
    	    if(chess[i]!=NOCHESS &&chess[i]!=WHITECHESS&&b[py][px]==0&&go==false){
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
    	    			a[py][px]=a[y][x];
	    	    		a[y][x]=0; 
	    	    		chess[y*5+x]=NOCHESS;
	    	    		xz=xz*(-1);//�����ź���
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
    	if(ChessViewPvsC.again){//���¿���
    		return blacksubnum;
    	}
    	if(again2){//���ӹ�����
    		return blacksubnum;
    	}
    	blacksubnum=Subblack();
    	return blacksubnum;
    }
    //ͳ�ư���ȥ���Ӹ���
    public int WhiteBack(){
    	if(ChessViewPvsC.again){
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
		   chess[i+2]==BLACKCHESS&& chess[i+3]==BLACKCHESS&& chess[i+4]==BLACKCHESS
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
		if( chess[9]==BLACKCHESS&&chess[13]==BLACKCHESS&&
			chess[17]==BLACKCHESS&&chess[21]==BLACKCHESS &&c[14]==0){
		 c[14]=1;
		num=num+2;
		}
		if(chess[5]==BLACKCHESS&&chess[11]==BLACKCHESS&&
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
		if(chess[10]==BLACKCHESS&& chess[16]==BLACKCHESS&&
			chess[22]==BLACKCHESS&&c[18]==0 ){
		 c[18]=1;
			num=num+1;
		}
	    if(chess[14]==BLACKCHESS&&chess[18]==BLACKCHESS&&
		chess[22]==BLACKCHESS&&c[19]==0 ){
		 c[19]=1;
		num=num+1;
		}
	int bn=0;
	for(int i=0;i<16;i=i+5){	
		for(int j=i;j<i+4;j++){
			if( chess[j]==BLACKCHESS&&chess[j+1]==BLACKCHESS&&chess[j+5]==BLACKCHESS
					&&    chess[j+6]==BLACKCHESS&&c[20+bn]==0){
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
			    chess[12]==WHITECHESS&&chess[18]==WHITECHESS&& chess[24]==WHITECHESS)&&d[0]==0){
		 d[0]=1;
		num=num+5;
		}
		if((chess[4]==WHITECHESS&&chess[8]==WHITECHESS&&
			    chess[12]==WHITECHESS&&chess[16]==WHITECHESS&&chess[20]==WHITECHESS&&d[1]==0)){		
		 d[1]=1;
		num=num+5;
		}
		for(int i=0;i<21;i=i+5){
		if(chess[i]==WHITECHESS&&chess[i+1]==WHITECHESS&&
			  chess[i+2]==WHITECHESS&&chess[i+3]==WHITECHESS&& chess[i+4]==WHITECHESS
			  &&d[2+n]==0){//��
			 d[2+n]=1;
			num=num+3;
		 }
		n++;
	   }
		for(int i=0;i<5;i++){
		if(chess[i]==WHITECHESS&&chess[i+5]==WHITECHESS&&
			    chess[i+10]==WHITECHESS&&chess[i+15]==WHITECHESS&&chess[i+20]==WHITECHESS
			    &&d[7+i]==0 ){//��
			 d[7+i]=1;
			num=num+3;
			}
		}
		if(chess[3]==WHITECHESS&&chess[7]==WHITECHESS&&
			    chess[11]==WHITECHESS&&chess[15]==WHITECHESS&&d[12]==0 ){
		 d[12]=1;
		num=num+2;
		}
		if(chess[1]==WHITECHESS&&chess[7]==WHITECHESS&&
			    chess[13]==WHITECHESS&&chess[19]==WHITECHESS&&d[13]==0 ){
			d[13]=1;
			num=num+2;
		}
		if(chess[9]==WHITECHESS&&chess[13]==WHITECHESS&&
			    chess[17]==WHITECHESS&&chess[21]==WHITECHESS &&d[14]==0){
		 d[14]=1;
		num=num+2;
		}
		if(chess[5]==WHITECHESS&&chess[11]==WHITECHESS&&
			    chess[17]==WHITECHESS&&chess[23]==WHITECHESS&&d[15]==0 ){
		 d[15]=1;
		num=num+2;
		}
		if(chess[2]==WHITECHESS&&chess[6]==WHITECHESS&&
		chess[10]==WHITECHESS &&d[16]==0){
		 d[16]=1;
			num=num+1;
		}
		if(chess[2]==WHITECHESS&&chess[8]==WHITECHESS&&
		 chess[14]==WHITECHESS &&d[17]==0){
		 d[17]=1;
		 num=num+1;
		}
		if(chess[10]==WHITECHESS&&chess[16]==WHITECHESS&&
		chess[22]==WHITECHESS&&d[18]==0 ){
		 d[18]=1;
		 num=num+1;
		}
		if(chess[14]==WHITECHESS&&chess[18]==WHITECHESS&&
			chess[22]==WHITECHESS&&d[19]==0 ){
		 d[19]=1;
		 num=num+1;
		}
		int wn=0;
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
       while(!Thread.currentThread().isInterrupted())
       {         
              if(again) {//���¿�ʼ
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
