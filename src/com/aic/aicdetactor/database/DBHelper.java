package com.aic.aicdetactor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	 private final static String DB_NAME ="aicdatabase.db";//数据库名
	 private final static int VERSION = 5;//版本号
	 
	  
	//保存从服务器接收到的原始巡检数据信息
	// public static String TABLE_SOURCE_FILE = "jxcheck";
	 public static String TABLE_SOURCE_FILE = "T_Original_Json_File";
	 public class SourceTable{
	 public static final String GUID="guid";
	 public static final String JXNAME="jxName";
	 public static final String PATH="filePath";//文件的全路径
	 public static final String DOWNTIME="downTime";
	 public static final String ISBEIGINCHECKED="isBeiginChecked";
	 public static final String ISCHECKED="isChecked";
	 public static final String ISUPLOADED="isuploaded";
	 public static final String LASTCHECKTIME="lastcheckTime";
	 public static final String WORKERNAME="workerName";
	 public static final String FIRSTCHECKTIME="firstcheckTime";
	 public static final String LASTCHECKSTATION="lastCheckStation";
	 public static final String LASTCHECKDEVICE_INDEX="lastCheckDeviceIndex";
	 public static final String LASTCHECKPARTITEM_INDEX="lastCheckPartItemIndex";
	 public static final String ISREVERSE_CHECK="isReverseCheck";
	 
	 public static final String PLANNAME="T_Line_Name";
	 public static final String PLANGUID="T_Line_Guid";
	 
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
		 public static final String  Name = "Name";
		 public static final String Number = "Number";
		 public static final String Password = "password";
		 public static final String IsAdministrator = "IsAdministrator";
		 public static final String PlanGuid = "PlanGuid";
		 public static final String GroupName = "GroupName";
	 }
	 public static String TABLE_TURN = "TurnInfo";
	 public class Plan_Turn_Table{
		 public static final String  Number = "Number";
		 public static final String  StartTime = "StartTime";
		 public static final String EndTime = "EndTime";
		 public static final String PlanGuid = "PlanGuid";
		 public static final String DutyNumber = "DutyNumber";
		 
		
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
				+ "guid varchar(64)," + "jxName varchar(128),"
				+ "filePath varchar(128)," + "downTime varchar(24),"
				+ "isBeiginChecked BOOLEAN," + "isChecked BOOLEAN,"
				+ "isuploaded BOOLEAN," + "lastcheckTime varchar(24),"
				+ "workerName varchar(128)," + "firstcheckTime varchar(24),"
				+ "lastCheckStation varchar(8),"
				+ "lastCheckDeviceIndex varchar(8),"
				+ "lastCheckPartItemIndex varchar(8),"
				+ "isReverseCheck BOOLEAN,"
				+ SourceTable.PLANNAME +" varchar(256),"
				+  SourceTable.PLANGUID + " varchar(256) PRIMARY KEY"
				//+ "PRIMARY KEY ("+SourceTable.PLANGUID +")"
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
				+ Plan_Worker_Table.Number+" varchar(256) PRIMARY KEY," 
				+ Plan_Worker_Table.Password +" varchar,"
				+ Plan_Worker_Table.IsAdministrator +" BOOLEAN,"
				+ Plan_Worker_Table.PlanGuid + " varchar(256),"
				+ Plan_Worker_Table.GroupName + " varchar(128)"
				//+ "PRIMARY KEY ( " +Plan_Worker_Table.Number +")"
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
				+ Plan_Turn_Table.StartTime+" varchar(16)," 
				+ Plan_Turn_Table.EndTime +" varchar(16),"
				+ Plan_Turn_Table.DutyNumber +" varchar(8),"
				+ Plan_Turn_Table.PlanGuid + " varchar(256)"
				//+ "PRIMARY KEY ("+Plan_Turn_Table.Number +")"
				+")";

		db.execSQL(turnSql);
		

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
	 }

	 @Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}

	}
