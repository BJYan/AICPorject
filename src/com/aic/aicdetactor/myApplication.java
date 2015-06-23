package com.aic.aicdetactor;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.aic.aicdetactor.util.MyJSONParse;

import android.app.Application;

public class myApplication extends Application
{
    private static final String VALUE = "aicdetector";
    
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
    public List<String>getDeviceItemDefList(Object deviceItemObject) throws JSONException{
    	 return 	json.getDeviceItemDefList(deviceItemObject);
    }
    public String getDeviceItemName(Object object) {
    	return json.getDeviceItemName(object);
    }
    public String getPartItemName(Object object) {
    	return json.getPartItemName(object);
    }
    
    public int getStationItemIndexByID(String strIdCode) throws JSONException {
    	return json.getStationItemIndexByID(strIdCode);
    }
    public String getPartItemSubStr(String partItemDataStr,int index){
    	return json.getPartItemSubStr(partItemDataStr,index);
    }
    public List<Object> getPartItemListByItemDef(Object object ,int index) throws JSONException{
    	return json.getPartItemListByItemDef(object,index);
    }
    public String getRoutName() throws JSONException{
    	return json.getRoutName();
    }
}