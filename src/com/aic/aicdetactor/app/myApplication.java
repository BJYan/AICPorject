package com.aic.aicdetactor.app;

import java.util.List;
import java.util.Map;

import android.app.Application;

import com.aic.aicdetactor.data.DownloadNormalData;
import com.aic.aicdetactor.data.WorkerInfoJson;

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
//    public String gStationName ="";    
//    //当前设备名称
//    public String gDeviceName = "";
//    //当前巡检项名称
//    public String mPartItemName = "";
    
//    private String mStrGuid = null;
    //登录的工人用户名
    private String mWorkerName = null;
    //登录用户名对应的密码
    private String mWorkerPwd = null;
    //登录工人的工号
    public String mWorkerNumber = null;
    //生成第六个节点用的信息
    public String mStartDate = null;
    public String mTurnNumber = null;
    public String mTurnStartTime = null;
    public String mTurnEndTime = null;
      
    public List<Map<String,String>> mFileList = null;	
    public DownloadNormalData mNormalLineJsonData=null;
	private List<String> mTMPRouteFileList = null;
	public List<WorkerInfoJson> gWorkerInfoJsonList=null;
	private boolean gBLogIn = false;

	public void setParItemIndex(int index,String Name){
		mPartItemIndex = index;
	}
	/**
	 * 生成需要保存的JSON文件名字
	 * @return
	 */
//	public String genXJFileName(){
//		return Environment.getExternalStorageDirectory()+"/"+mStrGuid+mWorkerNumber +".txt";
//	}
    
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
     * 整个应用启动的第一个接口
     */
    @Override
    public void onCreate()
    {
        super.onCreate(); 
    }
    
    /**
     * 初始化系统信息
     * @return
     */
    public int InitData(){
    	return 0;
    }
    
    public void setLogInStatus(boolean loginSuccess){
    	this.gBLogIn=loginSuccess;
    }
    
    public boolean isLogin(){
    	return gBLogIn;
    }
    
    public void setLoginWorkerName(String name){
    	mWorkerName=name;
    }
    public String getLoginWorkerName(){
    	return mWorkerName;
    }
    public void setLoginWorkerPwd(String pwd){
    	mWorkerPwd=pwd;
    }
    public String getLoginWorkerPwd(){
    	return mWorkerPwd;
    }

}