package com.aic.aicdetactor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	 private final static String DB_NAME ="aicdatabase.db";//数据库名
	 private final static int VERSION = 4;//版本号
	 
	  
	// public static String ID="_id";
	 public static String GUID="guid";
	 public static String JXNAME="jxName";
	 public static String PATH="filePath";//文件的全路径
	 public static String DOWNTIME="downTime";
	 public static String ISBEIGINCHECKED="isBeiginChecked";
	 public static String ISCHECKED="isChecked";
	 public static String ISUPLOADED="isuploaded";
	 public static String LASTCHECKTIME="lastcheckTime";
	 public static String WORKERNAME="workerName";
	 public static String FIRSTCHECKTIME="firstcheckTime";
	 public static String LASTCHECKSTATION="lastCheckStation";
	 public static String LASTCHECKDEVICE_INDEX="lastCheckDeviceIndex";
	 public static String LASTCHECKPARTITEM_INDEX="lastCheckPartItemIndex";
	 public static String ISREVERSE_CHECK="isReverseCheck";
	 

	 public static String MEDIA_PATH_NAME = "filePath";
	 public static String TABLE_NAME_AUDIO = "audio";
	 public class Media_audio{
		 public static final String Name = "Name";
		 public static final String filePath = "filePath";
		 public static final String duration = "duration";		 
		 public static final String addTime = "addTime";
	 }
	 public static String TABLE_NAME_TEXTRECORD = "text";
	 public class Media_textRecord{		
		 public static final String Name = "Name";
		 public static final String filePath = "filePath";
		 public static final String content = "content";		 
		 public static final String addTime = "addTime";
	 }
	 public static String TABLE_NAME_PICTRUE = "pictrue";
	 public class Media_pic{
		 public static final String Name = "Name";
		 public static final String filePath = "filePath";			 
		 public static final String addTime = "addTime";
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
		// "_id integer INTEGER PRIMARY KEY , "
		String jxchecksql = "create table IF NOT EXISTS jxcheck("
				+ "guid varchar(64)," + "jxName varchar(128),"
				+ "filePath varchar(128)," + "downTime varchar(24),"
				+ "isBeiginChecked BOOLEAN," + "isChecked BOOLEAN,"
				+ "isuploaded BOOLEAN," + "lastcheckTime varchar(24),"
				+ "workerName varchar(128)," + "firstcheckTime varchar(24),"
				+ "lastCheckStation varchar(8),"
				+ "lastCheckDeviceIndex varchar(8),"
				+ "lastCheckPartItemIndex varchar(8),"
				+ "isReverseCheck BOOLEAN)";

		db.execSQL(jxchecksql);

		String pictruesql = "create table IF NOT EXISTS " + TABLE_NAME_PICTRUE
				+ "(" + "Name varchar(256)," + "filePath varchar(256),"
				+ "addTime varchar(24)" + ")";

		db.execSQL(pictruesql);

		String audiosql = "create table IF NOT EXISTS " + TABLE_NAME_AUDIO
				+ "("+ "Name varchar(256)," + "filePath varchar(256),"
				+ "duration varchar(32)," + "addTime varchar(24)" + ")";

		db.execSQL(audiosql);

		String textRecordsql = "create table IF NOT EXISTS "
				+ TABLE_NAME_TEXTRECORD + "(" + "Name varchar(256),"
				+ "filePath varchar(256)," + "content varchar,"
				+ "addTime varchar(24)"+")";

		db.execSQL(textRecordsql);

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
	 }

	 @Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}

	}
