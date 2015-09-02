package com.aic.aicdetactor.data;

public class T_Worker {
public static final String RootNodeName="T_Worker";
	public Worker mWorker = null;
	public T_Worker(){
		//mWorker = new Worker();
	}
	/**
	 * 工人数据成员
	 * @author Administrator
	 *
	 */
	public class Worker{
		public   String Alias_Name = "";
		public   String Class_Group  = "";
		public   String Mumber  = "";
		public   String Name  ="";
		public   String T_Line_Content_Guid ="";
		public   String T_Line_Guid ="";
		public   String T_Organization_R_Guid  ="";
	}
	
	/**
	 * 工人KEY Name
	 * @author Administrator
	 *
	 */
	public class Worker_Const{		
		public static final   String Key_Alias_Name = "Alias_Name";
		public static final   String Key_Class_Group  = "Class_Group";
		public static final   String Key_Number  = "Number";
		public static final   String Key_Name  ="Name";
		public static final   String Key_T_Line_Content_Guid ="T_Line_Content_Guid";
		public static final   String Key_T_Line_Guid ="T_Line_Guid";
		public static final   String Key_T_Organization_Guid  ="T_Organization_Guid";
		}
}
