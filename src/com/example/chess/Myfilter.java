package com.example.chess;
import java.io.File;
import java.io.FilenameFilter;
class MyFilter implements FilenameFilter {   //ʵ��FilenameFilter �ӿ�
    private String type;  
    public MyFilter(String type){      //���캯��
	this.type = type; 
    } 
    @Override    
    public boolean accept(File dir,String name)  {    //dir��ǰĿ¼, name�ļ���
	return name.endsWith(type);       //����true���ļ���ϸ�
    }
}
