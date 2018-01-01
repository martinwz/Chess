package com.example.chess;
import com.example.chess.ChessView;
import com.example.chess.R;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
public class Game extends Activity {
	Button again,rr,ss;
	 int APP_EXIT=9;
	SharedPreferences preferences;
	int a=1;
	RadioGroup main_rg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			 setContentView(R.layout.activity_main);
			 main_rg=(RadioGroup)findViewById(R.id.rg_tab);
				getFragmentManager().beginTransaction().replace(R.id.fl_content, new GameFragment()).commit();
				main_rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch(checkedId){
						case R.id.rb_home:
							getFragmentManager().beginTransaction().replace(R.id.fl_content, new GameFragment()).commit();
							break;
						case R.id.rb_meassage:
							getFragmentManager().beginTransaction().replace(R.id.fl_content, new GameListFragment()).commit();
							break;
						default:
							break;
						}
					}
				});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	  protected Dialog onCreateDialog(int id) {
		     if (id == APP_EXIT) {  
		            return new AlertDialog.Builder(Game.this)  
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
		     if (id == 0) {  
		            return new AlertDialog.Builder(Game.this)  
		                    .setMessage("版本:v1.0\n"
		                    		+ "发布日期:2016-11-13")  
		                    .setTitle("版本信息")  
		                    .setPositiveButton("确定",  
		                            new DialogInterface.OnClickListener() {  
		                                public void onClick(DialogInterface dialog,  
		                                        int which) {  
		                                    dialog.dismiss();  
		                                }  
		                            }) .create();  
		        }   
		return null;  
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
		int id = item.getItemId();
		if (id == R.id.message) {
			showDialog(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
