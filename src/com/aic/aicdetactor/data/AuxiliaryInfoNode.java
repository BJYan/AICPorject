package com.aic.aicdetactor.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 上传服务器数据的第六个节点辅助信息
 * @author Administrator
 *
 */
public class AuxiliaryInfoNode {
/**
 * Index	32整数	为8
Date :	字符串	碰到ID扣后，巡检仪的当前时间
GUID 	字符串	判断巡检后的内容是否上传过
TurnNumber 	字符串	当前巡检的轮次号
WorkerNumber	字符串	当前巡检工人工号
StartTime	字符串	当前轮次的起始时间
EndTime	字符串	当前轮次的结束时间

 */
	public int Index =0;
	public String DateStr = null;
	public String GUIDStr= null;
	public String TurnNumber =null;
	public String WorkerNumber = null;	
	public String StartTime =null;
	public String EndTime = null;
	
	private JSONObject mObject = null;
	
	public static String KEY_Index ="Index";
	public static String KEY_Date = "Date";
	public static String KEY_GUID= "GUID";
	public static String KEY_TurnNumber ="TurnNumber";
	public static String KEY_WorkerNumber = "WorkerNumber";	
	public static String KEY_StartTime ="StartTime";
	public static String KEY_EndTime = "EndTime";
	
	public AuxiliaryInfoNode(){
		mObject = new JSONObject();
	}
	
	public void set(String key,Object Value) throws JSONException{
		mObject.putOpt(key, Value);
	}
	
	public Object getObject(){
		return mObject;
	}
}
