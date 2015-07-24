package com.aic.aicdetactor.app;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;









import com.aic.aicdetactor.data.CheckStatus;

import com.aic.aicdetactor.data.MyJSONParse;
import com.aic.aicdetactor.data.Temperature;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class myApplication extends Application
{
    private static final String VALUE = "aicdetector";
    
    
    
   // private String value;
   // public String mPath = "/sdcard/down.txt";
    MyJSONParse json = new MyJSONParse();
    @Override
    public void onCreate()
    {
        super.onCreate(); 
    }
    public int InitData(){
    	return json.InitData(this.getApplicationContext());
    }
    public int insertNewRouteInfo(String fileName,String path,Context context){
    	json.insertNewRouteInfo(fileName, path,context);
    	return 1;
    }
    public List<Object> getStationList(int routeIndex) throws JSONException {
     return	json.getStationList(routeIndex);
    }
    public CheckStatus getRoutePartItemCount(int routeIndex) throws JSONException{
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
    public CheckStatus getDevicePartItemCount(Object deviceItemObject) throws JSONException {
    	return json.getDevicePartItemCount(deviceItemObject);
    }
    public CheckStatus getStationPartItemCount(Object staionItemObject) throws JSONException {
    	return json.getStationPartItemCount(staionItemObject);
    }
    public List<Object> getPartItemDataList(int stationIndex,int deviceIndex) throws JSONException {
    	List<Object> deviceItemList = json.getDeviceItem(stationIndex);
    	JSONObject object =  (JSONObject)deviceItemList.get(deviceIndex);
     return 	json.getPartList(object);
    }
    
    public Object getPartItemObject(int stationIndex,int deviceIndex) throws JSONException {
    	List<Object> deviceItemList = json.getDeviceItem(stationIndex);
    	for(int i =0;i<deviceItemList.size();i++){
    		Log.d("testkey",deviceItemList.get(i).toString());
    	}
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
    public JSONObject setPartItem_ItemDef(JSONObject partItemDataJson,int deviceIndex,String Value){
    	return json.setPartItem_ItemDef(partItemDataJson,deviceIndex,Value);
    }
    public int getStationItemIndexByID(int routeIndex,String strIdCode) throws JSONException {
    	return json.getStationItemIndexByID(routeIndex,strIdCode);
    }
    public String getPartItemSubStr(String partItemDataStr,int index){
    	return json.getPartItemSubStr(partItemDataStr,index);
    }
    public List<Object> getPartItemListByItemDef(Object partItemobject ,int index) throws JSONException{
    	return json.getPartItemListByItemDef(partItemobject,index);
    }
    public String getRoutName(int routeIndex) throws JSONException{
    	return json.getRoutName(routeIndex);
    }
    public Object addIsChecked(Object DeviceItemJson,boolean bValue){
    	return json.addIsChecked(DeviceItemJson,bValue);
    }
    public Temperature getPartItemTemperatrue(Object object){
    	return json.getPartItemTemperatrue(object);
    }   

}