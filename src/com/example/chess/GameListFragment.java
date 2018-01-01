package com.example.chess;

import java.io.File;
import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
public class GameListFragment extends Fragment {
	File[] songFiles;
	Button btn_stop;
	int num=0;
	String sp2=null;
	SharedPreferences preferences;//临时数据存储
		public GameListFragment(){
		}
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.history, container, false);	
			ArrayList<String> list_songPath = new ArrayList<String>();   //适配器数据源
			File path=new File("/storage/F8E7-AA9C/Android/data/com"
					+ ".netease.cloudmusic/files/Documents/Music");//获得SD卡的mp3文件夹
			final String sp=path.toString();
			btn_stop=(Button)rootView.findViewById(R.id.his_stop);
			btn_stop.setClickable(false);
			//返回当前目录下所有以.mp3结尾的文件 (文件过滤)      
			songFiles = path.listFiles( new MyFilter(".mp3") );   
			for (File file :songFiles){           	
			    list_songPath.add( (file.getAbsolutePath()).toString().substring(77).replace(".mp3", "") ); //获取文件的绝对路径
			    num++;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					rootView.getContext(),
					android.R.layout.simple_list_item_single_choice,
					list_songPath);
			final ListView li=(ListView)rootView.findViewById(R.id.listView1);
			li.setAdapter(adapter);
			li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			        try{	
			        if(isRunningService()){
			        	Intent intent=new Intent(getActivity(),MyMediaService.class);
			        	getActivity().stopService(intent);
			        }
			        preferences=getActivity().getSharedPreferences("count", Context.MODE_PRIVATE);
			        SharedPreferences.Editor editor=preferences.edit();
		        	   editor.putInt("count", position);
		        	   editor.commit(); 
		        	   btn_stop.setClickable(true);	 
		        	   Intent intent=new Intent(getActivity(),MyMediaService.class);
		        	   intent.putExtra("sp", songFiles[position].toString());		      
		        	   getActivity().startService(intent);
			        }catch (Exception e){ }
			    }	    
				});
			btn_stop.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isRunningService()){
						Intent intent =new Intent(getActivity(),MyMediaService.class);
						getActivity().stopService(intent);
						btn_stop.setClickable(false);
					}
				}	 
			});
			return rootView;
		}
		 private SharedPreferences getSharedPreferences(String string, int mode_PRIVATE) {
			// TODO Auto-generated method stub
			return null;
		}
		public boolean isRunningService(){//服务启动判断函数
				ActivityManager man=(ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
	        	for(RunningServiceInfo ser:man.getRunningServices(Integer.MAX_VALUE)){
	        		if("com.example.chess.MyMediaService".equals(ser.service.getClassName())){
	        		return true;
	        		}
	        	}
				return false;
		 }
		 @Override  
		 public void onCreate(Bundle savedInstanceState) {  
		        super.onCreate(savedInstanceState);  
		  }  
}
