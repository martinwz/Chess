package com.example.chess;
import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.media.MediaPlayer;  
public class MyMediaService extends Service implements MediaPlayer.OnCompletionListener{  
    MediaPlayer myPlayer;  
    String data=null;
    private final IBinder binder = new AudioBinder();  
    @Override  
    public IBinder onBind(Intent arg0) {  
        // TODO Auto-generated method stub  
        return binder;  
    }  
    /** 
     * ��Audio�������ʱ�򴥷��ö��� 
     */  
    @Override  
    public void onCompletion(MediaPlayer player) {  
        // TODO Auto-generated method stub  
        stopSelf();//�����ˣ������Service  
    }  
    //ʵ����MediaPlayer����  
    public void onCreate(){  
        super.onCreate();  
        //���Ǵ�raw�ļ����л�ȡһ��Ӧ���Դ���mp3�ļ�  
       myPlayer=new MediaPlayer();
        myPlayer.setOnCompletionListener(this);  
        myPlayer.setLooping(true);//���õ���ѭ��
    }  
    public int onStartCommand(Intent intent, int flags, int startId){  
    	data=intent.getStringExtra("sp");
    	String currentSong=data.substring(77).replace(".mp3", "");
    	Toast.makeText(getApplicationContext(),"��ǰ���ŵ��ǣ�" 
    	+currentSong, Toast.LENGTH_SHORT).show();
    	if(myPlayer.isPlaying()){
    		myPlayer.stop();
    		myPlayer.release();
    		try {
				myPlayer.setDataSource(data);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		try {
				myPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else{
    		try {
				myPlayer.setDataSource(data);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				myPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     //׼��
    	}
        	myPlayer.start(); //����
        return START_STICKY;  
    }  
    public void onDestroy(){  //ֹͣ����
        super.onDestroy();  
        if(myPlayer.isPlaying()){  
            myPlayer.stop();  
        }  
        myPlayer.release();  
    }  
    //Ϊ�˺�Activity������������Ҫ����һ��Binder����  
    class AudioBinder extends Binder{  
        //����Service����  
        MyMediaService getService(){  
            return MyMediaService.this;  
        }  
    }  
}  