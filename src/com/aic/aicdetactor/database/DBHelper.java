package com.aic.aicdetactor.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	 private final static String DB_NAME ="test.db";//数据库名
	 private final static int VERSION = 1;//版本号
	 
	  

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
	  String jxchecksql = "create table jxcheck(" +
	      "id integer primary key autoincrement, "
	      + "guid varchar(64),"
	      + "jxName varchar(128)," 
	      + "path varchar(128),"
	      + "downTime varchar(24),"
	      + "isChecked varchar(2),"
	      + "isuploaded varchar(2),"
	      + "lastcheckTime varchar(24),"
	      + "workerName varchar(128),"
	      + "firstcheckTime varchar(24),"
	      + "lastCheckStation varchar(8),"
	      + "lastCheckDeviceIndex varchar(8),"
	      + "lastCheckPartItemIndex varchar(8),"
	      + "isReverseCheck varchar(4))";

	  db.execSQL(jxchecksql);
	  
	 }

	 //版本更新时调用
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  String sql  = "update jxcheck ....";//自己的Update操作
	  
	 // db.execSQL("DROP TABLE IF EXISTS titles");  
	  db.execSQL(sql);
	 }

	 @Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}

	}
