package com.aic.aicdetactor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	 private final static String DB_NAME ="aicdatabase.db";//数据库名
	 private final static int VERSION = 7;//版本号
	 
	  
	//保存从服务器接收到的原始巡检数据信息
	// public static String TABLE_SOURCE_FILE = "jxcheck";
	 public static String TABLE_SOURCE_FILE = "T_Original_Json_File";
	 public class SourceTable{	 
	 //巡检名称，在ListView 中显示的巡检路径名称
	 public static final String PLANNAME="T_Line_Name";
	 //巡检GUID,同时是JSON的文件名称
	 public static final String PLANGUID="T_Line_Guid";
	//文件的存储路径
	 public static final String Path ="Path";
	 
	 }

	 public static String MEDIA_PATH_NAME = "filePath";
	 public static String TABLE_NAME_AUDIO = "audio";
	 public class Media_audio_Table{
		 public static final String Name = "Name";
		 public static final String filePath = "filePath";
		 public static final String duration = "duration";		 
		 public static final String addTime = "addTime";
	 }
	 public static String TABLE_NAME_TEXTRECORD = "text";
	 public class Media_textRecord_Table{		
		 public static final String Name = "Name";
		 public static final String filePath = "filePath";
		 public static final String content = "content";		 
		 public static final String addTime = "addTime";
	 }
	 public static String TABLE_NAME_PICTRUE = "pictrue";
	 public class Media_pic_Table{
		 public static final String Name = "Name";
		 public static final String filePath = "filePath";			 
		 public static final String addTime = "addTime";
	 }
	 
	 public static String TABLE_WORKERS = "workers";
	 public class Plan_Worker_Table{
		public static final   String Alias_Name = "Alias_Name";
		public static final   String Class_Group  = "Class_Group";
		public static final   String Number  = "Number";
		public static final   String Name  ="Name";
		public static final   String T_Line_Content_Guid ="T_Line_Content_Guid";
		public static final   String T_Line_Guid ="T_Line_Guid";
		public static final   String T_Organization_Guid  ="T_Organization_Guid";
		public static final  String Pwd ="pwd";
		public static final  String IsModifyPwd = "IsModifyPwd";
		public static final  String newPwd = "newPwd";
	 }
	 public static String TABLE_TURN = "TurnInfo";
	 public class Plan_Turn_Table{
		public static final String End_Time = "End_Time";
		public static final String Name = "Name";
		public static final String Number = "Number";
		public static final String Start_Time ="Start_Time";
		public static final String T_Line_Guid ="T_Line_Guid";
		public static final String T_Line_Content_Guid ="T_Line_Content_Guid";
		
	 }
	 
	 
	// public static String TABLE_CHECKING = "checkFile";
	 public static String TABLE_CHECKING = "T_Upload_Json_File";
	 public class Checking_Table{ 
		 public static final String T_Line_Guid = "T_Line_Guid";
		 public static final String T_Line_Name = "T_Line_Name";		 	 
		 public static final String Task_Mode = "Task_Mode";
		 public static final String Start_Point = "Start_Point";		 
		 public static final String Span = "Span";
		 public static final String Turn_Finish_Mode = "Turn_Finish_Mode";		 
		 public static final String T_Period_Unit_Code = "T_Period_Unit_Code";
		 public static final String Base_Point = "Base_Point";		 
		 public static final String Worker_Name = "Worker_Name";
		 public static final String Worker_Number = "Worker_Number";
		 public static final String Class_Group = "Class_Group";
		 public static final String Turn_Name = "Turn_Name";		 
		 public static final String Turn_Number = "Turn_Number";
		 public static final String Date = "Date";	
		 public static final String File_Guid = "File_Guid";		 
		 public static final String Is_Uploaded = "Is_Uploaded";
		 public static final String Is_Updateed = "Is_Updateed";
	 } 
	 
	 public static String TABLE_TEMPORARY = "T_Temporary_Line_Json";  
	 public class Temporary_Table{ 
		 public static final String Title = "Title";
		 public static final String Content = "Content";		 	 
		 public static final String GroupName = "GroupName";
		 public static final String CorporationName = "CorporationName";		 
		 public static final String WorkShopName = "WorkShopName";
		 public static final String DevName = "DevName";		 
		 public static final String DevSN = "DevSN";
		 public static final String Task_Mode = "Task_Mode";		 
		 public static final String T_Worker_R_Name = "T_Worker_R_Name";
		 public static final String T_Worker_R_Mumber = "T_Worker_R_Mumber";
		 public static final String Create_Time = "Create_Time";
		 public static final String Receive_Time = "Receive_Time";		 
		 public static final String Feedback_Time = "Feedback_Time";
		 public static final String Execution_Time = "Execution_Time";	
		 public static final String Finish_Time = "Finish_Time";		 
		 public static final String Result = "Result";
		 public static final String Remarks = "Remarks"; 
		 public static final String Unit = "Unit";		 
		 public static final String T_Measure_Type_Id = "T_Measure_Type_Id";
		 public static final String T_Measure_Type_Code = "T_Measure_Type_Code";		 
		 public static final String T_Item_Abnormal_Grade_Id = "T_Item_Abnormal_Grade_Id";
		 public static final String T_Item_Abnormal_Grade_Code = "T_Item_Abnormal_Grade_Code";
		 public static final String UpLimit = "UpLimit";
		 public static final String Middle_Limit = "Middle_Limit";		 
		 public static final String DownLimit = "DownLimit";
		 public static final String T_Temporary_Line_Guid = "T_Temporary_Line_Guid";	
		 public static final String Guid = "Guid";		 
		 public static final String Is_Original_Line = "Is_Original_Line";
		 public static final String Is_Readed = "Is_Readed";
	 } 
	 //T_Organization
	 public static String TABLE_T_Organization_CorporationName = "T_Organization_CorporationName";  
	 public class Organization_CorporationName_Table{ 
		 public static final String CorporationName = "CorporationName";
	 } 
	 
	 //GroupName
	 public static String TABLE_T_Organization_GroupName = "T_Organization_GroupName";  
	 public class Organization_GroupName_Table{ 
		 public static final String GroupName = "GroupName";
	 } 
	 
	 //WorkShopName
	 public static String TABLE_T_Organization_WorkShopName = "T_Organization_WorkShopName";  
	 public class Organization_WorkShopName_Table{ 
		 public static final String WorkShopName = "WorkShopName";
	 } 
	 
	//Periods
		 public static String TABLE_Periods = "Periods";  
		 public class Periods_Table{ 
			 public static final String Is_Omission_Check = "Is_Omission_Check";
			 public static final String Is_Permission_Timeout = "Is_Permission_Timeout";
			 public static final String Span = "Span";
			 public static final String Start_Point = "Start_Point";
			 public static final String T_Turn_Number_Array = "T_Turn_Number_Array";
			 public static final String	 Task_Mode = "Task_Mode";
			 public static final String	 Turn_Finish_Mode = "Turn_Finish_Mode";
			 public static final String Line_Guid = "Line_Guid";
		 } 
	 //自带的构造方法
	 public DBHelper(Context context, String name, CursorFactory factory,
	   int version) {
	  super(context, name, factory, version);
	 }

	 //为了每次构造时不用传入dbName和版本号，自己得新定义一个构造方法
	 public DBHelper(Context cxt){
	  this(cxt, DB_NAME, null, VERSION);//调用上面的构造方法
	 }

	 //版本变更时
	 public DBHelper(Context cxt,int version) {
	  this(cxt,DB_NAME,null,version);
	 }

	 //当数据库创建的时候调用
	public void onCreate(SQLiteDatabase db) {
		
		//save file that from server
		String jxchecksql = "create table IF NOT EXISTS "+TABLE_SOURCE_FILE
				+"("
				+ SourceTable.Path +" varchar(256),"
				+ SourceTable.PLANNAME +" varchar(256),"
				+  SourceTable.PLANGUID + " varchar(256) PRIMARY KEY"				
				+")";

		db.execSQL(jxchecksql);

		//media some table picture
		String pictruesql = "create table IF NOT EXISTS " + TABLE_NAME_PICTRUE
				+ "(" + "Name varchar(256)," + "filePath varchar(256),"
				+ "addTime varchar(24)" + ")";

		db.execSQL(pictruesql);

		//media some table audio
		String audiosql = "create table IF NOT EXISTS " + TABLE_NAME_AUDIO
				+ "("+ "Name varchar(256)," + "filePath varchar(256),"
				+ "duration varchar(32)," + "addTime varchar(24)" + ")";

		db.execSQL(audiosql);

		//media some table txt info
		String textRecordsql = "create table IF NOT EXISTS "
				+ TABLE_NAME_TEXTRECORD + "(" + "Name varchar(256),"
				+ "filePath varchar(256)," + "content varchar,"
				+ "addTime varchar(24)"+")";

		db.execSQL(textRecordsql);
		
		//parse json and save worker information
		String workerSql = "create table IF NOT EXISTS "
				+ TABLE_WORKERS 
				+ "(" 
				+ Plan_Worker_Table.Name+" varchar(256),"
				+ Plan_Worker_Table.Alias_Name+" varchar(256)," 
				+ Plan_Worker_Table.Class_Group +" varchar,"
				+ Plan_Worker_Table.Number +" varchar,"
				+ Plan_Worker_Table.T_Line_Content_Guid + " varchar(256),"
				+ Plan_Worker_Table.T_Line_Guid + " varchar(128),"
				+ Plan_Worker_Table.T_Organization_Guid + " varchar(256),"
				+ Plan_Worker_Table.Pwd +" varchar(256),"
				+ Plan_Worker_Table.IsModifyPwd +" BOOLEAN ,"
				+ Plan_Worker_Table.newPwd +" varchar(256)"
				+")";

		db.execSQL(workerSql);
		
		//save checking data 
		String checkingsql = "create table IF NOT EXISTS "
				+ TABLE_CHECKING 
				+ "(" 
				+ Checking_Table.T_Line_Guid +" varchar(256) PRIMARY KEY,"
				+ Checking_Table.T_Line_Name +" varchar(256),"
				+ Checking_Table.Task_Mode +" varchar(256),"
				+ Checking_Table.Start_Point +" varchar(256),"
				+ Checking_Table.Span +" varchar(24),"
				+ Checking_Table.Turn_Finish_Mode +" varchar(128),"
				+ Checking_Table.T_Period_Unit_Code +" varchar(128),"
				
				+ Checking_Table.Base_Point +" varchar(256),"
				+ Checking_Table.Worker_Name +" varchar(256),"
				+ Checking_Table.Worker_Number +" varchar(24),"
				+ Checking_Table.Class_Group +" varchar(128),"
				+ Checking_Table.Turn_Name +" varchar(128),"
				+ Checking_Table.File_Guid +" varchar(256),"
				+ Checking_Table.Turn_Number +" varchar(256),"
				+ Checking_Table.Date +" varchar(256),"
				+ Checking_Table.Is_Uploaded +" BOOLEAN,"
				+ Checking_Table.Is_Updateed +" BOOLEAN "				
				+")";

		db.execSQL(checkingsql);
		
		
		//turn info witch from json txt
		String turnSql = "create table IF NOT EXISTS "
				+ TABLE_TURN 
				+ "(" 
				+ Plan_Turn_Table.Number+" varchar(256),"
				+ Plan_Turn_Table.End_Time+" varchar(16)," 
				+ Plan_Turn_Table.Name +" varchar(16),"
				+ Plan_Turn_Table.Start_Time +" varchar(8),"
				+ Plan_Turn_Table.T_Line_Content_Guid + " varchar(256),"
				+ Plan_Turn_Table.T_Line_Guid +" varchar(16)"
				
				+")";

		db.execSQL(turnSql);
		
		
		
		String temporartSql = "create table IF NOT EXISTS "
				+ TABLE_TEMPORARY 
				+ "(" 
				+ Temporary_Table.Title+" varchar(256),"
				+ Temporary_Table.Content+" varchar(400)," 
				+ Temporary_Table.GroupName +" varchar(16),"
				+ Temporary_Table.CorporationName +" varchar(8),"
				+ Temporary_Table.WorkShopName + " varchar(256),"				
				+ Temporary_Table.DevName+" varchar(256),"
				+ Temporary_Table.DevSN+" varchar(400)," 
				+ Temporary_Table.Task_Mode +" INT,"
				+ Temporary_Table.T_Worker_R_Name +" varchar(8),"
				+ Temporary_Table.T_Worker_R_Mumber + " varchar(256),"
				+ Temporary_Table.Create_Time+" varchar(256),"
				+ Temporary_Table.Receive_Time+" varchar(400)," 
				+ Temporary_Table.Feedback_Time +" varchar(16),"
				+ Temporary_Table.Execution_Time +" varchar(8),"
				+ Temporary_Table.Finish_Time + " varchar(256),"
				+ Temporary_Table.Result+" varchar(256),"
				+ Temporary_Table.Remarks+" varchar(400)," 
				+ Temporary_Table.Unit +" varchar(16),"
				+ Temporary_Table.T_Measure_Type_Id +" INT,"
				+ Temporary_Table.T_Measure_Type_Code + " varchar(256),"				
				+ Temporary_Table.T_Item_Abnormal_Grade_Id+" INT," 
				+ Temporary_Table.T_Item_Abnormal_Grade_Code +" varchar(16),"
				+ Temporary_Table.UpLimit +" FLOAT,"
				+ Temporary_Table.Middle_Limit + " FLOAT,"
				+ Temporary_Table.DownLimit+" FLOAT,"
				+ Temporary_Table.T_Temporary_Line_Guid+" varchar(400)," 
				+ Temporary_Table.Guid +" varchar(16),"
				+ Temporary_Table.Is_Original_Line +" BOOLEAN,"
				+ Temporary_Table.Is_Readed + " BOOLEAN"				
				+")";

		db.execSQL(temporartSql);
		
		
		
		//Organization_CorporationName_Table
		String Oragniation_corpSql = "create table IF NOT EXISTS "
				+ TABLE_T_Organization_CorporationName 
				+ "(" 
				+ Organization_CorporationName_Table.CorporationName+" varchar(256) PRIMARY KEY"				
				+")";
		db.execSQL(Oragniation_corpSql);
		
		//TABLE_T_Organization_GroupName
		String Organization_GrouSql = "create table IF NOT EXISTS "
				+ TABLE_T_Organization_GroupName 
				+ "(" 
				+ Organization_GroupName_Table.GroupName+" varchar(256) PRIMARY KEY"				
				+")";
		db.execSQL(Organization_GrouSql);
		
		//TABLE_T_Organization_WorkShopName
		String Organization_WorkShopNameSql = "create table IF NOT EXISTS "
				+ TABLE_T_Organization_WorkShopName 
				+ "(" 
				+ Organization_WorkShopName_Table.WorkShopName+" varchar(256) PRIMARY KEY"				
				+")";
		db.execSQL(Organization_WorkShopNameSql);

		//Period Table
		String PeriodSql = "create table IF NOT EXISTS "
				+ TABLE_Periods 
				+ "(" 
				+ Periods_Table.Is_Omission_Check+" varchar(256) ,"	
				+ Periods_Table.Is_Permission_Timeout+" varchar(256) ,"	
				+ Periods_Table.Line_Guid+" varchar(256) ,"	
				+ Periods_Table.Span+" varchar(256) ,"	
				+ Periods_Table.Start_Point+" varchar(256) ,"	
				+ Periods_Table.T_Turn_Number_Array+" varchar(256), "	
				+ Periods_Table.Task_Mode+" varchar(256), "
				+ Periods_Table.Turn_Finish_Mode+" varchar(256) "
				+")";
		db.execSQL(PeriodSql);

	}

	 //版本更新时调用
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  String sql  = "DROP TABLE IF EXISTS jxcheck";
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_NAME_PICTRUE;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_NAME_AUDIO;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_NAME_TEXTRECORD;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_WORKERS;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_CHECKING;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_TURN;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_TEMPORARY;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_T_Organization_CorporationName;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_T_Organization_GroupName;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_T_Organization_WorkShopName;
	  db.execSQL(sql);
	  
	  sql  = "DROP TABLE IF EXISTS "+TABLE_Periods;
	  db.execSQL(sql);
	  
	 }
	 
	 

	 @Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}

	}
