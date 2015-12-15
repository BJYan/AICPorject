package com.aic.aicdetactor.data;

public class RoutePeroid {

	public String T_Line_Name; //线路名称
	public String T_Period_Name;
	public String T_Line_Guid;
        public String T_Line_Content_Guid ;//2015-12-06添加
	public String T_Turn_Guid;  //2015-12-06添加
	public int Task_Mode;;
	public int Start_Point;
	public int Span;
	public int Turn_Finish_Mode;
	public String T_Period_Unit_Code;
	public String Base_Point;
	public String Worker_Name;
	public String Worker_Number;
	public String Class_Group;
	public String Turn_Name;
	public int Turn_Number;
	public String Start_Time;
	public String End_Time;
	public String File_Guid; //要巡检的文件
//	public String Special_Inspection_Status_Array;
	public int Is_Omission_Check;
	public int Is_Permission_Timeout;// 是否允许超时。
	public int Is_Special_Inspection;
	
    public String T_Worker_Guid;  //人员Guid ,2015-12-06添加, 第一次谁检的填谁的GUID。
   // public String Check_Datetime;  //2015-12-06添加, 第一次检的日期 时间
	


}
