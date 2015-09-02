package com.aic.aicdetactor.data;

public class RoutePeroid {

	String Name;
	String T_Line_Guid;
	int Task_Mode;;
	int Start_Point;
	int Span;
	int Turn_Finish_Mode;
	int T_Period_Unit_Code;
	String Base_Point;
	String Worker_Name;
	String Worker_Number;
	String Class_Group;
	String Turn_Name;
	String Turn_Number;
	String Start_Time;
	String End_Time;
	String File_Guid;
	String Status_Array; // 起停时需要用到。对应内容 "运行/停机/其它"
	String Special_Inspection_Status_Array;
	boolean Is_Omission_Check;
	boolean Is_permission_Time_out;// 是否允许超时。

}
