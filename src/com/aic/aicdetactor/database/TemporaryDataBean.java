package com.aic.aicdetactor.database;

public class TemporaryDataBean {
	 public  String Title = null;
	 public  String Content = null;		 	 
	 public  String GroupName = null;
	 public  String CorporationName = null;		 
	 public  String WorkShopName = null;
	 public  String DevName = null;		 
	 public  String DevSN = null;
	 public  String Task_Mode = null;		 
	 public  String T_Worker_R_Name = null;
	 public  String T_Worker_R_Mumber = null;
	 public  String Create_Time = null;
	 public  String Receive_Time = null;		 
	 public  String Feedback_Time = null;
	 public  String Execution_Time = null;	
	 public  String Finish_Time = null;		 
	 public  String Result = null;
	 public  String Remarks = null; 
	 public  String Unit = null;		 
	 public  int T_Measure_Type_Id = 0;
	 public  String T_Measure_Type_Code = null;		 
	 public  int T_Item_Abnormal_Grade_Id = 0;
	 public  String T_Item_Abnormal_Grade_Code = null;
	 public  float UpLimit = 0.0f;
	 public  float Middle_Limit = 0.0f;		 
	 public  float DownLimit = 0.0f;
	 public  String T_Temporary_Line_Guid = null;	
	 public  String Guid = null;		 
	 public  boolean Is_Original_Line = false;
	 public  boolean Is_Readed = false;
	 
	 
	 public TemporaryDataBean(){
		 
	 }
	 
	 public String ToStrting(){
		 String str = null;
		 return str;
	 }
}
