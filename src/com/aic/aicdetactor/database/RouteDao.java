package com.aic.aicdetactor.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aic.aicdetactor.comm.OrganizationType;
import com.aic.aicdetactor.comm.RouteDaoStationParams;
import com.aic.aicdetactor.data.JugmentParms;
import com.aic.aicdetactor.data.OrganizationInfoJson;
import com.aic.aicdetactor.data.PeriodInfoJson;
import com.aic.aicdetactor.data.PeriodJson;
import com.aic.aicdetactor.data.Route;
import com.aic.aicdetactor.data.RoutePeroid;
import com.aic.aicdetactor.data.T_Route;
import com.aic.aicdetactor.data.T_Temporary_Line;
import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.data.TurnInfoJson;
import com.aic.aicdetactor.data.WorkerInfo;
import com.aic.aicdetactor.data.WorkerInfoJson;
import com.aic.aicdetactor.data.upLoadInfo;
import com.aic.aicdetactor.database.DBHelper.SourceTable;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;


public class RouteDao {
	private final String TAG ="luotest";
	private DBHelper helper = null;
	private String RouteTableName = DBHelper.TABLE_SOURCE_FILE;
	private String WorkerTableName = DBHelper.TABLE_WORKERS;
	private String TurnTableName = DBHelper.TABLE_TURN;
	private String PeriodTableName = DBHelper.TABLE_Periods;
	private String TempRouteTableName = DBHelper.TABLE_TEMPORARY;
	private SQLiteDatabase mDB = null;

	private RouteDao(Context cxt) {
		helper = new DBHelper(cxt);
		mDB = helper.getWritableDatabase();
	}
	private static RouteDao singleton = null;
	public static RouteDao getInstance(Context cxt) {
		
			if (singleton == null) {
				singleton = new RouteDao(cxt);
			}
			return singleton;
	}
	/**
	 * 当Activity中调用此构造方法，传入一个版本号时，系统会在下一次调用数据库时调用Helper中的onUpgrade()方法进行更新
	 * 
	 * @param cxt
	 * @param version
	 */
	private RouteDao(Context cxt, int version) {
		helper = new DBHelper(cxt, version);
		mDB = helper.getWritableDatabase();
	}

	/**
	 * 根据参数查询TABLE_CHECKING 来生成存储文件的GUID,文件的文件名以此GUID
	 * @param WorkerName
	 * @param WorkerNumber
	 * @param ClassGroup
	 * @param TurnName
	 * @param TurnNumber
	 * @param LineGuid
	 * @return
	 */
	public String getDataSaveFileName(String WorkerName,String WorkerNumber,String ClassGroup,String TurnName,String TurnNumber,String LineGuid){
		Cursor cursor = null;	
		String Name="";
		try{
		cursor = mDB.query(DBHelper.TABLE_CHECKING,
				null,
				DBHelper.Checking_Table.T_Line_Guid + "=? and "
				+DBHelper.Checking_Table.Worker_Name + "=? and "
				+DBHelper.Checking_Table.Worker_Number + "=? and "
				+DBHelper.Checking_Table.Class_Group + "=? and "
				+DBHelper.Checking_Table.Turn_Name + "=? and "
				+DBHelper.Checking_Table.Turn_Number + "=?  ", new String[] { LineGuid,WorkerName,WorkerNumber,ClassGroup,TurnName,TurnNumber }, null, null,
				null);
		if(cursor!=null &&cursor.getCount()>0){
			cursor.moveToFirst();
			Name=cursor.getString(cursor.getColumnIndex(DBHelper.Checking_Table.File_Guid));
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();
				}
		}
		return Name;
	}
	public Cursor execSQL(String StrSql){
	return	mDB.rawQuery(StrSql, null);
	}
	/**
	 * 
	 * @param name
	 * @param Path
	 * @param lineGuid
	 * @param itemCounts
	 * @param checkedItemCounts
	 * @param WorkerList
	 * @param TurnList
	 * @param OrganizationInfo
	 */
	@SuppressWarnings("unused")
	public void insertNormalLineInfo(String lineName,
			String Path,
			String lineGuid,
			int normalitemCounts,
			int specialitemCounts,
			int checkedItemCounts,
			List<WorkerInfoJson>WorkerList,
			List<TurnInfoJson>TurnList,
			PeriodInfoJson peroid,			
			OrganizationInfoJson OrganizationInfo,
			boolean hasSpecialLine
			){
		Cursor cursor = null;		
		//T_Route info = (T_Route) infos;
		//查数据表中是否存在已有的GUID，如果没有的话 ，直接插入
		cursor = mDB.query(RouteTableName,
					null,
					DBHelper.SourceTable.PLANGUID + "=?", new String[] { lineGuid }, null, null,
					null);
		String sql = null;
		if(cursor== null || cursor.getCount()<1){ 
		 sql = "insert into "+RouteTableName
				 +"("
				 + DBHelper.SourceTable.Path+","
				+ DBHelper.SourceTable.PLANNAME+","
				+ DBHelper.SourceTable.PLANGUID+","
				+DBHelper.SourceTable.Checked_Count+","
				+DBHelper.SourceTable.NormalItemCounts +","
				+DBHelper.SourceTable.SPecialItemCounts +","
				+DBHelper.SourceTable.HasSpecialLine +","
				+DBHelper.SourceTable.DownLoadDate +")values(?,?,?,?,?,?,?,?)";
		
		mDB.execSQL(sql, new Object[] { Path, lineName,lineGuid,checkedItemCounts,normalitemCounts,specialitemCounts,hasSpecialLine,SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDD2)});
		
		}
		//如果数据表中存在已有的GUID ，如何处理呢？  目前是不做插入
		if(cursor != null){
			cursor.close();
			}
		
		//解析工人信息到数据表中
		WorkerInfoJson workerinfo = null;
		
		//parse worker information from json txt
		////////////////////////////////////////////
		
		for(int i = 0;i<WorkerList.size();i++){
			workerinfo = (WorkerInfoJson) WorkerList.get(i);
			cursor = mDB.query(WorkerTableName,
					null,
					DBHelper.Plan_Worker_Table.Number + "=?", new String[] { workerinfo.Number }, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+WorkerTableName
						 +"(" 
						 + DBHelper.Plan_Worker_Table.Name+","				 
						 + DBHelper.Plan_Worker_Table.Alias_Name+","
						 + DBHelper.Plan_Worker_Table.Class_Group+","
						 + DBHelper.Plan_Worker_Table.Number+","
						 + DBHelper.Plan_Worker_Table.Guid+ " ," 
						 + DBHelper.Plan_Worker_Table.T_Line_Guid+ ","
						 + DBHelper.Plan_Worker_Table.T_Organization_Guid +","
						 + DBHelper.Plan_Worker_Table.Pwd
						 +")values(?,?,?,?,?,?,?,?)";
						
			
				mDB.execSQL(sql, new Object[] {
						workerinfo.Name,
						workerinfo.Alias_Name,
						workerinfo.Class_Group,								
						workerinfo.Number,
						workerinfo.Guid,		
						workerinfo.T_Line_Guid,
						workerinfo.T_Organization_Guid,
						workerinfo.Password});
				}
			if(cursor != null){cursor.close();}
		}
		
		/////////////////////////////////////////////
		
		TurnInfoJson turninfo = null;
		
		//parse turn information from json txt
		
		for(int i = 0;i<TurnList.size();i++){
			turninfo = (TurnInfoJson) TurnList.get(i);
			cursor = mDB.query(TurnTableName,
					null,
					DBHelper.Plan_Turn_Table.Number + "=?" , 
					new String[] { String.valueOf(turninfo.Number)}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+TurnTableName
						 +"(" + DBHelper.Plan_Turn_Table.Number +","				 
						 + DBHelper.Plan_Turn_Table.End_Time+","
						 + DBHelper.Plan_Turn_Table.Name+","
						 + DBHelper.Plan_Turn_Table.Start_Time+","
						 + DBHelper.Plan_Turn_Table.T_Line_Guid+","						
						 + DBHelper.Plan_Turn_Table.T_Line_Content_Guid
						 +")values(?,?,?,?,?,?)";
						
				// SQLiteDatabase db = helper.getWritableDatabase();
				mDB.execSQL(sql, new Object[] {
						turninfo.Number,
						turninfo.End_Time,
						turninfo.Name,
						turninfo.Start_Time,	
						turninfo.T_Line_Guid,		
						turninfo.T_Line_Content_Guid
						});
				}
			if(cursor != null){cursor.close();}
		}
		//-----------------------------period start-------------------------------------------
		PeriodJson peroid2	 = null;
		for(int i = 0;i<peroid.Periods.size();i++){
			peroid2 = (PeriodJson) peroid.Periods.get(i);
			
			 sql = "insert into "+PeriodTableName
					 +"(" + DBHelper.Periods_Table.Is_Omission_Check +","				 
					 + DBHelper.Periods_Table.Is_Permission_Timeout+","
					 + DBHelper.Periods_Table.Span+","
					 + DBHelper.Periods_Table.Start_Point+","
					 + DBHelper.Periods_Table.T_Turn_Number_Array+","						
					 + DBHelper.Periods_Table.Task_Mode + ","
					  + DBHelper.Periods_Table.Turn_Finish_Mode + ","
					  + DBHelper.Periods_Table.Line_Guid
					 +")values(?,?,?,?,?,?,?,?)";
					
			mDB.execSQL(sql, new Object[] {
					peroid2.Is_Omission_Check,
					peroid2.Is_Permission_Timeout,
					peroid2.Span,
					peroid2.Start_Point,	
					peroid2.T_Turn_Number_Array,		
					peroid2.Task_Mode,
					peroid2.Turn_Finish_Mode,
					lineGuid
					});
			}
		
		
		
		 sql = "insert into "+DBHelper.TABLE_Period
				 +"(" + DBHelper.Period_Table.Base_Point +","				 
				 + DBHelper.Period_Table.Frequency+","
				 + DBHelper.Period_Table.Name+","
				 + DBHelper.Period_Table.Status_Array+","				
				 + DBHelper.Period_Table.T_Line_Content_Guid+","	
				   + DBHelper.Period_Table.T_Line_Guid+","	
				 + DBHelper.Period_Table.T_Period_Unit_Code + ","
				  + DBHelper.Period_Table.T_Period_Unit_Id
				 +")values(?,?,?,?,?,?,?,?)";
				
		mDB.execSQL(sql, new Object[] {
				peroid.Base_Point,
				peroid.Frequency,
				peroid.Name,
				peroid.Status_Array,				
				peroid.T_Line_Content_Guid,	
				lineGuid,
				peroid.T_Period_Unit_Code,
				peroid.T_Period_Unit_Id
				});
		
		//-----------------------------period end-------------------------------------------
			
		//-----------------------------Organization_CorporationName Start-------------------------------------------
		{
			MLog.Logd(TAG," info.mOrganizationInfo.CorporationName = "+OrganizationInfo.CorporationName);
			////insert organization	
			cursor = mDB.query(DBHelper.TABLE_T_Organization_CorporationName,
					null,
					DBHelper.Organization_CorporationName_Table.Name + "=?" , 
					new String[] { OrganizationInfo.CorporationName}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
			 sql = "insert into "
			+DBHelper.TABLE_T_Organization_CorporationName
			+"("
			+ DBHelper.Organization_CorporationName_Table.Name 
			+")values(?)";					
			
			mDB.execSQL(sql, new Object[] {	OrganizationInfo.CorporationName});
			}
			if(cursor != null){cursor.close();}
			//-----------------------------Organization_CorporationName End-------------------------------------------
			
			
			//-----------------------------Organization_GroupName Start-------------------------------------------
			//GroupName
			cursor = mDB.query(DBHelper.TABLE_T_Organization_GroupName,
					null,
					DBHelper.Organization_GroupName_Table.Name + "=?" , 
					new String[] {OrganizationInfo.GroupName}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
			 sql = "insert into "
			+DBHelper.TABLE_T_Organization_GroupName
			+"("
			+ DBHelper.Organization_GroupName_Table.Name 
			+")values(?)";					
			
			mDB.execSQL(sql, new Object[] {	OrganizationInfo.GroupName});
			}
			if(cursor != null){cursor.close();}
			//-----------------------------Organization_GroupName End-------------------------------------------
			
			
			//-----------------------------Organization_WorkShopName Start-------------------------------------------
			//WorkShopName
			cursor = mDB.query(DBHelper.TABLE_T_Organization_WorkShopName,
					null,
					DBHelper.Organization_WorkShopName_Table.Name  + "=?" , 
					new String[] {OrganizationInfo.WorkShopName}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
			 sql = "insert into "
			+DBHelper.TABLE_T_Organization_WorkShopName
			+"("
			+ DBHelper.Organization_WorkShopName_Table.Name 
			+")values(?)";					
			
			mDB.execSQL(sql, new Object[] {	OrganizationInfo.WorkShopName});
			}
			if(cursor != null){cursor.close();}
		}
		//-----------------------------Organization_WorkShopName End-------------------------------------------
	}
	private void insertNormalRouteInfo(Route infos){
		Cursor cursor = null;		
		T_Route info = (T_Route) infos;
		//查数据表中是否存在已有的GUID，如果没有的话 ，直接插入
		cursor = mDB.query(RouteTableName,
					null,
					DBHelper.SourceTable.PLANGUID + "=?", new String[] { info.Guid }, null, null,
					null);
		String sql = null;
		if(cursor== null || cursor.getCount()<1){ 
		 sql = "insert into "+RouteTableName
				 +"("
				 + DBHelper.SourceTable.Path+","
				+ DBHelper.SourceTable.PLANNAME+","
				+ DBHelper.SourceTable.PLANGUID+")values(?,?,?)";
		
		mDB.execSQL(sql, new Object[] { info.Path, info.Name,info.Guid});
		
		}
		//如果数据表中存在已有的GUID ，如何处理呢？  目前是不做插入
		if(cursor != null){
			cursor.close();
			}
		
		//解析工人信息到数据表中
		WorkerInfo workerinfo = null;
		
		//parse worker information from json txt
		////////////////////////////////////////////
		
		for(int i = 0;i<info.mWorkerList.size();i++){
			workerinfo = (WorkerInfo) info.mWorkerList.get(i);
			cursor = mDB.query(WorkerTableName,
					null,
					DBHelper.Plan_Worker_Table.Number + "=?", new String[] { workerinfo.Number }, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+WorkerTableName
						 +"(" 
						 + DBHelper.Plan_Worker_Table.Name+","				 
						 + DBHelper.Plan_Worker_Table.Alias_Name+","
						 + DBHelper.Plan_Worker_Table.Class_Group+","
						 + DBHelper.Plan_Worker_Table.Number+","
						 + DBHelper.Plan_Worker_Table.Guid +","
						 + DBHelper.Plan_Worker_Table.T_Line_Guid+ ","
						 + DBHelper.Plan_Worker_Table.T_Organization_Guid +","
						 + DBHelper.Plan_Worker_Table.Pwd
						 +")values(?,?,?,?,?,?,?,?)";
						
			
				mDB.execSQL(sql, new Object[] {
						workerinfo.Name,
						workerinfo.Alias_Name,
						workerinfo.Class_Group,								
						workerinfo.Number,
						workerinfo.Guid,		
						workerinfo.T_Line_Guid,
						workerinfo.T_Organization_Guid,
						"00000000"});
				}
			if(cursor != null){cursor.close();}
		}
		
		/////////////////////////////////////////////
		
		TurnInfo turninfo = null;
		
		//parse turn information from json txt
		
		for(int i = 0;i<info.mTurnList.size();i++){
			turninfo = (TurnInfo) info.mTurnList.get(i);
			cursor = mDB.query(TurnTableName,
					null,
					DBHelper.Plan_Turn_Table.Number + "=?" , 
					new String[] { turninfo.Number}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
				 sql = "insert into "+TurnTableName
						 +"(" + DBHelper.Plan_Turn_Table.Number +","				 
						 + DBHelper.Plan_Turn_Table.End_Time+","
						 + DBHelper.Plan_Turn_Table.Name+","
						 + DBHelper.Plan_Turn_Table.Start_Time+","
						 + DBHelper.Plan_Turn_Table.T_Line_Guid+","						
						 + DBHelper.Plan_Turn_Table.T_Line_Content_Guid
						 +")values(?,?,?,?,?,?)";
						
				// SQLiteDatabase db = helper.getWritableDatabase();
				mDB.execSQL(sql, new Object[] {
						turninfo.Number,
						turninfo.End_Time,
						turninfo.Name,
						turninfo.Start_Time,	
						turninfo.T_Line_Guid,		
						turninfo.T_Line_Content_Guid
						});
				}
			if(cursor != null){cursor.close();}
		}
		
		
			
		{
			MLog.Logd(TAG," info.mOrganizationInfo.CorporationName = "+info.mOrganizationInfo.CorporationName);
			////insert organization	
			cursor = mDB.query(DBHelper.TABLE_T_Organization_CorporationName,
					null,
					DBHelper.Organization_CorporationName_Table.Name + "=?" , 
					new String[] { info.mOrganizationInfo.CorporationName}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
			 sql = "insert into "
			+DBHelper.TABLE_T_Organization_CorporationName
			+"("
			+ DBHelper.Organization_CorporationName_Table.Name 
			+")values(?)";					
			
			mDB.execSQL(sql, new Object[] {
					info.mOrganizationInfo.CorporationName
					});
			}
			if(cursor != null){cursor.close();}
			
			//GroupName
			cursor = mDB.query(DBHelper.TABLE_T_Organization_GroupName,
					null,
					DBHelper.Organization_GroupName_Table.Name + "=?" , 
					new String[] {info.mOrganizationInfo.GroupName}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
			 sql = "insert into "
			+DBHelper.TABLE_T_Organization_GroupName
			+"("
			+ DBHelper.Organization_GroupName_Table.Name 
			+")values(?)";					
			
			mDB.execSQL(sql, new Object[] {
					info.mOrganizationInfo.GroupName
					});
			}
			if(cursor != null){cursor.close();}
			
			//WorkShopName
			cursor = mDB.query(DBHelper.TABLE_T_Organization_WorkShopName,
					null,
					DBHelper.Organization_WorkShopName_Table.Name  + "=?" , 
					new String[] {info.mOrganizationInfo.WorkShopName}, null, null,
					null);
			if(cursor== null || cursor.getCount()<1){ 
			 sql = "insert into "
			+DBHelper.TABLE_T_Organization_WorkShopName
			+"("
			+ DBHelper.Organization_WorkShopName_Table.Name 
			+")values(?)";					
			
			mDB.execSQL(sql, new Object[] {
					info.mOrganizationInfo.WorkShopName
					});
			}
			if(cursor != null){cursor.close();}
		}
		
	}
	
	private void insertTempRouteInfo(T_Temporary_Line info) {
		Cursor cursor = mDB.query(DBHelper.TABLE_TEMPORARY,
				null, DBHelper.Temporary_Table.T_Temporary_Line_Guid + "=?",
				new String[] { info.T_Temporary_Line_Guid }, null, null, null);
		if (cursor == null || cursor.getCount() < 1) {

			String sql = "insert into " + TempRouteTableName + "("
					+ DBHelper.Temporary_Table.Title + ","
					+ DBHelper.Temporary_Table.Content + ","
					+ DBHelper.Temporary_Table.GroupName + ","
					+ DBHelper.Temporary_Table.CorporationName + ","
					+ DBHelper.Temporary_Table.WorkShopName + ","
					+ DBHelper.Temporary_Table.DevName + ","
					+ DBHelper.Temporary_Table.DevSN + ","
					+ DBHelper.Temporary_Table.Task_Mode + ","
					+ DBHelper.Temporary_Table.T_Worker_R_Name + ","
					+ DBHelper.Temporary_Table.T_Worker_R_Mumber + ","
					+ DBHelper.Temporary_Table.Create_Time + ","
					+ DBHelper.Temporary_Table.Receive_Time + ","
					+ DBHelper.Temporary_Table.Feedback_Time + ","
					+ DBHelper.Temporary_Table.Execution_Time + ","
					+ DBHelper.Temporary_Table.Finish_Time + ","
					+ DBHelper.Temporary_Table.Result + ","
					+ DBHelper.Temporary_Table.Remarks + ","
					+ DBHelper.Temporary_Table.Unit + ","
					+ DBHelper.Temporary_Table.T_Measure_Type_Id + ","
					+ DBHelper.Temporary_Table.T_Measure_Type_Code + ","
					+ DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Id + ","
					+ DBHelper.Temporary_Table.T_Item_Abnormal_Grade_Code + ","
					+ DBHelper.Temporary_Table.UpLimit + ","
					+ DBHelper.Temporary_Table.Middle_Limit + ","
					+ DBHelper.Temporary_Table.DownLimit + ","
					+ DBHelper.Temporary_Table.T_Temporary_Line_Guid + ","
					+ DBHelper.Temporary_Table.Guid + ","
					+ DBHelper.Temporary_Table.Is_Original_Line + ","
					+ DBHelper.Temporary_Table.Is_Readed + ")values(?,?,?,?,?,"
					+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
					+ "?,?,?,?" + ")";

			// SQLiteDatabase db = helper.getWritableDatabase();
			mDB.execSQL(sql, new Object[] { info.Title, info.Content,
					info.GroupName, info.CorporationName, info.WorkShopName,
					info.DevName, info.DevSN, info.Task_Mode,
					info.T_Worker_R_Name, info.T_Worker_R_Mumber,
					info.Create_Time, info.Receive_Time, info.Feedback_Time,
					info.Execution_Time, info.Finish_Time, info.Result,
					info.Remarks, info.Unit, info.T_Measure_Type_Id,
					info.T_Measure_Type_Code, info.T_Item_Abnormal_Grade_Id,
					info.T_Item_Abnormal_Grade_Code, info.UpLimit,
					info.Middle_Limit, info.DownLimit,
					info.T_Temporary_Line_Guid, info.Data_Exist_Guid,
					info.Is_Original_Line, info.Is_Readed

			});

		}
		if (cursor != null) {
			cursor.close();
		}
	}
	
	/**
	 * 把巡检结果插入数据表中
	 * @param info
	 */
	public  void insertUploadFile(RoutePeroid info,boolean bIsUpdate,boolean bUpdated,boolean bUpLoaded){
		MLog.Logd(TAG, "insertUploadFile() start");
		String sql ="";
		if(!bIsUpdate){
		sql = "insert into "
					+DBHelper.TABLE_CHECKING
					+"("
					+ DBHelper.Checking_Table.Base_Point +"," 
					+ DBHelper.Checking_Table.Class_Group +"," 
					+ DBHelper.Checking_Table.Date +"," 
					+ DBHelper.Checking_Table.File_Guid +"," 
					+ DBHelper.Checking_Table.Is_Updateed +"," 
					+ DBHelper.Checking_Table.Is_Uploaded +"," 
					+ DBHelper.Checking_Table.Span +"," 
					+ DBHelper.Checking_Table.Start_Point +"," 
					+ DBHelper.Checking_Table.T_Line_Guid +"," 
					+ DBHelper.Checking_Table.T_Line_Name +"," 					
					+ DBHelper.Checking_Table.T_Period_Unit_Code +"," 
					+ DBHelper.Checking_Table.Task_Mode +"," 
					+ DBHelper.Checking_Table.Turn_Finish_Mode +"," 					
					+ DBHelper.Checking_Table.Turn_Name +"," 
					+ DBHelper.Checking_Table.Turn_Number +"," 
					+ DBHelper.Checking_Table.Worker_Name +"," 					
					+ DBHelper.Checking_Table.Worker_Number +","
					+ DBHelper.Checking_Table.Is_Special_Inspection 
					+")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";					
					
					mDB.execSQL(sql, new Object[] {
							info.Base_Point,
							info.Class_Group,
							SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM),
							info.File_Guid,
							false,
							false,
							info.Span,
							info.Start_Point,
							info.T_Line_Guid,
							info.T_Line_Name,
							info.T_Period_Unit_Code,
							info.Task_Mode,
							info.Turn_Finish_Mode,
							info.Turn_Name,
							info.Turn_Number,
							info.Worker_Name,
							info.Worker_Number,
							info.Is_Special_Inspection
							});
		}else{
			//update table_name set field1=val1, field2=val2 where expression;
			
			int update=0;
			int upload=0;
			if(bUpdated){update=1;}
			if(bUpLoaded){upload=1;}
			
			sql="update " +DBHelper.TABLE_CHECKING +" set "+ DBHelper.Checking_Table.Is_Updateed+" = " +update +","
					+DBHelper.Checking_Table.Is_Uploaded + " = "+upload 
					+ " where "
					+ DBHelper.Checking_Table.Base_Point +"=" +info.Base_Point +" and "
					+ DBHelper.Checking_Table.Class_Group +"='" +info.Class_Group+"' and "
					+ DBHelper.Checking_Table.File_Guid +"='"+info.File_Guid +"' and "
					+ DBHelper.Checking_Table.Span +"="+info.Span +" and "
					+ DBHelper.Checking_Table.Start_Point +"=" +info.Start_Point+" and "
					+ DBHelper.Checking_Table.T_Line_Guid +"='" +info.T_Line_Guid+"' and "
					+ DBHelper.Checking_Table.T_Line_Name +"='" 	+info.T_Line_Name	+"' and "			
					+ DBHelper.Checking_Table.T_Period_Unit_Code +"='"+info.T_Period_Unit_Code +"' and "
					+ DBHelper.Checking_Table.Task_Mode +"="+info.Task_Mode +" and "
					+ DBHelper.Checking_Table.Turn_Finish_Mode +"=" +info.Turn_Finish_Mode		+" and "			
					+ DBHelper.Checking_Table.Turn_Name +"='" +info.Turn_Name+"' and "
					+ DBHelper.Checking_Table.Turn_Number +"=" +info.Turn_Number+" and "
					+ DBHelper.Checking_Table.Worker_Name +"='" +info.Worker_Name	+"' and "				
					+ DBHelper.Checking_Table.Worker_Number +"='"+info.Worker_Number+"' and "
					+ DBHelper.Checking_Table.Is_Special_Inspection +"="+info.Is_Special_Inspection;
			mDB.execSQL(sql);
		}
					MLog.Logd(TAG, "insertUploadFile() end");
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
	 * 修改工人登录密码，原的密码保持不变
	 * @param name
	 * @param pwd
	 * @param newPwd
	 */
	public boolean ModifyWorkerPwd(String name,String pwd,String newPwd,ContentValues errorcv){
		
		String error = "";
		boolean bok = true;
		//first 查找 name  ismodify =1 and newpwd 
		Cursor cursor =  mDB.query(DBHelper.TABLE_WORKERS,
					null,
					DBHelper.Plan_Worker_Table.Name  + "=? and " +DBHelper.Plan_Worker_Table.Pwd +"=?  " , 
					new String[] {name,pwd}, null, null,
					null);
			if(cursor== null || cursor.getCount()>0){ 
				String where  =  DBHelper.Plan_Worker_Table.Name+"=? and "+DBHelper.Plan_Worker_Table.Pwd +"=?";
				String[] whereValue = {name,pwd};
				
				ContentValues cv = new ContentValues();			
				cv.put(DBHelper.Plan_Worker_Table.Pwd, newPwd);
				cv.put(DBHelper.Plan_Worker_Table.IsModifyPwd, true);
				cv.put(DBHelper.Plan_Worker_Table.ModifyDate, SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
				mDB.update(DBHelper.TABLE_WORKERS, cv, where, whereValue);
				cursor.close();
				cursor= null;
				}else{
					errorcv.put("error", "请确认您的用户名及密码");
					bok = false;
				}
			if(cursor !=null)cursor.close();
		
		return bok;
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
						SourceTable.Path+ "=?", new String[] { path }, null,
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
	
	/**
	 * 根据巡检路线的lineGuid获取轮次数组
	 * @param strLineGuid
	 * @return
	 */
	public List<TurnInfoJson>getTurnInfoListByGuid(String strLineGuid){
		List<TurnInfoJson> list = new ArrayList<TurnInfoJson>();
		Cursor cur = null;
		try{
		cur = mDB.query(DBHelper.TABLE_TURN, null, DBHelper.Plan_Turn_Table.T_Line_Guid +" =? ",new String[]{strLineGuid} , null, null, null);
		if(cur!=null){
			cur.moveToFirst();
			for(int i=0;i<cur.getCount();i++){
				TurnInfoJson turninfo = new TurnInfoJson();
				turninfo.End_Time		= cur.getString(cur.getColumnIndex(DBHelper.Plan_Turn_Table.End_Time));
				turninfo.Name           = cur.getString(cur.getColumnIndex(DBHelper.Plan_Turn_Table.Name));
				turninfo.Number         = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Plan_Turn_Table.Number)));
				turninfo.Start_Time     = cur.getString(cur.getColumnIndex(DBHelper.Plan_Turn_Table.Start_Time));
				turninfo.T_Line_Guid    = cur.getString(cur.getColumnIndex(DBHelper.Plan_Turn_Table.T_Line_Guid));
				turninfo.T_Line_Content_Guid= cur.getString(cur.getColumnIndex(DBHelper.Plan_Turn_Table.T_Line_Content_Guid));
				list.add(turninfo);
				cur.moveToNext();
			}
		}
		}catch(Exception e){
			
		}finally{
			if(cur!=null){cur.close();}
		}
		return list;
	}
	
	 
	public PeriodInfoJson getPeriodJsonListByGuidEx(String strLineGuid){
		//List<PeriodJson> list = new ArrayList<PeriodJson>();
		PeriodInfoJson pjson= new PeriodInfoJson();
		Cursor cur = null;
		try{
		cur = mDB.query(DBHelper.TABLE_Periods, null, DBHelper.Periods_Table.Line_Guid +" =? ",new String[]{strLineGuid} , null, null, null);
		if(cur!=null){
			cur.moveToFirst();
			for(int i=0;i<cur.getCount();i++){
				PeriodJson periofinfo = new PeriodJson();
				periofinfo.Is_Omission_Check     = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Is_Omission_Check)));
				periofinfo.Is_Permission_Timeout = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Is_Permission_Timeout)));
				periofinfo.Span                  = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Span)));
				periofinfo.Start_Point           = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Start_Point)));
				periofinfo.T_Turn_Number_Array   = cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.T_Turn_Number_Array));				
				periofinfo.Task_Mode             = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Task_Mode)));
				periofinfo.Turn_Finish_Mode      = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Turn_Finish_Mode)));
				pjson.Periods.add(periofinfo);
				cur.moveToNext();
			}
		}
		}catch(Exception e){
			Log.e(TAG, e.toString());
		}finally{
			if(cur!=null){cur.close();}
		}
		
		try{
			cur = mDB.query(DBHelper.TABLE_Period, null, DBHelper.Period_Table.T_Line_Guid +" =? ",new String[]{strLineGuid} , null, null, null);
			if(cur!=null){
				cur.moveToFirst();
				for(int i=0;i<cur.getCount();i++){
					pjson.Base_Point     = cur.getString(cur.getColumnIndex(DBHelper.Period_Table.Base_Point));
					pjson.Frequency = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Period_Table.Frequency)));
					pjson.Name                  = cur.getString(cur.getColumnIndex(DBHelper.Period_Table.Name));
					pjson.Status_Array           = cur.getString(cur.getColumnIndex(DBHelper.Period_Table.Status_Array));
					pjson.T_Line_Content_Guid   = cur.getString(cur.getColumnIndex(DBHelper.Period_Table.T_Line_Content_Guid));				
					pjson.T_Line_Guid             = strLineGuid;
					pjson.T_Period_Unit_Code      = cur.getString(cur.getColumnIndex(DBHelper.Period_Table.T_Period_Unit_Code));
					pjson.T_Period_Unit_Id      = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Period_Table.T_Period_Unit_Id)));
					cur.moveToNext();
				}
			}
			}catch(Exception e){
				
			}finally{
				if(cur!=null){cur.close();}
			}
		
		return pjson;
	}
	/**
	 * 根据巡检数据的LINEGuid获取周期的数组
	 * @param strLineGuid
	 * @return
	 */
	public List<PeriodJson>getPeriodJsonListByGuid(String strLineGuid){
		List<PeriodJson> list = new ArrayList<PeriodJson>();
		Cursor cur = null;
		try{
		cur = mDB.query(DBHelper.TABLE_Periods, null, DBHelper.Periods_Table.Line_Guid +" =? ",new String[]{strLineGuid} , null, null, null);
		if(cur!=null){
			cur.moveToFirst();
			for(int i=0;i<cur.getCount();i++){
				PeriodJson periofinfo = new PeriodJson();
				periofinfo.Is_Omission_Check     = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Is_Omission_Check)));
				periofinfo.Is_Permission_Timeout = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Is_Permission_Timeout)));
				periofinfo.Span                  = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Span)));
				periofinfo.Start_Point           = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Start_Point)));
				periofinfo.T_Turn_Number_Array   = cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.T_Turn_Number_Array));				
				periofinfo.Task_Mode             = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Task_Mode)));
				periofinfo.Turn_Finish_Mode      = Integer.valueOf(cur.getString(cur.getColumnIndex(DBHelper.Periods_Table.Turn_Finish_Mode)));
				list.add(periofinfo);
				cur.moveToNext();
			}
		}
		}catch(Exception e){
			
		}finally{
			if(cur!=null){cur.close();}
		}
		return list;
	}
	/**
	 * 根据worker信息 获取其其他属性
	 * @param name
	 * @param pwd
	 * @return
	 */
	public List<WorkerInfoJson>getWorkerInfoListByNameAndPwd(String name,String pwd){
		List<WorkerInfoJson>list = new ArrayList<WorkerInfoJson>();
		Cursor cur = mDB.query(WorkerTableName,						
						null,
						DBHelper.Plan_Worker_Table.Name+ "=?" +" and "+DBHelper.Plan_Worker_Table.Number+ "=?", new String[] { name,pwd }, 
						null,
						null, null);
		if (cur != null) {
			cur.moveToFirst();
			for(int n=0;n<cur.getCount();n++){
				WorkerInfoJson worker = new WorkerInfoJson();
				worker.Name = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Name));
				worker.Number = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Number));
				worker.Alias_Name = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Alias_Name));
				worker.Class_Group = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Class_Group));
				worker.Guid = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Guid));
				worker.T_Line_Guid = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.T_Line_Guid));
				worker.T_Organization_Guid = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.T_Organization_Guid));
				worker.Password = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Pwd));
				cur.moveToNext();
				list.add(worker);
			}
			cur.close();
		}
		return list;
}
	
	public WorkerInfoJson getWorkerInfoByLineGuid(String strLineGuid,String name,String pwd){
		WorkerInfoJson list = new WorkerInfoJson();
		Cursor cur = mDB.query(WorkerTableName,						
						null,
						DBHelper.Plan_Worker_Table.Name+ "=?" +" and "+DBHelper.Plan_Worker_Table.Pwd+ "=?"+" and "+DBHelper.Plan_Worker_Table.T_Line_Guid+ "=?", new String[] { name,pwd,strLineGuid }, 
						null,
						null, null);
		WorkerInfoJson worker = new WorkerInfoJson();
		if (cur != null) {
			cur.moveToFirst();
			for(int n=0;n<cur.getCount();n++){
				
				worker.Name = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Name));
				worker.Number = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Number));
				worker.Alias_Name = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Alias_Name));
				worker.Class_Group = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Class_Group));
				worker.Guid = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Guid));
				worker.T_Line_Guid = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.T_Line_Guid));
				worker.T_Organization_Guid = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.T_Organization_Guid));
				worker.Password = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.Pwd));
				cur.moveToNext();
			}
			cur.close();
		}
		return worker;
}

/**
 * 查询工人信息，如果工人信息正确，调用GetUploadJsonFile 函数，
 * @param name
 * @param pwsd
 * @param cv
 * @return 工人对应的JSON文件路径
 */
public List<Map<String,String>> queryLineInfoByWorker(String name,String pwsd,ContentValues cv){
	String error = "";
	List<String> guidList = new ArrayList<String>();
	List<Map<String,String>> fileNameList = new ArrayList<Map<String,String>>();
	//查询工人信息
	Cursor cur =  mDB.query(WorkerTableName, null,
				DBHelper.Plan_Worker_Table.Name + "=?" + " and "
						+ DBHelper.Plan_Worker_Table.Pwd + "=? ",
				new String[] { name, pwsd}, null, null, null);
		if (cur != null && cur.getCount() > 0) {
			MLog.Logd("luotest",
					"queryLogIn()  cur != null cur.count ="
							+ cur.getCount());
			cur.moveToFirst();
			for (int n = 0; n < cur.getCount(); n++) {
				String GUID = cur
						.getString(cur
								.getColumnIndex(DBHelper.Plan_Worker_Table.T_Line_Guid));
				guidList.add(GUID);
				cur.moveToNext();
				MLog.Logd("luotest",
						"queryLogIn()  searvher worker table GUID is"
								+ GUID);
			}
			cur.close();
			cur = null;
		} else {
			
			MLog.Logd("luotest",
					"queryLogIn() searvher worker table cur is null");
			error = "没有您的信息，请核实再登录";
		}

	if (cur != null) {
		cur.close();
	}

	

	for (int k = 0; k < guidList.size(); k++) {
		MLog.Logd("luotest", " queryLogIn() search worker table " + k);
		Cursor cursor2 = mDB.query(RouteTableName,
				null,
				DBHelper.SourceTable.PLANGUID + "=?",
				new String[] { guidList.get(k) }, null, null, null);
		if (cursor2 != null && cursor2.getCount() > 0) {
			cursor2.moveToFirst();
			for (int i = 0; i < cursor2.getCount(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				String path = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.Path));
				String totalCount = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.NormalItemCounts));
				String lineName = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.PLANNAME));
				String checkedCount = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.Checked_Count));
				map.put(RouteDaoStationParams.LineName, cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.PLANNAME)));
				map.put(RouteDaoStationParams.LinePath, cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.Path)));
				map.put(RouteDaoStationParams.LineNormalTotalCount, cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.NormalItemCounts)));
				map.put(RouteDaoStationParams.LineSpecialTotalCount, cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.SPecialItemCounts)));
				map.put(RouteDaoStationParams.LineCheckedCount, checkedCount);
				map.put(RouteDaoStationParams.HasSpecialLine, cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.HasSpecialLine)));
				fileNameList.add(map);
				cursor2.moveToNext();
				MLog.Logd("luotest",
						" queryLogIn() search route table  result path is "
								+ path);
			}
			cursor2.close();
			cursor2 = null;
		} else {
			MLog.Logd("luotest",
					" queryLogIn() search route table cursor2 is null");
			error = "没有您对应的巡检任务";
		}

		if (cursor2 != null) {
			cursor2.close();
		}
	}
	MLog.Logd("luotest", "queryLogIn() end  error=" + error);
	cv.put("error", error);
	return fileNameList;
}

public List<JugmentParms> queryLineInfoByWorkerEx(String name,String pwsd,ContentValues cv){
	String error = "";
	List<String> guidList = new ArrayList<String>();
	List<JugmentParms> mJugmentListParms=new   ArrayList<JugmentParms>();
	
	//查询工人信息
	Cursor cur =  mDB.query(WorkerTableName, null,
				DBHelper.Plan_Worker_Table.Name + "=?" + " and "+ DBHelper.Plan_Worker_Table.Pwd + "=? ",
				new String[] { name, pwsd}, null, null, null);
	
		if (cur != null && cur.getCount() > 0) {
			MLog.Logd("luotest","queryLogIn()  cur != null cur.count ="	+ cur.getCount());
			WorkerInfoJson WorkerInfoJson = new WorkerInfoJson();
			cur.moveToFirst();
			for (int n = 0; n < cur.getCount(); n++) {
				String GUID = cur.getString(cur.getColumnIndex(DBHelper.Plan_Worker_Table.T_Line_Guid));
				guidList.add(GUID);
				cur.moveToNext();
				MLog.Logd("luotest","queryLogIn()  searvher worker table GUID is"+ GUID);
			}
			cur.close();
			cur = null;
		} else {
			
			MLog.Logd("luotest","queryLogIn() searvher worker table cur is null");
			error = "没有您的信息，请核实再登录";
		}

	if (cur != null) {
		cur.close();
	}

	

	for (int k = 0; k < guidList.size(); k++) {
		MLog.Logd("luotest", " queryLogIn() search worker table " + k);
		Cursor cursor2 = mDB.query(RouteTableName,
				null,
				DBHelper.SourceTable.PLANGUID + "=?",
				new String[] { guidList.get(k) }, null, null, null);
		
		if (cursor2 != null && cursor2.getCount() > 0) {
			cursor2.moveToFirst();
			for (int i = 0; i < cursor2.getCount(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				JugmentParms mJugmentParms = new JugmentParms();
				String path = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.Path));
				mJugmentParms.T_Line.Name = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.PLANNAME));
				mJugmentParms.T_Line.T_Line_Guid = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.PLANGUID));
				mJugmentParms.T_Line.LinePath = cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.Path));
				mJugmentParms.T_Line.LineNormalTotalCount= Integer.valueOf(cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.NormalItemCounts)));
				mJugmentParms.T_Line.LineSpecialTotalCount= Integer.valueOf(cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.SPecialItemCounts)));
				mJugmentParms.T_Line.LineCheckedCount= Integer.valueOf(cursor2.getString(cursor2.getColumnIndex(DBHelper.SourceTable.Checked_Count)));
				mJugmentParms.T_Line.HasSpecialLine= Integer.valueOf(cursor2.getInt(cursor2.getColumnIndex(DBHelper.SourceTable.HasSpecialLine)))>0?true:false;

				
				mJugmentParms.m_WorkerInfoJson=getWorkerInfoByLineGuid(mJugmentParms.T_Line.T_Line_Guid,name,pwsd);
				mJugmentParms.m_PeriodInfo =getPeriodJsonListByGuidEx(mJugmentParms.T_Line.T_Line_Guid);
				mJugmentParms.T_Turn=getTurnInfoListByGuid(mJugmentParms.T_Line.T_Line_Guid);
				
				mJugmentListParms.add(mJugmentParms);
				cursor2.moveToNext();
				MLog.Logd("luotest"," queryLogIn() search route table  result path is "	+ path);
			}
			cursor2.close();
			cursor2 = null;
		} else {
			MLog.Logd("luotest",
					" queryLogIn() search route table cursor2 is null");
			error = "没有您对应的巡检任务";
		}

		if (cursor2 != null) {
			cursor2.close();
		}
	}
	MLog.Logd("luotest", "queryLogIn() end  error=" + error);
	cv.put("error", error);
	return mJugmentListParms;
}

//查询临时路线
	private void queryTempRouteInfo(List<Map<String,String>> fileNameList){
		Cursor cur = mDB.query(TempRouteTableName, 
				new String[]{DBHelper.Temporary_Table.Title,DBHelper.Temporary_Table.Content},
				null,null, null, null, null);
		if (cur != null && cur.getCount() > 0) {
			MLog.Logd("luotest",
					"queryTempRouteInfo()  cur != null cur.count =" + cur.getCount());
			cur.moveToFirst();
			for (int n = 0; n < cur.getCount(); n++) {
				Map<String, String> map = new HashMap<String, String>();
				String title = cur
						.getString(cur
								.getColumnIndex(DBHelper.Temporary_Table.Title));
				String Content = cur
						.getString(cur
								.getColumnIndex(DBHelper.Temporary_Table.Content));
				map.put(RouteDaoStationParams.TMPLineName, title);
				map.put(RouteDaoStationParams.TMPLineContent, Content);
				fileNameList.add(map);
				cur.moveToNext();
				MLog.Logd("luotest", "queryTempRouteInfo()  title is"
						+ title);
			}
			cur.close();
			cur = null;
		}
		if(cur != null){cur.close();}
	}
//	/**
//	 * 根据传入的文件路径 解析路线信息
//	 * @param filelist
//	 * @return
//	 */
//	public List<T_Route> getRouteInfoByFilePath(List<String >filelist) {
//		if(filelist ==null) return null;
//		
//		List<T_Route> list = new ArrayList<T_Route>();
//		for (int n = 0; n < filelist.size(); n++) {
//			Cursor cursor = queryRouteInfoByPath(filelist.get(n));
//			if (cursor != null) {
//				cursor.moveToFirst();
//				for (int i = 0; i < cursor.getCount(); i++) {
//					T_Route info = new T_Route();
//
//					info.Guid =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.PLANGUID));
//					info.Name =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.PLANNAME));
//					info.Path =cursor.getString(cursor.getColumnIndex(DBHelper.SourceTable.Path));
//					list.add(info);
//					cursor.moveToNext();
//				}
//
//			}
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return list;
//	}
//
//	
//	public List<T_Route> getNoCheckFinishRouteInfo() {
//		List<T_Route> list = new ArrayList<T_Route>();
//		Cursor cursor = queryNotFinishRouteInfo();
//		if (cursor != null) {
//			cursor.moveToFirst();
//			for (int i = 0; i < cursor.getCount(); i++) {
//				T_Route info = new T_Route();
//
//				info.Name = cursor.getString(cursor
//						.getColumnIndex(DBHelper.SourceTable.Path));
//				info.Guid = cursor.getString(cursor
//						.getColumnIndex(DBHelper.SourceTable.PLANGUID));
//				info.Name = cursor
//						.getString(cursor
//								.getColumnIndex(DBHelper.SourceTable.PLANNAME));
//				list.add(info);
//				cursor.moveToNext();
//			}
//
//		}
//		return list;
//	}

	public int getCount() {
		int count = 0;
		Cursor cursor = mDB
				.query(RouteTableName,						
						null,
						null, 
						null,
						null, 
						null, 
						null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getCount();
		}
		return count;
	}

	public void updateRouteInfoList() {

	}
	/**
	 * （1）	JSON周期数组来自表T_Line_Original_Json
（2）	当前APP时间—调用函数那一刻APP时间
（3）	T_Line_Guid来自T_Line_Original_Json
（4）	m_CurPeriod 机构数组，返回参数、
（5）	m_FileName –巡检文件名，返回参数

	 * @return
	 */
	boolean GetUploadJsonFile(Object object, String date, String filePath,RoutePeroid m_CurPeriod,ContentValues cv){
		
		return true;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public List<String>getOrganizationList(OrganizationType type){
		List<String> list = new ArrayList<String>();
		Cursor cursor =null;
		String tableName="";
		if(type == OrganizationType.OrganizationCorporation){
			tableName =DBHelper.TABLE_T_Organization_CorporationName;
		}else if(type == OrganizationType.OrganizationGroup){
			tableName =DBHelper.TABLE_T_Organization_GroupName;
		}else if(type == OrganizationType.OrganizationWorkShop){
			tableName =DBHelper.TABLE_T_Organization_WorkShopName;
		}
		if("".equals(tableName)){return null;}
		try{
		cursor =  mDB
				.query(tableName,						
						null,
						null, 
						null,
						null, 
						null, 
						null);
		if (cursor != null) {
			cursor.moveToFirst();
			for(int i =0;i<cursor.getCount();i++){
				list.add(cursor.getString(cursor.getColumnIndex("Name")));
				cursor.moveToNext();
			}
		}
		}catch(Exception e){}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return list;
	}
}
