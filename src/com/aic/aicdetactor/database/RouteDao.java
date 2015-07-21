package com.aic.aicdetactor.database;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.data.RouteInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RouteDao {
	DBHelper helper = null;
	private String RouteTableName = "jxcheck";
	private SQLiteDatabase mDB = null;

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

	public int insertNewRouteInfo(String fileName, String path) {
		RouteBean route = new RouteBean();
		route.mGUID = fileName;
		route.mPath = path;
		insertData(route);
		return 1;
	}

	// 插入操作
	public void insertData(RouteBean route) {
		Log.d("luotest", "in insertData");
		if (route == null) {
			return;
		}
		Log.d("testluo", " name is " + route.mGUID + ",path is " + route.mPath);
		/**
		 * + "guid varchar(64)," + "jxName varchar(128)," +
		 * "filePath varchar(128)," + "downTime varchar(24)," +
		 * "isBeiginChecked BOOLEAN," + "isChecked BOOLEAN," +
		 * "isuploaded BOOLEAN," + "lastcheckTime varchar(24)," +
		 * "workerName varchar(128)," + "firstcheckTime varchar(24)," +
		 * "lastCheckStation varchar(8)," + "lastCheckDeviceIndex varchar(8)," +
		 * "lastCheckPartItemIndex varchar(8)," + "isReverseCheck BOOLEAN)";
		 */
		String sql = "insert into jxcheck (" + "guid," + "jxName,"
				+ "filePath," + "downTime," + "isChecked," + "isBeiginChecked,"
				+ "isuploaded," + "lastcheckTime," + "workerName,"
				+ "firstcheckTime," + "lastCheckStation,"
				+ "lastCheckDeviceIndex," + "lastCheckPartItemIndex,"
				+ "isReverseCheck" + ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// SQLiteDatabase db = helper.getWritableDatabase();
		mDB.execSQL(sql, new Object[] { route.mGUID, route.mjxName,
				route.mPath, route.mDownLoadTime, route.mIsChecked,
				route.mIsBeiginCheck, route.mIsUploaded, route.mLastCheckTime,
				route.mWorkerName, route.mFirstCheckTime,
				route.mLastCheckedStationIndex, route.mLastCheckedDeviceIndex,
				route.mLastCheckedPartItemIndex, route.mIsReverseChecking });
	}

	// 其它操作
	// 删除操作
	public void delete(int id) {
		// SQLiteDatabase db = helper.getWritableDatabase();
		String where = id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		mDB.delete(RouteTableName, where, whereValue);
	}

	/**
	 * 修改操作
	 */
	public void update(int id, RouteBean updateRoute) {
		if (updateRoute == null) {
			return;
		}
		// SQLiteDatabase db = helper.getWritableDatabase();
		String where = id + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put("guid", updateRoute.mGUID);
		cv.put("jxName", updateRoute.mjxName);
		cv.put("downTime", updateRoute.mDownLoadTime);
		cv.put("isChecked", updateRoute.mIsChecked);
		cv.put("isBeiginChecked", updateRoute.mIsBeiginCheck);
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

	// 查询操作

	public Cursor queryLastCheckIndex(String strGUID) {
		return mDB
				.query(RouteTableName,
						new String[] { "lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck,isChecked,isuploaded" },
						"guid" + "=?", new String[] { strGUID }, null, null,
						null);

	}

	/**
	 * 有两种情况 第一种，还没巡检过的 第二种，已经开始巡检了，但还没巡检完毕
	 * 
	 * @return
	 */
	public Cursor queryNotFinishRouteInfo() {
		String strarg = "0";

		Cursor cursor = mDB
				.query(RouteTableName,
						new String[] { "guid,jxName,filePath,downTime,isChecked,isBeiginChecked,isuploaded,lastcheckTime,workerName,firstcheckTime,lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck" },
						"isChecked" + "=?", new String[] { strarg }, null,
						null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	/**
	 * 查询已经巡检完毕了，但还没上传服务器的巡检路线信息
	 * 
	 * @return
	 */
	public Cursor queryNotUploadRouteInfo() {
		String strarg = "0";

		Cursor cursor = mDB
				.query(RouteTableName,
						new String[] { "guid,jxName,downTime,isChecked,isBeiginChecked,isuploaded,lastcheckTime,workerName,firstcheckTime,lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck" },
						"isChecked" + "=?", new String[] { strarg }, null,
						null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public List<RouteInfo> getNoCheckFinishRouteInfo() {
		List<RouteInfo> list = new ArrayList<RouteInfo>();
		Cursor cursor = queryNotFinishRouteInfo();
		if (cursor != null) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				RouteInfo info = new RouteInfo();

				info.mIsReverseCheck = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.ISREVERSE_CHECK)));
				info.mDeviceIndex = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.LASTCHECKDEVICE_INDEX)));
				info.mIsBeiginChecked = Integer.parseInt(cursor
						.getString(cursor
								.getColumnIndex(DBHelper.ISBEIGINCHECKED)));
				info.mIsChecked = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.ISCHECKED)));
				info.mIsReverseCheck = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.ISREVERSE_CHECK)));
				info.mPartItemIndex = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.LASTCHECKPARTITEM_INDEX)));
				info.mRoutName = cursor.getString(cursor
						.getColumnIndex(DBHelper.JXNAME));
				info.mStationIndex = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.LASTCHECKSTATION)));
				int k = cursor.getColumnIndex(DBHelper.PATH);
				Log.d("luotest", "k = " + k);
				info.mFileName = cursor.getString(k);
				list.add(info);
				cursor.moveToNext();
			}

		}
		return list;
	}

	public int getCount() {
		int count = 0;
		Cursor cursor = mDB
				.query(RouteTableName,
						new String[] { "guid,jxName,downTime,isChecked,isBeiginChecked,isuploaded,lastcheckTime,workerName,firstcheckTime,lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck" },
						null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getCount();
		}
		return count;
	}

	public void updateRouteInfoList() {

	}

}