package com.example.chess;
import java.io.File;
import java.io.FilenameFilter;
class MyFilter implements FilenameFilter {   //实现FilenameFilter 接口
    private String type;  
    public MyFilter(String type){      //构造函数
	this.type = type; 
    } 
    @Override    
    public boolean accept(File dir,String name)  {    //dir当前目录, name文件名
	return name.endsWith(type);       //返回true的文件则合格
    }
}
