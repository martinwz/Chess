package com.example.chess;
import static com.example.chess.Constants.NOCHESS;
import java.io.File;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class PvsPActivity extends Activity {
	SharedPreferences preferences;//��ʱ���ݴ洢����Ų���������Ϣ
	File[] songFiles;
	private static final int APP_EXIT = 9;//���ؼ�ֵ
	private ChessView cv = null;
	boolean eqatbegin=false;
	boolean sum=false;
	boolean blackisgo=false;
	boolean whiteisgo=false;
	boolean aftermove=false;//�ƶ�һ��
	boolean ifBeginSub=false;//��ʼ����
	boolean ifBeginSub2=false;//���Ӹ������
	boolean isFulls=false;
	int[] a=new int[4];//�ĸ��������
	Button regret_y,regret_m,g_m,g_y;
	TextView sub_y,time_y,sub_m,time_m;
	int blacksub,whitesub;
	boolean bool=true;//ɾ�Ӽ���ж�
	boolean checkIfAgain=false;//���¿���
	int sumb;//��������
	int sumw;//��������
	Handler mHandler = new Handler(){
	        @Override   
	      public void handleMessage(Message msg){  
	          switch (msg.what){
	          case 0:
	        	  if((isFulls==false&&isFull())||checkIfAgain){
	        	  blacksub=cv.BlackBack();
	              whitesub=cv.WhiteBack();
	              sub_y.setText(Integer.toString(whitesub));
	              sub_m.setText(Integer.toString(blacksub));
	              if(blacksub==0&&whitesub==0	&&!checkIfAgain){
	            	  showDialog(5); 
	              }
	              checkIfAgain=false;
	              if(isFull()){
	            	  ifBeginSub=true;
	            	  isFulls=true;
	            	  sum=true;
	              }
	          }
	        	  if(sum){
		              sumb=cv.sumAndBackBlack();
		              sumw=cv.sumAndBackWhite();
		              if(whitesub>=sumb&&whitesub!=0&&sumb!=0&&a[1]==0&&a[2]==0&&a[3]==0){
		            	  sum=false;
		            	  a[0]=1;
		            	  showDialog(0); 
		              }
		              if(blacksub>=sumw&&blacksub!=0&&sumw!=0&&a[0]==0&&a[2]==0&&a[3]==0){
		            	  sum=false;
		            	  a[1]=1;
		            	  showDialog(3);
		              }
		              if(sumb<3&&a[1]==0&&a[0]==0&&a[3]==0&&sumb!=0){
		            	  sum=false;
		            	  a[2]=1;
		            	  showDialog(0); 
		              }
		              if(sumw<3&&a[1]==0&&a[2]==0&&a[0]==0&&sumw!=0){
		            	  sum=false;
		            	  a[3]=1;
		            	  showDialog(3); 
		              }
	        	  }
	        	  //��ʼ���Ӳ���
	        	  if(ifBeginSub&&!isFull()){
	        		  blacksub=cv.BlackBack();
		              whitesub=cv.WhiteBack();
		              sub_y.setText(Integer.toString(whitesub));
		              sub_m.setText(Integer.toString(blacksub));
	        	  }
	        	  //�����ƶ�
	              if(blacksub==0&&whitesub==0&&bool==false){
	            	  cv.SetMove(true);
	            	  aftermove=true;
	              }      
	             // �ƶ����ټ���
	              if((blacksub>0||whitesub>0)&&aftermove){
	            	  cv.SetAgain2(true);
	            	  ifBeginSub=true;
	        		  aftermove=false;
	              }
	            break;
	          case 1:
	        	  if(isFull()&&bool){
	        		  cv.SetAgain2(true);
	        		  bool=false;
	        	  }
	        	  break;
	        default:
	          break;
	          }  
	      }  
	      };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game );
		inita();//��ʼ��
		showDialog(7);
		cv = (ChessView)findViewById(R.id.chessviewpvsp);
		cv.init();
		sub_y=(TextView)findViewById(R.id.pvsp_pc_tv_result);
		time_y=(TextView)findViewById(R.id.label);
		sub_m=(TextView)findViewById(R.id.pvsp_pc_tv_me_result);
		time_m=(TextView)findViewById(R.id.pc_me_label);
		regret_y=(Button)findViewById(R.id.game_pc_undo);//ȡ��ѡ��ť
		regret_m=(Button)findViewById(R.id.game_pc_me_undo);//ȡ��ѡ��ť
		g_m=(Button)findViewById(R.id.game_pc_me_giveup);//���䰴ť
		g_y=(Button)findViewById(R.id.game_pc_pickover);;//���䰴ť
		sub_y.setText(Integer.toString(0));//�ɼ�����
		sub_m.setText(Integer.toString(0));//�ɼ�����
		time_m.setText("BLACK");
		time_y.setText("WHITE");
		regret_y.setOnClickListener(new View.OnClickListener() {//ȡ��ѡ��ť��Ӧ����
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cv.setRegret();
			}
		});
		regret_m.setOnClickListener(new View.OnClickListener() {//ȡ��ѡ��ť��Ӧ����
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cv.setRegret();
			}
		});
		g_m.setOnClickListener(new View.OnClickListener() {	//���䰴ť��Ӧ����
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(20);
			}
		});
		g_y.setOnClickListener(new View.OnClickListener() {	//���䰴ť��Ӧ����
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(21);
			}
		});
       if(cv.isEnding()){
	    	  cv.SetAgain2(true);
	    	  Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
	      }
       final int milliseconds = 100;
	     new Thread(){
	        @Override
	        public void run(){
	          while(true){  
	        try {  
	            sleep(milliseconds);  
	        } catch (InterruptedException e) {   e.printStackTrace();    }  
	        mHandler.sendEmptyMessage(0);  
	          }  
	        }
	      }.start();
	      new Thread(){
		        @Override
		        public void run(){
		          while(true){  
		        try {  
		            sleep(5000);  
		        } catch (InterruptedException e) {   e.printStackTrace();    }  
		        mHandler.sendEmptyMessage(1);  
		          }  
		        }
		      }.start();
		}
	public Boolean isFull(){
		for(int i=0;i<25;i++){
			if(cv.chess[i]==NOCHESS){
				return false;
			}
		}
		return true;
	}
	
	public void inita(){
		a[0]=0;
		a[1]=0;
		a[2]=0;
		a[3]=0;
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
		//���ؼ���Ӧ��������д���ؼ�
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            showDialog(APP_EXIT);  
            return true;  
        } else  
            return super.onKeyDown(keyCode, event);  
    }  
    @Override  
    protected Dialog onCreateDialog(int id) {  
        if (id == APP_EXIT) {  
            return new AlertDialog.Builder(PvsPActivity.this)  
                    .setMessage("�Ƿ��˳���Ϸ?")  
                    .setTitle("�û��˳�")  
                    .setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
                                    stopService(intent);
                                    android.os.Process  
                                       .killProcess(android.os.Process  
                                            .myPid());  
                                    finish();  
  
                                }  
                            })  
                    .setNegativeButton("ȡ��",  
                            new DialogInterface.OnClickListener() {  
  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                }  
                            }).create();  
        }       
        if(id==3){
        	return new AlertDialog.Builder(PvsPActivity.this)  
                    .setMessage("Game Over! Black Win!")  
                    .setTitle("����һ��(ȡ�����˳�)")  
                    .setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    inita();
                                    cv.init();
                                    isFulls=false;
                                    cv.SetAgain(true);   //���¿���
                                    cv.Setsubnum(0, 0);
                                    checkIfAgain=true;
                                    bool=true;
                                    ifBeginSub=false;
                                    blackisgo=false;
                                    eqatbegin=false;
                                    whiteisgo=false;
                                    aftermove=false;
                                    sum=false;
                                    blacksub=0;
                                    whitesub=0;
                                }  
                            })  
                    .setNegativeButton("ȡ��",  
                            new DialogInterface.OnClickListener() {   
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
                                    stopService(intent);
                                    android.os.Process  
                                    .killProcess(android.os.Process  
                                            .myPid());  
                                    finish();  
                                }  
                            }).create();  
        }
        if(id==0){
        	return new AlertDialog.Builder(PvsPActivity.this)  
                    .setMessage("Game Over! White Win!")  
                    .setTitle("����һ��(ȡ�����˳�)")  
                    .setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    inita();
                                    cv.init();
                                    isFulls=false;
                                    cv.SetAgain(true);   //���¿���
                                    cv.Setsubnum(0, 0);
                                    checkIfAgain=true;
                                    eqatbegin=false;
                                    bool=true;
                                    ifBeginSub=false;
                                    blackisgo=false;
                                    whiteisgo=false;
                             	   	aftermove=false;
                             	   	sum=false;
                                }  
                            })  
                    .setNegativeButton("ȡ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
                                    stopService(intent);
                                    android.os.Process  
                                    .killProcess(android.os.Process  
                                          .myPid());  
                                    finish();  
                                }  
                            }).create();  
        	}
        	if(id==7){
        		return new AlertDialog.Builder(PvsPActivity.this)  
        				.setMessage("1.����˳���ɺ������£����Ӻ��£�"
                    		+ "2.���ӣ�����֮���ɰ����ȼ��ӣ������ڰ���"
                    		+ "�������ӣ����Ӻ���������������ɼ�Է��ӣ�"
                    		+ "3.���ӣ�������֮���ɶԷ����ӣ�"
                    		+ "4.����������һ�л�һ������������3�����Խ���"
                    		+ "����������5���������Խ�������������2��������"
                    		+ "�Խ�������������1����"
                    		+ "���ӷֱ���һ����С���ε��ĸ������1����ע��"
                    		+ "ÿ����״����ʹ��һ�Σ���"
                    		+ "5.��Ӯ�������ķ�����С��3���ķ����䣡")  
        				.setTitle("�������")  
        				.setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    	dialog.dismiss();
                                }  
                            })  .create();  
        	}
        	if(id==5){
        		return new AlertDialog.Builder(PvsPActivity.this)  
        				.setMessage("˫�������ӿɼ񣡴���ƽ��!")  
                    	.setTitle("����һ��(ȡ�����˳�)")  
                    	.setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                    		public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    inita();
                                    cv.init();
                     			   isFulls=false;
                     			   cv.SetAgain(true);   //���¿���
                     			   cv.Setsubnum(0, 0);
                     			   checkIfAgain=true;
                     			   eqatbegin=false;
                     			   bool=true;
                     			   ifBeginSub=false;
                                   blackisgo=false;
                             	   whiteisgo=false;
                             	   aftermove=false;
                             	   sum=false;
                             	   blacksub=0;
                             	   whitesub=0;
                                }  
                            })  
                    	.setNegativeButton("ȡ��",  
                    			new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
                                    stopService(intent);
                                    android.os.Process  
                                    .killProcess(android.os.Process  
                                           .myPid());  
                                    finish();  
                                }  
                            }).create();  
	        }
	        if (id == 20) {  //�����
	        	return new AlertDialog.Builder(PvsPActivity.this)  
	        			.setMessage("ȷ������?!")  
	        			.setTitle("���佫�жԷ�Ӯ��")  
	        			.setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    showDialog(0);
                                }  
                            })  
	        			.setNegativeButton("ȡ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
                                    stopService(intent);
                                    android.os.Process  
                                    .killProcess(android.os.Process  
                                         .myPid());  
                                    finish();  
                                }  
                            }).create();  
        }  
        if (id == 21) {  //Home��
            return new AlertDialog.Builder(PvsPActivity.this)  
                    .setMessage("ȷ������?!")  
                    .setTitle("���佫�жԷ�Ӯ��")  
                    .setPositiveButton("ȷ��",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                   showDialog(3);
                                }  
                            })  
                    .setNegativeButton("ȡ��",  
                            new DialogInterface.OnClickListener() {  
  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
                                    stopService(intent);
                                    android.os.Process  
                                    .killProcess(android.os.Process  
                                            .myPid());  
                                    finish();  
                                }  
                            }).create();   
        }  
        return null;  
    }  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	public boolean isRunningService(){
		ActivityManager man=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    	for(RunningServiceInfo ser:man.getRunningServices(Integer.MAX_VALUE)){
    		if("com.example.chess.MyMediaService".equals(ser.service.getClassName())){
    		return true;
    		}
    	}
		return false;
 }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){//���¿�ʼ
		case R.id.game_again:
		    inita();
		    eqatbegin=false;
			cv.init();
			isFulls=false;
			cv.SetAgain(true);   //���¿���
			cv.Setsubnum(0, 0);
			checkIfAgain=true;
            bool=true;
            ifBeginSub=false;
            blackisgo=false;
        	whiteisgo=false;
        	aftermove=false;
        	sum=false;
        	blacksub=0;
        	whitesub=0;
            cv.SetMove(false);
			break;
		case R.id.game_stop://ֹͣ����
			 Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
             stopService(intent);
			break;
		case R.id.game_nt://��һ��
			preferences=getSharedPreferences("count", MODE_PRIVATE);
			int sp=preferences.getInt("count", 0);
			File path=new File("/storage/F8E7-AA9C/Android/data/com.netease."
					+ "cloudmusic/files/Documents/Music");//����ֻ������ļ���
			songFiles = path.listFiles( new MyFilter(".mp3") );   
			if(sp==(songFiles.length-1)){
				sp=-1;
			}
			String sp2=songFiles[sp+1].toString();
			if(isRunningService()){
			Intent intent2=new Intent(getApplicationContext(),MyMediaService.class);
            stopService(intent2);
			}
			Intent intent2=new Intent(getApplicationContext(),MyMediaService.class);
			intent2.putExtra("sp", sp2);
            startService(intent2);
            SharedPreferences.Editor editor=preferences.edit();
     	    editor.putInt("count", sp+1);
     	    editor.commit();
			break;
			case R.id.game_bf://��һ��
			preferences=getSharedPreferences("count", MODE_PRIVATE);
			int spp=preferences.getInt("count", 0);
			File path2=new File("/storage/F8E7-AA9C/Android/data/com.netease.cloudmusic/files/Documents/Music");//���SD����mp3�ļ���
			songFiles = path2.listFiles( new MyFilter(".mp3") );   
			if(spp==0){
				spp=songFiles.length;
			}
			String spp2=songFiles[spp-1].toString();
			if(isRunningService()){
			Intent intent32=new Intent(getApplicationContext(),MyMediaService.class);
            stopService(intent32);
			}
			Intent intent42=new Intent(getApplicationContext(),MyMediaService.class);
			intent42.putExtra("sp", spp2);
            startService(intent42);
            SharedPreferences.Editor editor2=preferences.edit();
     	   editor2.putInt("count", spp-1);
     	   editor2.commit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
