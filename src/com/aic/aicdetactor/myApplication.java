package com.aic.aicdetactor;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.util.MyJSONParse;

import android.app.Application;

public class myApplication extends Application
{
    private static final String VALUE = "aicdetector";
    
    //checkItemData 以*隔开的数据编号
    //轮次
    public  final int PARTITEM_TURN_NAME =0;
    //部件名
    public  final int PARTITEM_UNIT_NAME =1;
    //巡检项目名
    public  final int PARTITEM_CHECKPOINT_NAME =2;
    //巡检数据种类
    public  final int PARTITEM_DATA_TYPE_NAME =3;
  //测量单位
    public  final int PARTITEM_MEASUREMENT_UNIT_NAME =4;
    //状态标识
    public  final int PARTITEM_STATE_MARK_NAME =5;
    //上限数值
    public  final int PARTITEM_MAX_VALUE_NAME =6;
    //中限数值
    public  final int PARTITEM_MIDDLE_VALUE_NAME =7;
    //下限数值
    public  final int PARTITEM_MIN_VALUE_NAME =8;
    //额外信息
    public  final int PARTITEM_ADDITIONAL_INFO_NAME =9;
    
    private String value;
    public String mPath = "/sdcard/down.txt";
    MyJSONParse json = new MyJSONParse();
    @Override
    public void onCreate()
    {
        super.onCreate();
      
        json.initData(mPath);
        
    }
    public List<Object> getStationList() throws JSONException {
     return	json.getStationList();
    }
    public int getRoutePartItemCount(int routeIndex) throws JSONException{
    	 return	json.getRoutePartItemCount(routeIndex);
    }
    public String getStationItemName(Object object) throws JSONException {
    	return json.getStationItemName(object);
    }
    public List<Object> getDeviceList(Object object) throws JSONException {
    	return json.getDeviceList(object);
    }
    public List<Object> getDeviceItemList(int stationIndex) throws JSONException {
    	return json.getDeviceItem(stationIndex);
    }
    public int getDevicePartItemCount(Object deviceItemObject) throws JSONException {
    	return json.getDevicePartItemCount(deviceItemObject);
    }
    public int getStationPartItemCount(Object staionItemObject) throws JSONException {
    	return json.getStationPartItemCount(staionItemObject);
    }
    public List<Object> getPartItemDataList(int stationIndex,int deviceIndex) throws JSONException {
    	List<Object> deviceItemList = json.getDeviceItem(stationIndex);
    	JSONObject object =  (JSONObject)deviceItemList.get(deviceIndex);
     return 	json.getPartList(object);
    }
    
    public Object getPartItemObject(int stationIndex,int deviceIndex) throws JSONException {
    	List<Object> deviceItemList = json.getDeviceItem(stationIndex);
    	JSONObject object =  (JSONObject)deviceItemList.get(deviceIndex);
     return 	object;
    }
    
    public List<String>getDeviceItemDefList(Object deviceItemObject) throws JSONException{
    	 return 	json.getDeviceItemDefList(deviceItemObject);
    }
    public String getDeviceItemName(Object object) {
    	return json.getDeviceItemName(object);
    }
    public String getPartItemName(Object object) {
    	return json.getPartItemName(object);
    }
    public String getPartItemCheckUnitName(Object object,int index) {
    	return json.getPartItemCheckUnitName(object,index);
    }
    
    public int getStationItemIndexByID(String strIdCode) throws JSONException {
    	return json.getStationItemIndexByID(strIdCode);
    }
    public String getPartItemSubStr(String partItemDataStr,int index){
    	return json.getPartItemSubStr(partItemDataStr,index);
    }
    public List<Object> getPartItemListByItemDef(Object partItemobject ,int index) throws JSONException{
    	return json.getPartItemListByItemDef(partItemobject,index);
    }
    public String getRoutName() throws JSONException{
    	return json.getRoutName();
    }
}