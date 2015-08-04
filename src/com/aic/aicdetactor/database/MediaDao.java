package com.aic.aicdetactor.database;


import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.SystemUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MediaDao {

	private DBHelper helper = null;	
	private SQLiteDatabase mDB = null;	

	public MediaDao(Context cxt) {
		helper = new DBHelper(cxt);
		mDB = helper.getWritableDatabase();		
	}

	
	public MediaDao(Context cxt, int version) {
		helper = new DBHelper(cxt, version);
		mDB = helper.getWritableDatabase();	
	}

	public int insertInfo(int fileType,String fileName, String path,String contentStr,long duration) {
		String TableName = null;	
		switch(fileType){
		case CommonDef.FILE_TYPE_PICTRUE:
			TableName = DBHelper.TABLE_NAME_PICTRUE;
			break;
		case CommonDef.FILE_TYPE_AUDIO:
			TableName = DBHelper.TABLE_NAME_AUDIO;
			break;
		case CommonDef.FILE_TYPE_TEXTRECORD:
			TableName = DBHelper.TABLE_NAME_TEXTRECORD;
			break;
		}
		
		return insertInfo( TableName, fileName,  path, contentStr, duration);
	}
	
	public String getTableName(int fileType) {
		String TableName = null;	
		switch(fileType){
		case CommonDef.FILE_TYPE_PICTRUE:
			TableName = DBHelper.TABLE_NAME_PICTRUE;
			break;
		case CommonDef.FILE_TYPE_AUDIO:
			TableName = DBHelper.TABLE_NAME_AUDIO;
			break;
		case CommonDef.FILE_TYPE_TEXTRECORD:
			TableName = DBHelper.TABLE_NAME_TEXTRECORD;
			break;
		}
		
		return TableName;
	}
	
	private int insertInfo(String TableNameStr,String fileName, String path,String contentStr,long duration) {
		clearTableContent(TableNameStr);
		String sqlStr = "INSERT INTO " + TableNameStr;		
		if(DBHelper.TABLE_NAME_AUDIO.equals(TableNameStr)){
			 sqlStr = sqlStr + "(" 
		                +DBHelper.Media_audio_Table.Name+","+DBHelper.Media_audio_Table.filePath+","+ DBHelper.Media_audio_Table.duration+","+DBHelper.Media_audio_Table.addTime+ ")"
		                		+ " VALUES " + "(?,?,?,?)";
			 mDB.execSQL(sqlStr,new Object[]{fileName,path,duration,System.currentTimeMillis()});
		       
		}else if(DBHelper.TABLE_NAME_TEXTRECORD.equals(TableNameStr)){
			 sqlStr = sqlStr + "(" 
		                +DBHelper.Media_textRecord_Table.Name+","+DBHelper.Media_textRecord_Table.filePath+","+ DBHelper.Media_textRecord_Table.content+","+DBHelper.Media_textRecord_Table.addTime+ ")"
		                		+ " VALUES " + "(?,?,?,?)";
			 mDB.execSQL(sqlStr,new Object[]{fileName,path,contentStr,System.currentTimeMillis()});
		}else if(DBHelper.TABLE_NAME_PICTRUE.equals(TableNameStr)){
			 sqlStr = sqlStr + "(" 
		                +DBHelper.Media_pic_Table.Name+","+DBHelper.Media_pic_Table.filePath+","+DBHelper.Media_pic_Table.addTime+ ")"
		                		+ " VALUES " + "(?,?,?)";
			 mDB.execSQL(sqlStr,new Object[]{fileName,path,System.currentTimeMillis()});
		}
		 
		return 1;
	}

	
	
	// 删除操作
	public void clearTableContent(String TableNameStr) {
		Cursor cursor = queryRecord(TableNameStr);
		String path = null;
		if(cursor !=null && cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++)
			path = cursor.getString(cursor.getColumnIndex(DBHelper.MEDIA_PATH_NAME));
			SystemUtil.deleteFile(path);
			cursor.moveToNext();
		}
		String sqlStr= "DELETE FROM " + TableNameStr +";";		
		mDB.execSQL(sqlStr);
	}

	public Cursor queryRecord(int fileType) {
		String TableNameStr = getTableName(fileType);
		return mDB
				.query(TableNameStr,
						null,
						null, null, null, null,
						null);

	}
	
	public Cursor queryRecord(String TableNameStr) {
		return mDB
				.query(TableNameStr,
						null,
						null, null, null, null,
						null);

	}

	
}
