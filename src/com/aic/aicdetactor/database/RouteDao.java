package com.aic.aicdetactor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RouteDao {
	DBHelper helper = null;
	private String RouteTableName = "jxcheck";
	private SQLiteDatabase mDB= null;

	public RouteDao(Context cxt) {
		helper = new DBHelper(cxt);
		mDB = helper.getWritableDatabase();
	}

	/**
	 * 当Activity中调用此构造方法，传入一个版本号时，系统会在下一次调用数据库时调用Helper中的onUpgrade()方法进行更新
	 * 
	 * @param cxt
	 * @param version
	 */
	public RouteDao(Context cxt, int version) {
		helper = new DBHelper(cxt, version);
		mDB = helper.getWritableDatabase();
	}
	

	// 插入操作
	public void insertData(RouteBean route) {
		if (route == null)
			return;
		/*
		 * + "guid varchar(64)," + "jxName varchar(128)," + "path varchar(128),"
		 * + "downTime varchar(24)," + "isChecked varchar(2)," +
		 * "isuploaded varchar(2)," + "lastcheckTime varchar(24)," +
		 * "workerName varchar(128)," + "firstcheckTime varchar(24)," +
		 * "lastCheckStation varchar(8)," + "lastCheckDeviceIndex varchar(8)," +
		 * "lastCheckPartItemIndex varchar(8)," + "isReverseCheck varchar(4))";
		 */
		String sql = "insert into jxcheck (" + "guid," + "jxName," + "path,"
				+ "downTime," + "isChecked," + "isuploaded," + "lastcheckTime,"
				+ "workerName," + "firstcheckTime," + "lastCheckStation"
				+ "lastCheckDeviceIndex," + "lastCheckPartItemIndex,"
				+ "isReverseCheck" + ")values(?,?,?,?,?,?,?,?,?,?,?)";
		//SQLiteDatabase db = helper.getWritableDatabase();
		mDB.execSQL(sql, new Object[] { route.mGUID, route.mjxName, route.mPath,
				route.mDownLoadTime, route.mIsChecked, route.mIsUploaded,
				route.mLastCheckTime, route.mWorkerName, route.mFirstCheckTime,
				route.mLastCheckedStationIndex, route.mLastCheckedDeviceIndex,
				route.mLastCheckedPartItemIndex, route.mIsReverseChecking });
	}

	// 其它操作
	// 删除操作
	public void delete(int id) {
		//SQLiteDatabase db = helper.getWritableDatabase();
		String where = id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		mDB.delete(RouteTableName, where, whereValue);
	}

	// 修改操作
	public void update(int id, RouteBean updateRoute) {
		if (updateRoute == null) {
			return;
		}
		//SQLiteDatabase db = helper.getWritableDatabase();
		String where = id + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put("guid", updateRoute.mGUID);
		cv.put("jxName", updateRoute.mjxName);
		cv.put("downTime", updateRoute.mDownLoadTime);
		cv.put("isChecked", updateRoute.mIsChecked);
		cv.put("isuploaded", updateRoute.mIsUploaded);
		cv.put("lastcheckTime", updateRoute.mLastCheckTime);
		cv.put("workerName", updateRoute.mWorkerName);
		cv.put("firstcheckTime", updateRoute.mFirstCheckTime);
		cv.put("lastCheckStation", updateRoute.mLastCheckedStationIndex);
		cv.put("lastCheckDeviceIndex", updateRoute.mLastCheckedDeviceIndex);
		cv.put("lastCheckPartItemIndex", updateRoute.mLastCheckedPartItemIndex);
		cv.put("isReverseCheck", updateRoute.mIsReverseChecking);

		mDB.update(RouteTableName, cv, where, whereValue);
	}
	//查询操作
	 

	public Cursor queryLastCheckIndex(String strGUID){
	return	mDB.query(RouteTableName, new String[] {
			"lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck,isChecked,isuploaded"}, "guid"+"=?", new String[]{strGUID}, null, null, null);
		  
	}
}