package com.aic.aicdetactor.setting;

import com.aic.aicdetactor.util.SharedPreferencesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DownLoadSetting {
	/************************************************************************************************/
	/*           下载界面上的本地参数   start                                                        */
	/************************************************************************************************/
/**
 * 上传数据类型
 */
	private boolean mNormalLine;
	private boolean mSpecialLine;
	private boolean mTemplLine;
	private boolean mTask;
	private boolean mMationInfo;
	
	/**
	 * 0 仅WIFI情况下自动上传
	 * 1 仅USB情况下自动上传
	 * 2 只要通信连接就自动上传
	 */
	private int mUpDataType=0;
	
	
	
	/**
	 * 下载界面 true 为手动，ｆａｌｓｅ　为自动
	 * 
	 */
	private boolean mPlanUpload=true;
	
	
	/**
	 * 设置界面上的
	 */
	private String mMationName="";
	
	//推送设置开关
	private boolean mbFindNewLinePlan;
	private boolean mbFindNewNews;
	private boolean mbFindNewTask;
	private boolean mbLowSpace;
	
	
	//读卡器
	private String mServerIP;
	private String mRFid;
	
	//存储设置
	private int mCheckDataStorageIndex;
	private int mCheckPictrueStorageIndex;
	private int mCheckAudioRecordIndex;
	private int mCheckTempDataIndex;
	private int mTaskDataIndex;
	/************************************************************************************************/
	/*           下载界面上的本地参数       stop                                                     */
	/************************************************************************************************/
	
	
	/**
	 * 
	 */
	public final static String  bNormalLine="NormalLine";
	public final static String  bSpecialLine="SpecialLine";
	public final static String  bTemplLine="TemplLine";
	public final static String  bTask="Task";
	public final static String  bMationInfo="MationInfo";
	public final static String  iUpDataType="UpDataType";
	public final static String  bPlanUpload="UpDataType";
	public final static String  MationName="MationName";	
	public final static String  bFindNewLinePlan="bFindNewLinePlan";
	public final static String  bFindNewNews="mbFindNewNews";
	public final static String  bFindNewTask="mbFindNewTask";
	public final static String  bLowSpace="mbLowSpace";
	public final static String  ServerIP="mServerIP";
	public final static String  RFid="mRFid";	
	public final static String  iCheckDataStorageIndex="mCheckDataStorageIndex";
	public final static String  iCheckPictrueStorageIndex="mCheckPictrueStorageIndex";
	public final static String  iCheckAudioRecordIndex="mCheckAudioRecordIndex";
	public final static String  iCheckTempDataIndex="mCheckTempDataIndex";
	public final static String  iTaskDataIndex="mTaskDataIndex";
	
	public void  setNormalLine(boolean NormalLine){
		mNormalLine=NormalLine;
	}
	
	public void setSpecialLine(boolean SpecialLine){
		mSpecialLine=SpecialLine;
	}
	
	public void  setTemplLine(boolean TemplLine){
		mTemplLine=TemplLine;
	}
	
	public void setTask(boolean Task){
		mTask=Task;
	}
	
	public void setMationInfo(boolean MationInfo){
		mMationInfo=MationInfo;
	}
	
	public void setUpDataType(int UpDataType){
		mUpDataType=UpDataType;
	}
	public void setPlanUpload(boolean PlanUpload){
		mPlanUpload=PlanUpload;
	}
	public void setMationName(String MationName){
		mMationName=MationName;
	}
	
	public void  setFindNewLinePlan(boolean FindNewLinePlan){
		mbFindNewLinePlan=FindNewLinePlan;
	}
	
	public void setFindNewNews(boolean FindNewNews){
		mbFindNewNews=FindNewNews;
	}
	
	public void  setFindNewTask(boolean FindNewTask){
		mbFindNewTask=FindNewTask;
	}
	
	public void setLowSpace(boolean LowSpace){
		mbLowSpace=LowSpace;
	}
	
	public void setServerIP(String ServerIP){
		mServerIP=ServerIP;
	}
	
	public void setRFid(String RFid){
		mRFid=RFid;
	}
	
	public void setCheckPictrueStorageIndex(int CheckPictrueStorageIndex){
		 mCheckPictrueStorageIndex=CheckPictrueStorageIndex;
	}
	public void setCheckDataStorageIndex(int CheckDataStorageIndex){
		 mCheckDataStorageIndex=CheckDataStorageIndex;
	}
	public void setCheckAudioRecordIndex(int CheckAudioRecordIndex){
		 mCheckAudioRecordIndex=CheckAudioRecordIndex;
	}
	public void setCheckTempDataIndex(int CheckTempDataIndex){
		 mCheckTempDataIndex=CheckTempDataIndex;
	}
	public void setTaskDataIndex(int TaskDataIndex){
		 mTaskDataIndex=TaskDataIndex;
	}
	
	
	public boolean  getNormalLine(){
		return mNormalLine;
	}
	
	public boolean getSpecialLine(){
		return mSpecialLine;
	}
	
	public boolean  getTemplLine(){
		return mTemplLine;
	}
	
	public boolean getTask(){
		return mTask;
	}
	
	public boolean getMationInfo(){
		return mMationInfo;
	}
	
	public int getUpDataType(){
		return mUpDataType;
	}
	public boolean getPlanUpload(){
		return mPlanUpload;
	}
	public String getMationName(){
		return mMationName;
	}
	
	public boolean  getFindNewLinePlan(){
		return mbFindNewLinePlan;
	}
	
	public boolean getFindNewNews(){
		return mbFindNewNews;
	}
	
	public boolean  getFindNewTask(){
		return mbFindNewTask;
	}
	
	public boolean getLowSpace(){
		return mbLowSpace;
	}
	
	public String getServerIP(){
		return mServerIP;
	}
	
	public String getRFid(){
		return mRFid;
	}
	
	public int getCheckPictrueStorageIndex(){
		return mCheckPictrueStorageIndex;
	}
	public int getCheckDataStorageIndex(){
		return mCheckDataStorageIndex;
	}
	public int getCheckAudioRecordIndex(){
		return mCheckAudioRecordIndex;
	}
	public int getCheckTempDataIndex(){
		return mCheckTempDataIndex;
	}
	public int getTaskDataIndex(){
		return mTaskDataIndex;
	}
	
	
	//sharedpreferences 
	private SharedPreferences mPreferences;
	private Context mContext;
	
	public DownLoadSetting(Context content){
		mContext = content;
		mPreferences = mContext.getSharedPreferences("AIC_Setting", mContext.MODE_WORLD_READABLE | mContext.MODE_WORLD_WRITEABLE);
	}
	
	/**
	 * 从本地文件中读取配置信息
	 */
	public  void  readParams(){
		mNormalLine=mPreferences.getBoolean(bNormalLine, mNormalLine);
		mSpecialLine=mPreferences.getBoolean(bSpecialLine, mSpecialLine);
		mTemplLine=mPreferences.getBoolean(bTemplLine, mTemplLine);
		mTask=mPreferences.getBoolean(bTask, mTask);        
		mMationInfo=mPreferences.getBoolean(bMationInfo, mMationInfo); 
		mUpDataType=mPreferences.getInt(iUpDataType, mUpDataType);
		mPlanUpload=mPreferences.getBoolean(bPlanUpload, mPlanUpload);
		mMationName=mPreferences.getString(MationName, mMationName);        
		mbFindNewLinePlan=mPreferences.getBoolean(bFindNewLinePlan, mbFindNewLinePlan);
		mbFindNewNews=mPreferences.getBoolean(bFindNewNews, mbFindNewNews);
		mbFindNewTask=mPreferences.getBoolean(bFindNewTask, mbFindNewTask);        
		mbLowSpace=mPreferences.getBoolean(bLowSpace, mbLowSpace);
		mServerIP=mPreferences.getString(ServerIP, mServerIP);
		mRFid=mPreferences.getString(RFid, mRFid);
        
		mCheckDataStorageIndex=mPreferences.getInt(iCheckDataStorageIndex, mCheckDataStorageIndex);
		mCheckPictrueStorageIndex=mPreferences.getInt(iCheckPictrueStorageIndex, mCheckPictrueStorageIndex);
		mCheckAudioRecordIndex=mPreferences.getInt(iCheckAudioRecordIndex, mCheckAudioRecordIndex);
		mCheckTempDataIndex=mPreferences.getInt(iCheckTempDataIndex, mCheckTempDataIndex);
		mTaskDataIndex=mPreferences.getInt(iTaskDataIndex, mTaskDataIndex);
        
		
	}
	
	/**
	 * 写内存数据到本地文件中
	 */
	public void writeParams(){
		Editor ed = mPreferences.edit();
        ed.putBoolean(bNormalLine, mNormalLine);
        ed.putBoolean(bSpecialLine, mSpecialLine);
        ed.putBoolean(bTemplLine, mTemplLine);
        ed.putBoolean(bTask, mTask);        
        ed.putBoolean(bMationInfo, mMationInfo); 
        ed.putInt(iUpDataType, mUpDataType);
        ed.putBoolean(bPlanUpload, mPlanUpload);
        ed.putString(MationName, mMationName);        
        ed.putBoolean(bFindNewLinePlan, mbFindNewLinePlan);
        ed.putBoolean(bFindNewNews, mbFindNewNews);
        ed.putBoolean(bFindNewTask, mbFindNewTask);        
        ed.putBoolean(bLowSpace, mbLowSpace);
        ed.putString(ServerIP, mServerIP);
        ed.putString(RFid, mRFid);
        
        ed.putInt(iCheckDataStorageIndex, mCheckDataStorageIndex);
        ed.putInt(iCheckPictrueStorageIndex, mCheckPictrueStorageIndex);
        ed.putInt(iCheckAudioRecordIndex, mCheckAudioRecordIndex);
        ed.putInt(iCheckTempDataIndex, mCheckTempDataIndex);
        ed.putInt(iTaskDataIndex, mTaskDataIndex);
        
    SharedPreferencesCompat.apply(ed);
	}
	
}
