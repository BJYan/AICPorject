package com.aic.aicdetactor.app;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

















import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.data.MyJSONParse;
import com.aic.aicdetactor.data.Temperature;
import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.database.RouteDao;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class myApplication extends Application
{
    private static final String TAG = "aicdetector";
    
    //当前巡检路线的序号
    public int mRouteIndex = -1;
    //当前巡检路线下站点序号，序号指的是JSON站点数组的序号
    public int mStationIndex =-1;
    //当前巡检路线下 巡检的设备数组的序号
    public int mDeviceIndex = -1;
    //当前巡检的PartItemData
    public int mPartItemIndex = -1;
    
    //顶层常规巡检还是特定巡检。
    public String gRouteClassName = "";
    //当前路线名称
    public String gRouteName = "";
    //当前站点名称
    public String gStationName ="";    
    //当前设备名称
    public String gDeviceName = "";
    //当前巡检项名称
    public String mPartItemName = "";
    
    private String mStrGuid = null;
    public String mWorkerName = null;
    public String mWorkerPwd = null;
    //生成第六个节点用的信息
    public String mStartDate = null;
    public String mTurnNumber = null;
    public String mTurnStartTime = null;
    public String mTurnEndTime = null;
    public String mWorkerNumber = null;    
	private List<String> mFileList = null;	
	private MyJSONParse json = null;
	private RouteDao dao = null;
	
	public boolean gBLogIn = false;

	public void setParItemIndex(int index,String Name){
		mPartItemIndex = index;
	}
	/**
	 * 生成需要保存的JSON文件名字
	 * @return
	 */
	public String genXJFileName(){
		return Environment.getExternalStorageDirectory()+"/"+mStrGuid+mWorkerNumber +".txt";
	}
	/**
	 * 根据传入的用户名及密码查询对应的巡检原文件及工人工号信息
	 * @param name
	 * @param pwsd
	 */
    public void setUserInfo(String name ,String pwsd){
    	mWorkerName = name;
    	mWorkerPwd = pwsd;
    	dao = new RouteDao(this.getApplicationContext());
		mFileList = dao.queryLogIn(mWorkerName, mWorkerPwd);
		for (int i = 0; i < mFileList.size(); i++) {
			insertNewRouteInfo(SystemUtil.createGUID(), mFileList.get(i), this);
			Log.d(TAG,"setUserInfo() i=" + i + ","+ mFileList.get(i));
		}
		List<String> WorkerNumber = dao.queryWorkerNumber(mWorkerName, mWorkerPwd);
		for(int n =0;n<WorkerNumber.size();n++){
			mWorkerNumber = WorkerNumber.get(n);
			Log.d(TAG,"setUserInfo() n=" + n + ","+ mWorkerNumber);
		}
    }
    
    /**
     * 设置当前的巡检路线的序号，指的是ListView里的序号，从0开始
     * @param routeIndex
     */
    public void setCurrentRouteIndex(int routeIndex){
    	this.mRouteIndex = routeIndex;
    }
    
    /**
     * 获取当前的巡检路线序号 到全局变量
     * @return
     */
    public int getCurrentRouteIndex(){
    	return this.mRouteIndex;
    }
    
    /**
     * 设置当前原JSON文件对应的GUID信息到全局变量
     * @param strGuid
     */
    public void setCurrentRoutePlanGuid(String strGuid){
    	this.mStrGuid = strGuid;
    }
    
    /**
     * 获取当前的巡检路线原JSON文件对应的GUID
     * @return
     */
    public String getCurrentRoutePlanGuid(){
    	return this.mStrGuid;
    }
    
    /**
     * 整个应用启动的第一个接口
     */
    @Override
    public void onCreate()
    {
        super.onCreate(); 
        json = new MyJSONParse();
    }
    
    /**
     * 初始化系统信息
     * @return
     */
    public int InitData(){
    	return json.InitData(this.getApplicationContext(),mFileList);
    }
    
    /**
     * 插入新的JSON文件数据到元数据库表
     * @param fileName
     * @param path
     * @param context
     * @return
     */
    public int insertNewRouteInfo(String fileName,String path,Context context){
    	json.insertNewRouteInfo(fileName, path,context);
    	return 1;
    }
    
    /**
     * 获取指定序号的巡检路线对应的站点集合
     * @param routeIndex
     * @return
     * @throws JSONException
     */
    public List<Object> getStationList(int routeIndex) throws JSONException {
     return	json.getStationList(routeIndex);
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

    public CheckStatus getNodeCount(Object Object,int nodeType,int RouteIndex) throws JSONException{
    	return json.getNodeCount(Object,nodeType,RouteIndex);
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

    public Temperature getPartItemTemperatrue(Object object){
    	return json.getPartItemTemperatrue(object);
    }   
    public String getDeviceQueryNumber(Object object){
    	return json.getDeviceQueryNumber(object);
    }
    public void SaveData(int RouteIndex,String fileName){
    	 json.SaveData(RouteIndex,fileName);
    }
    public void setAuxiliaryNode(int RouteIndex,Object object){
    	 json.setAuxiliaryNode(RouteIndex,object);
    }
    
    public List<TurnInfo>getRouteTurnInfoList() throws JSONException{
    	return json.getTurnInfoItem(mRouteIndex);
    }

}