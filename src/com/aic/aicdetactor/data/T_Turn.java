package com.aic.aicdetactor.data;

public class T_Turn {
public static final String RootNodeName="T_Turn";
	
	/**
	 * 轮次数据成员
	 * @author Administrator
	 *
	 */
	public class Turn{
		public   String Time = "";
		public   String Name = "";
		public   String Number = "";
		public   String Start_Time ="";
		public   String T_Line_Guid ="";
		public   String T_Line_Content_Guid ="";
	}
	
	/**
	 * 轮次KEY Name
	 * @author Administrator
	 *
	 */
	public class Turn_Const{
		public static final String Key_End_Time = "End_Time";
		public static final String Key_Name = "Name";
		public static final String Key_Number = "Number";
		public static final String Key_Start_Time ="Start_Time";
		public static final String Key_T_Line_Guid ="T_Line_Guid";
		public static final String Key_T_Line_Content_Guid ="T_Line_Content_Guid";
		}
}
