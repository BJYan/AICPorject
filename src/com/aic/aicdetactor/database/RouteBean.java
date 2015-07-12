package com.aic.aicdetactor.database;

public class RouteBean {
	/*
	 * 计划巡检名称
	 */
	public String mjxName=null;
	/*
	 * 存储路径
	 */
	public String mPath =null;
	
	/**
	 * 计划巡检下载时间
	 */
	public String mDownLoadTime = null;
	
	/*
	 * 是否已经巡检完毕
	 */
	public boolean mIsChecked = false;
	
	/*
	 * guid 用于存储文件的文件名
	 */
	public String mGUID= null;
	
	/*
	 * 是否已上传服务器
	 */
	public boolean mIsUploaded = false;
	
	/*
	 * worker name
	 */
	public String mWorkerName = null;
	
    /*
     * 第一次碰ID 口时间
     */
	public String mFirstCheckTime = null;
	
	/*最近巡检时间
	 * 
	 */
	public String mLastCheckTime = null;
	
	/*
	 * 最近的巡检站点索引
	 */
	public int mLastCheckedStationIndex =-1;
	/*
	 * 最近的巡检设备索引
	 */
	public int mLastCheckedDeviceIndex =-1;
	/*
	 * 最近的巡检项索引
	 */
	public int mLastCheckedPartItemIndex =-1;
	/*
	 * 最近巡检项是否反向巡检
	 */
	public boolean mIsReverseChecking = false;
	/*
	 * 是否已开始巡检
	 */
	public boolean mIsBeiginCheck=false;
}
