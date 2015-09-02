package com.aic.aicdetactor.database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.MyJSONParse;









import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.data.WorkerInfo;
import com.aic.aicdetactor.database.DBHelper.SourceTable;
import com.aic.aicdetactor.util.SystemUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TemporaryRouteDao {
	DBHelper helper = null;
	Context mContext = null;
	
	public static final int SEACHER_TYPE_TODAY =1;
	public static final int SEACHER_TYPE_ALL =2;
	public static final int SEACHER_TYPE_UNREAD =3;
	private SQLiteDatabase mDB = null;
    private final String TAG = "luotest";
	public TemporaryRouteDao(Context cxt) {
		helper = new DBHelper(cxt);
		mContext = cxt;
		mDB = helper.getWritableDatabase();
	}

	/**
	 * 当Activity中调用此构造方法，传入一个版本号时，系统会在下一次调用数据库时调用Helper中的onUpgrade()方法进行更新
	 * 
	 * @param cxt
	 * @param version
	 */
	public TemporaryRouteDao(Context cxt, int version) {
		helper = new DBHelper(cxt, version);
		mDB = helper.getWritableDatabase();
		mContext = cxt;
	}

	/**
	 * 插入新的临时巡检数据到数据表中,
	 * 前面的两个参数好像没有，待商榷
	 * @param fileName
	 * @param path
	 * @param bean
	 * 
	 */	
	public void insertInfo(String fileName,String path,TemporaryDataBean bean) {
		Log.d("luotest", "in insertInfo() " +path);		
		
		String sql = null;
		
		 sql = "insert into "+DBHelper.TABLE_TEMPORARY
				+" ("
				+ DBHelper.Temporary_Table.Title +","
				+ DBHelper.Temporary_Table.Content+","
				+ DBHelper.Temporary_Table.GroupName+","
				+ DBHelper.Temporary_Table.CorporationName+","
				+ DBHelper.Temporary_Table.WorkShopName+","
				+ DBHelper.Temporary_Table.DevName+","
				+ DBHelper.Temporary_Table.DevSN+","
				+ DBHelper.Temporary_Table.Task_Mode+","
				+ DBHelper.Temporary_Table.T_Worker_R_Name+","
				+ DBHelper.Temporary_Table.T_Worker_R_Mumber+","
				+ DBHelper.Temporary_Table.Create_Time+","
				+ DBHelper.Temporary_Table.Receive_Time+","
				+ DBHelper.Temporary_Table.Feedback_Time+","
				+ DBHelper.Temporary_Table.Execution_Time+","
				+ DBHelper.Temporary_Table.Finish_Time+","
				+ DBHelper.Temporary_Table.Result+","
				+ DBHelper.Temporary_Table.Remarks+","
				+ DBHelper.Temporary_Table.Unit+","
				+ DBHelper.Temporary_Table.T_Measure_Type_Id+","
				+ DBHelper.Temporary_Table.T_Measure_Type_Code+","
				+ DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Id+","
				+ DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Code+","
				+ DBHelper.Temporary_Table.UpLimit+","
				+ DBHelper.Temporary_Table.Middle_Limit+","
				+ DBHelper.Temporary_Table.DownLimit+","
				+ DBHelper.Temporary_Table.T_Temporary_Line_Guid+","
				+ DBHelper.Temporary_Table.Guid+","
				+ DBHelper.Temporary_Table.Is_Original_Line+","
				+ DBHelper.Temporary_Table.Is_Readed

				+")values(?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?)";
		
		mDB.execSQL(sql, new Object[] {
				bean.Title,
				bean.Content,
				bean.GroupName,
				bean.CorporationName,
				bean.WorkShopName,
				bean.DevName,
				bean.DevSN,
				bean.Task_Mode,
				bean.T_Worker_R_Name,
				bean.T_Worker_R_Mumber,
				bean.Create_Time,
				bean.Receive_Time,
				bean.Feedback_Time,
				bean.Execution_Time,
				bean.Finish_Time,
				bean.Result,
				bean.Remarks,
				bean.Unit,
				bean.T_Measure_Type_Id,
				bean.T_Measure_Type_Code,
				bean.T_Item_Abnormal_Grade_Id,
				bean.T_Item_Abnormal_Grade_Code,
				bean.UpLimit,
				bean.Middle_Limit,
				bean.DownLimit,
				bean.T_Temporary_Line_Guid,
				bean.Guid,
				bean.Is_Original_Line,
				bean.Is_Readed


				});		
		
	}

	

	


	/**
	 * 根据原文件的guid 来删除数据表中的数据项
	 * @param temporaryGUID
	 */
	public void delete(String temporaryGUID) {
		// SQLiteDatabase db = helper.getWritableDatabase();
		String where = DBHelper.Temporary_Table.T_Temporary_Line_Guid + " = ?";
		String[] whereValue = { temporaryGUID };
		mDB.delete(DBHelper.TABLE_TEMPORARY, where, whereValue);
	}

	/**
	 * 修改数据。
	 * 根据数据表里的T_Temporary_Line_Guid 来查询修改。
	 * bean 数据是从数据表里查询得到的，再修改关注的数据 ，最后更新到数据表中。
	 * @param temporaryGUID
	 * @param bean
	 */
	public void update(String temporaryGUID, TemporaryDataBean bean) {
		if (bean == null) {
			return;
		}
		// SQLiteDatabase db = helper.getWritableDatabase();
		String where = DBHelper.Temporary_Table.T_Temporary_Line_Guid + " = ?";
		String[] whereValue = { temporaryGUID };

		ContentValues cv = new ContentValues();	
		cv.put(DBHelper.Temporary_Table.Title	,bean.Title);
		cv.put(DBHelper.Temporary_Table.Content	,bean.Content);
		cv.put(DBHelper.Temporary_Table.GroupName	,bean.GroupName);
		cv.put(DBHelper.Temporary_Table.CorporationName	,bean.CorporationName);
		cv.put(DBHelper.Temporary_Table.WorkShopName	,bean.WorkShopName);
		cv.put(DBHelper.Temporary_Table.DevName	,bean.DevName);
		cv.put(DBHelper.Temporary_Table.DevSN	,bean.DevSN);
		cv.put(DBHelper.Temporary_Table.Task_Mode	,bean.Task_Mode);
		cv.put(DBHelper.Temporary_Table.T_Worker_R_Name	,bean.T_Worker_R_Name);
		cv.put(DBHelper.Temporary_Table.T_Worker_R_Mumber	,bean.T_Worker_R_Mumber);
		cv.put(DBHelper.Temporary_Table.Create_Time	,bean.Create_Time);
		cv.put(DBHelper.Temporary_Table.Receive_Time	,bean.Receive_Time);
		cv.put(DBHelper.Temporary_Table.Feedback_Time	,bean.Feedback_Time);
		cv.put(DBHelper.Temporary_Table.Execution_Time	,bean.Execution_Time);
		cv.put(DBHelper.Temporary_Table.Finish_Time	,bean.Finish_Time);
		cv.put(DBHelper.Temporary_Table.Result	,bean.Result);
		cv.put(DBHelper.Temporary_Table.Remarks	,bean.Remarks);
		cv.put(DBHelper.Temporary_Table.Unit	,bean.Unit);
		cv.put(DBHelper.Temporary_Table.T_Measure_Type_Id	,bean.T_Measure_Type_Id);
		cv.put(DBHelper.Temporary_Table.T_Measure_Type_Code	,bean.T_Measure_Type_Code);
		cv.put(DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Id	,bean.T_Item_Abnormal_Grade_Id);
		cv.put(DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Code	,bean.T_Item_Abnormal_Grade_Code);
		cv.put(DBHelper.Temporary_Table.UpLimit	,bean.UpLimit);
		cv.put(DBHelper.Temporary_Table.Middle_Limit	,bean.Middle_Limit);
		cv.put(DBHelper.Temporary_Table.DownLimit	,bean.DownLimit);
		cv.put(DBHelper.Temporary_Table.T_Temporary_Line_Guid	,bean.T_Temporary_Line_Guid);
		cv.put(DBHelper.Temporary_Table.Guid	,bean.Guid);
		cv.put(DBHelper.Temporary_Table.Is_Original_Line	,bean.Is_Original_Line);
		cv.put(DBHelper.Temporary_Table.Is_Readed	,bean.Is_Readed);

		mDB.update(DBHelper.TABLE_TEMPORARY, cv, where, whereValue);
	}

	
	
	/**
	 * 根据查询条件查询数据表 输出不同的数据list
	 * 
	 * 查询 只能查询和当前登录的用户信息相同的数据。
	 * 在查询前  先判断用户名和当前的登录的用户是否同一个，如果不是的话  就不需要再查数据库了。
	 * 1,今天
	 * 2,全部
	 * 3,未读
	 * @param type：
	 * 1,今天
	 * 2,全部
	 * 3,未读
	 * @return
	 */
	public List<TemporaryDataBean> queryTemporaryInfoList(int type,String Name,String WorkerNumber) {
		
		List<TemporaryDataBean> list = new ArrayList<TemporaryDataBean>();
		Cursor cursor = null;
		
		String whereStr =  null;
		String whereValueStr[] =  null;
		
		switch(type){
		case SEACHER_TYPE_TODAY:
			whereStr = DBHelper.Temporary_Table.T_Worker_R_Name +"=? and "+
					DBHelper.Temporary_Table.T_Worker_R_Mumber +" =?  and "
					+ DBHelper.Temporary_Table.Receive_Time +" =? ";
			whereValueStr = new String[] { Name, WorkerNumber,SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD)};
			break;
		case SEACHER_TYPE_ALL:
			whereStr = DBHelper.Temporary_Table.T_Worker_R_Name +"=? and "+
					DBHelper.Temporary_Table.T_Worker_R_Mumber +" =?  and "
					+ DBHelper.Temporary_Table.Is_Original_Line +" =? ";
			whereValueStr = new String[] { Name, WorkerNumber,"1"};			
			
			break;
		case SEACHER_TYPE_UNREAD:
			whereStr = DBHelper.Temporary_Table.T_Worker_R_Name +"=? and "+
					DBHelper.Temporary_Table.T_Worker_R_Mumber +" =?  and "+
					DBHelper.Temporary_Table.Is_Original_Line +" =? and " +
					DBHelper.Temporary_Table.Is_Readed +" =?";
			whereValueStr = new String[] { Name, WorkerNumber,"1","0"};
			break;
		}
		
		Log.d(TAG, "queryTemporaryInfoList() Name is "+Name +",WorkerNumber is "+WorkerNumber);
		cursor = mDB.query(DBHelper.TABLE_TEMPORARY,						
				null,
				whereStr,					
				whereValueStr,
				null,
				null,
				null);
		
		if (cursor != null) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				TemporaryDataBean bean = new TemporaryDataBean();
				
				bean.Title= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Title));
				bean.Content= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Content));
				bean.GroupName= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.GroupName));
				bean.CorporationName= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.CorporationName));
				bean.WorkShopName= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.WorkShopName));
				bean.DevName= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.DevName));
				bean.DevSN= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.DevSN));
				bean.Task_Mode= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Task_Mode));
				bean.T_Worker_R_Name= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Worker_R_Name));
				bean.T_Worker_R_Mumber= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Worker_R_Mumber));
				bean.Create_Time= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Create_Time));
				bean.Receive_Time= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Receive_Time));
				bean.Feedback_Time= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Feedback_Time));
				bean.Execution_Time= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Execution_Time));
				bean.Finish_Time= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Finish_Time));
				bean.Result= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Result));
				bean.Remarks= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Remarks));
				bean.Unit= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Unit));
				bean.T_Measure_Type_Id= cursor.getInt(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Measure_Type_Id));
				bean.T_Measure_Type_Code= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Measure_Type_Code));
				bean.T_Item_Abnormal_Grade_Id= cursor.getInt(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Id));
				bean.T_Item_Abnormal_Grade_Code= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Code));
				bean.UpLimit= cursor.getFloat(cursor.getColumnIndex(DBHelper.Temporary_Table.UpLimit));
				bean.Middle_Limit= cursor.getFloat(cursor.getColumnIndex(DBHelper.Temporary_Table.Middle_Limit));
				bean.DownLimit= cursor.getFloat(cursor.getColumnIndex(DBHelper.Temporary_Table.DownLimit));
				bean.T_Temporary_Line_Guid= cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.T_Temporary_Line_Guid));
				bean.Guid = cursor.getString(cursor.getColumnIndex(DBHelper.Temporary_Table.Guid));
				bean.Is_Original_Line= cursor.getInt(cursor.getColumnIndex(DBHelper.Temporary_Table.Is_Original_Line))>0?true:false;
				bean.Is_Readed= cursor.getInt(cursor.getColumnIndex(DBHelper.Temporary_Table.Is_Readed))>0?true:false;
								
				list.add(bean);
				cursor.moveToNext();
			}
	
		}
		
		if (cursor != null) {
			cursor.close();
			}
		
		return list;
	}

	/**
	 * 获取数量总条数
	 * @return
	 */
	public int getCount() {
		int count = 0;
		Cursor cursor = mDB
				.query(DBHelper.TABLE_TEMPORARY,
						
						null,null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getCount();
		}
		return count;
	}

}