package com.aic.aicdetactor.data;

public class Worker {
	public static String GROUP_Name = "GroupName";
	public static String WORKER_Name = "WorkerName";
	public static String WORKER_NUMBER = "WorkerNumber";
	public class WorkerInfo{
	public String GroupName = null;
	public String WorkerName = null;
	public String WorkerNumber = null;
	}
	private WorkerInfo mWorkerInfo=null;
	public Worker(){
		mWorkerInfo = new WorkerInfo();
	}
	
	public void setWorkerInfo(String GroupName,String WorkerName,String WorkerNumber){
		if(mWorkerInfo !=null){
			mWorkerInfo.GroupName = GroupName;
			mWorkerInfo.WorkerName=WorkerName;
			mWorkerInfo.WorkerNumber = WorkerNumber;
		}
	}
	
	public WorkerInfo getWorkerInfo(){
		return mWorkerInfo;
	}
}
