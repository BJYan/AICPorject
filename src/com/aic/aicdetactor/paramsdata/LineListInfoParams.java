package com.aic.aicdetactor.paramsdata;


/**
 * 线路
 * @author AIC
 *
 */
public class LineListInfoParams {
	private String Name="";
	private String DEADLINE="";
	private String Process="";
	private int Index=0;
	private String Path="";


	
	public String getName(){
		return Name;
	}
	public String getPath(){
		return Path;
	}
	public String getDeadLine(){
		return DEADLINE;
	}
	public String getProcess(){
		return Process;
	}
	public int getIndex(){
		return Index;
	}
	
	public void setName(String name){
		 Name=name;
	}
	
	public void setPath(String path){
		Path=path;
	}
	
	public void setDeadLine(String deadline){
		DEADLINE=deadline;
	}
	public void setProcess(String process){
		 Process=process;
	}
	public void  setIndex(int index){
		Index=index;
	}
	
	public static final String NameConst ="Name";
	public static final String DeadLineConst ="DeadLine";
	public static final String ProcessConst ="Process";
	public static final String IndexConst ="Index";
	
}
