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
	SharedPreferences preferences;//临时数据存储，存放播放音乐信息
	File[] songFiles;
	private static final int APP_EXIT = 9;//返回键值
	private ChessView cv = null;
	boolean eqatbegin=false;
	boolean sum=false;
	boolean blackisgo=false;
	boolean whiteisgo=false;
	boolean aftermove=false;//移动一步
	boolean ifBeginSub=false;//开始捡子
	boolean ifBeginSub2=false;//捡子辅助检测
	boolean isFulls=false;
	int[] a=new int[4];//四个结果数组
	Button regret_y,regret_m,g_m,g_y;
	TextView sub_y,time_y,sub_m,time_m;
	int blacksub,whitesub;
	boolean bool=true;//删子检测判断
	boolean checkIfAgain=false;//重新开局
	int sumb;//黑子总数
	int sumw;//白子总数
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
	        	  //开始捡子操作
	        	  if(ifBeginSub&&!isFull()){
	        		  blacksub=cv.BlackBack();
		              whitesub=cv.WhiteBack();
		              sub_y.setText(Integer.toString(whitesub));
		              sub_m.setText(Integer.toString(blacksub));
	        	  }
	        	  //捡完移动
	              if(blacksub==0&&whitesub==0&&bool==false){
	            	  cv.SetMove(true);
	            	  aftermove=true;
	              }      
	             // 移动后再捡子
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
		inita();//初始化
		showDialog(7);
		cv = (ChessView)findViewById(R.id.chessviewpvsp);
		cv.init();
		sub_y=(TextView)findViewById(R.id.pvsp_pc_tv_result);
		time_y=(TextView)findViewById(R.id.label);
		sub_m=(TextView)findViewById(R.id.pvsp_pc_tv_me_result);
		time_m=(TextView)findViewById(R.id.pc_me_label);
		regret_y=(Button)findViewById(R.id.game_pc_undo);//取消选择按钮
		regret_m=(Button)findViewById(R.id.game_pc_me_undo);//取消选择按钮
		g_m=(Button)findViewById(R.id.game_pc_me_giveup);//认输按钮
		g_y=(Button)findViewById(R.id.game_pc_pickover);;//认输按钮
		sub_y.setText(Integer.toString(0));//可捡子数
		sub_m.setText(Integer.toString(0));//可捡子数
		time_m.setText("BLACK");
		time_y.setText("WHITE");
		regret_y.setOnClickListener(new View.OnClickListener() {//取消选择按钮响应函数
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cv.setRegret();
			}
		});
		regret_m.setOnClickListener(new View.OnClickListener() {//取消选择按钮响应函数
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cv.setRegret();
			}
		});
		g_m.setOnClickListener(new View.OnClickListener() {	//认输按钮响应函数
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(20);
			}
		});
		g_y.setOnClickListener(new View.OnClickListener() {	//认输按钮响应函数
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
		//返回键响应函数，重写返回键
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
                    .setMessage("是否退出游戏?")  
                    .setTitle("用户退出")  
                    .setPositiveButton("确定",  
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
                    .setNegativeButton("取消",  
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
                    .setTitle("再来一局(取消则退出)")  
                    .setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    inita();
                                    cv.init();
                                    isFulls=false;
                                    cv.SetAgain(true);   //重新开局
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
                    .setNegativeButton("取消",  
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
                    .setTitle("再来一局(取消则退出)")  
                    .setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    inita();
                                    cv.init();
                                    isFulls=false;
                                    cv.SetAgain(true);   //重新开局
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
                    .setNegativeButton("取消",  
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
        				.setMessage("1.下子顺序：由黑子先下，白子后下！"
                    		+ "2.捡子：下满之后由白子先捡子，黑子在白子"
                    		+ "剪完后捡子！走子后满足捡子条件即可捡对方子！"
                    		+ "3.走子：捡完子之后由对方走子！"
                    		+ "4.捡子条件：一行或一列五子相连捡3个，对角线"
                    		+ "五子相连捡5个，二级对角线四子相连捡2个，三级"
                    		+ "对角线三子相连捡1个，"
                    		+ "四子分别在一个最小矩形的四个顶点捡1个（注："
                    		+ "每个形状仅能使用一次！）"
                    		+ "5.输赢条件：哪方子先小于3个哪方便输！")  
        				.setTitle("规则介绍")  
        				.setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    	dialog.dismiss();
                                }  
                            })  .create();  
        	}
        	if(id==5){
        		return new AlertDialog.Builder(PvsPActivity.this)  
        				.setMessage("双方均无子可捡！此盘平局!")  
                    	.setTitle("再来一局(取消则退出)")  
                    	.setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                    		public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    inita();
                                    cv.init();
                     			   isFulls=false;
                     			   cv.SetAgain(true);   //重新开局
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
                    	.setNegativeButton("取消",  
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
	        if (id == 20) {  //认输键
	        	return new AlertDialog.Builder(PvsPActivity.this)  
	        			.setMessage("确认认输?!")  
	        			.setTitle("认输将判对方赢！")  
	        			.setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                    showDialog(0);
                                }  
                            })  
	        			.setNegativeButton("取消",  
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
        if (id == 21) {  //Home键
            return new AlertDialog.Builder(PvsPActivity.this)  
                    .setMessage("确认认输?!")  
                    .setTitle("认输将判对方赢！")  
                    .setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                    dialog.dismiss();  
                                   showDialog(3);
                                }  
                            })  
                    .setNegativeButton("取消",  
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
		switch(item.getItemId()){//重新开始
		case R.id.game_again:
		    inita();
		    eqatbegin=false;
			cv.init();
			isFulls=false;
			cv.SetAgain(true);   //重新开局
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
		case R.id.game_stop://停止音乐
			 Intent intent=new Intent(getApplicationContext(),MyMediaService.class);
             stopService(intent);
			break;
		case R.id.game_nt://下一首
			preferences=getSharedPreferences("count", MODE_PRIVATE);
			int sp=preferences.getInt("count", 0);
			File path=new File("/storage/F8E7-AA9C/Android/data/com.netease."
					+ "cloudmusic/files/Documents/Music");//获得手机音乐文件夹
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
			case R.id.game_bf://上一首
			preferences=getSharedPreferences("count", MODE_PRIVATE);
			int spp=preferences.getInt("count", 0);
			File path2=new File("/storage/F8E7-AA9C/Android/data/com.netease.cloudmusic/files/Documents/Music");//获得SD卡的mp3文件夹
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
