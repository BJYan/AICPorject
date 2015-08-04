package com.aic.aicdetactor.database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.data.MyJSONParse;
import com.aic.aicdetactor.data.Route;








import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.data.WorkerInfo;
import com.aic.aicdetactor.database.DBHelper.SourceTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RouteDao {
	DBHelper helper = null;
	private String RouteTableName = DBHelper.TABLE_SOURCE_FILE;
	private String WorkerTableName = DBHelper.TABLE_WORKERS;
	private String TurnTableName = DBHelper.TABLE_TURN;
	
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
		RouteBean routeBean = new RouteBean();
		routeBean.mGUID = fileName;
		routeBean.mPath = path;
		
		insertRouteBaseInfo(fileName,path);
//		insertData(route);
		return 1;
	}
	public void insertRouteBaseInfo(String fileName,String path) {
		Log.d("luotest", "in insertRouteBaseInfo() " +path);
		
		Route info = 	MyJSONParse.getPlanInfo(path);
		info.parseBaseInfo();
		
		RouteBean route = new RouteBean();
		route.mGUID = fileName;
		route.mPath = path;
		
		if (route == null) {
			return;
		}
		Cursor cursor = null;
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
		cursor = mDB.query(RouteTableName,
					null,
					DBHelper.SourceTable.PLANGUID + "=?", new String[] { info.minfo.PlanGuid }, null, null,
					null);
		String sql = null;
		if(cursor== null || cursor.getCount()<1){ 
		 sql = "insert into "+RouteTableName
				+" (" + "guid," + "jxName,"
				+ "filePath," + "downTime," + "isChecked," + "isBeiginChecked,"
				+ "isuploaded," + "lastcheckTime," + "workerName,"
				+ "firstcheckTime," + "lastCheckStation,"
				+ "lastCheckDeviceIndex," + "lastCheckPartItemIndex,"
				+ "isReverseCheck," 
				+ DBHelper.SourceTable.PLANNAME+","
				+ DBHelper.SourceTable.PLANGUID+")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		mDB.execSQL(sql, new Object[] { route.mGUID, route.mjxName,
				route.mPath, route.mDownLoadTime, route.mIsChecked,
				route.mIsBeiginCheck, route.mIsUploaded, route.mLastCheckTime,
				route.mWorkerName, route.mFirstCheckTime,
				route.mLastCheckedStationIndex, route.mLastCheckedDeviceIndex,
				route.mLastCheckedPartItemIndex, route.mIsReverseChecking,
				info.minfo.PlanName,info.minfo.PlanGuid});
		
		}
		if(cursor != null){cursor.close();}
		
		WorkerInfo workerinfo = null;
		
		//parse worker information from json txt
		////////////////////////////////////////////
		
		for(int i = 0;i<info.WorkerInfoList.size();i++){
			workerinfo = (WorkerInfo) info.WorkerInfoList.get(i);
			cursor = mDB.query(WorkerTableName,
					null,
					DBHelper.Plan_Worker_Table.Number + "=?", new String[] { workerinfo.WorkerNumber }, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+WorkerTableName
						 +"(" + DBHelper.Plan_Worker_Table.Name+","				 
						 + DBHelper.Plan_Worker_Table.Number+","
						 + DBHelper.Plan_Worker_Table.Password+","
						 + DBHelper.Plan_Worker_Table.IsAdministrator+","
						 + DBHelper.Plan_Worker_Table.PlanGuid+","
						 + DBHelper.Plan_Worker_Table.GroupName
						 +")values(?,?,?,?,?,?)";
						
			
				mDB.execSQL(sql, new Object[] {
						workerinfo.WorkerName,
						workerinfo.WorkerNumber,
						workerinfo.WorkerPwd,
						workerinfo.isAdministrator,				
						info.minfo.PlanGuid,
						workerinfo.GroupName});
				}
			if(cursor != null){cursor.close();}
		}
		
		/////////////////////////////////////////////
		
		TurnInfo turninfo = null;
		
		//parse turn information from json txt
		
		for(int i = 0;i<info.TurnInfoList.size();i++){
			turninfo = (TurnInfo) info.TurnInfoList.get(i);
			cursor = mDB.query(TurnTableName,
					null,
					DBHelper.Plan_Turn_Table.Number + "=?" + " and "+DBHelper.Plan_Turn_Table.PlanGuid + "=?", 
					new String[] { turninfo.Number, info.minfo.PlanGuid}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+TurnTableName
						 +"(" + DBHelper.Plan_Turn_Table.Number +","				 
						 + DBHelper.Plan_Turn_Table.StartTime+","
						 + DBHelper.Plan_Turn_Table.EndTime+","
						 + DBHelper.Plan_Turn_Table.DutyNumber+","
						 + DBHelper.Plan_Turn_Table.PlanGuid
						 +")values(?,?,?,?,?)";
						
				// SQLiteDatabase db = helper.getWritableDatabase();
				mDB.execSQL(sql, new Object[] {
						turninfo.Number,
						turninfo.StartTime,
						turninfo.EndTime,
						turninfo.DutyNumber,				
						info.minfo.PlanGuid
						});
				}
			if(cursor != null){cursor.close();}
		}
		
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
	
	public Cursor queryRouteInfoByPath(String path) {
		String strarg = "0";

		Cursor cursor = mDB
				.query(RouteTableName,
						null,
						SourceTable.PATH+ "=?", new String[] { path }, null,
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

public List<String> queryWorkerNumber(String name,String pwsd) {
		
		List<String>guidList = new ArrayList<String>();
		Cursor cur = mDB
				.query(WorkerTableName,						
						null,
						DBHelper.Plan_Worker_Table.Name+ "=?" +" and "+DBHelper.Plan_Worker_Table.Password+ "=?", new String[] { name,pwsd }, 
						null,
						null, null);
		if (cur != null) {
			cur.moveToFirst();
			for(int n=0;n<cur.getCount();n++){
			String number = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Number));
			guidList.add(number);
			cur.moveToNext();
			Log.d("luotest", "search worker table result  number is" +number);
			}
			cur.close();
		}
		return guidList;
}
	public List<String> queryLogIn(String name,String pwsd) {
		
		List<String>guidList = new ArrayList<String>();
		Cursor cur = mDB
				.query(WorkerTableName,						
						null,
						DBHelper.Plan_Worker_Table.Name+ "=?" +" and "+DBHelper.Plan_Worker_Table.Password+ "=?", new String[] { name,pwsd }, 
						null,
						null, null);
		if (cur != null) {
			cur.moveToFirst();
			for(int n=0;n<cur.getCount();n++){
			String GUID = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.PlanGuid));
			guidList.add(GUID);
			cur.moveToNext();
			Log.d("luotest", "search worker table result  GUID is" +GUID);
			}
			cur.close();
		}

		List<String>fileNameList = new ArrayList<String>();
		
		for(int k =0 ;k<guidList.size();k++){
			Log.d("luotest", " 2 sear worker table " +k);
			Cursor cursor2 = mDB
					.query(RouteTableName,						
							new String[]{DBHelper.SourceTable.PATH},
							DBHelper.SourceTable.PLANGUID+ "=?" , new String[] { guidList.get(k) }, 
							null,
							null,
							null);
				if (cursor2 != null) {
					cursor2.moveToFirst();
					for(int i=0;i<cursor2.getCount();i++){
						String path =	cursor2.getString(0);				
					fileNameList.add(path);
					cursor2.moveToNext();
					Log.d("luotest", " search route table  result path is " +path);
					}
					cursor2.close();
				}
		}

		
		return fileNameList;
	}
	
	public List<Route> getRouteInfoByFilePath(List<String >filelist) {
		if(filelist ==null) return null;
		
		List<Route> list = new ArrayList<Route>();
		for (int n = 0; n < filelist.size(); n++) {
			Cursor cursor = queryRouteInfoByPath(filelist.get(n));
			if (cursor != null) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					Route info = new Route();

					info.mIsReverseCheck = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(DBHelper.SourceTable.ISREVERSE_CHECK)));
					info.mDeviceIndex = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(DBHelper.SourceTable.LASTCHECKDEVICE_INDEX)));
					info.mIsBeiginChecked = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(DBHelper.SourceTable.ISBEIGINCHECKED)));
					info.mIsChecked = Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(DBHelper.SourceTable.ISCHECKED)));
					info.mIsReverseCheck = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(DBHelper.SourceTable.ISREVERSE_CHECK)));
					info.mPartItemIndex = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(DBHelper.SourceTable.LASTCHECKPARTITEM_INDEX)));
					info.mRoutName = cursor.getString(cursor
							.getColumnIndex(DBHelper.SourceTable.JXNAME));
					info.mStationIndex = Integer
							.parseInt(cursor.getString(cursor
									.getColumnIndex(DBHelper.SourceTable.LASTCHECKSTATION)));
					int k = cursor.getColumnIndex(DBHelper.SourceTable.PATH);
					Log.d("luotest", "k = " + k);
					info.mFileName = cursor.getString(k);
					list.add(info);
					cursor.moveToNext();
				}

			}
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	
	public List<Route> getNoCheckFinishRouteInfo() {
		List<Route> list = new ArrayList<Route>();
		Cursor cursor = queryNotFinishRouteInfo();
		if (cursor != null) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				Route info = new Route();

				info.mIsReverseCheck = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.ISREVERSE_CHECK)));
				info.mDeviceIndex = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.LASTCHECKDEVICE_INDEX)));
				info.mIsBeiginChecked = Integer.parseInt(cursor
						.getString(cursor
								.getColumnIndex(DBHelper.SourceTable.ISBEIGINCHECKED)));
				info.mIsChecked = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.ISCHECKED)));
				info.mIsReverseCheck = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.ISREVERSE_CHECK)));
				info.mPartItemIndex = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.LASTCHECKPARTITEM_INDEX)));
				info.mRoutName = cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.JXNAME));
				info.mStationIndex = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(DBHelper.SourceTable.LASTCHECKSTATION)));
				int k = cursor.getColumnIndex(DBHelper.SourceTable.PATH);
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
						//new String[] { "guid,jxName,downTime,isChecked,isBeiginChecked,isuploaded,lastcheckTime,workerName,firstcheckTime,lastCheckStation,lastCheckDeviceIndex,lastCheckPartItemIndex,isReverseCheck" },
						null,null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getCount();
		}
		return count;
	}

	public void updateRouteInfoList() {

	}

}