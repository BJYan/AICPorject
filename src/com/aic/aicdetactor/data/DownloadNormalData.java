package com.aic.aicdetactor.data;

import java.util.List;



public  class DownloadNormalData
{
    public GlobalInfoJson GlobalInfo ;
    public List<StationInfoJson> StationInfo ;
    public List<ItemAbnormalGradeJson> T_Item_Abnormal_Grade ;
    public LineInfoJson T_Line ;
    public List<MeasureTypeJson> T_Measure_Type ;
    public OrganizationInfoJson T_Organization ;
    public PeriodInfoJson T_Period ;
    public List<TurnInfoJson> T_Turn ;
    public List<WorkerInfoJson> T_Worker ;
    
//    public GlobalInfoJson getGlobalInfo(){
//    	return GlobalInfo; 
//    }
//    public List<StationInfoJson> getStationInfoList(){
//    	return StationInfo;
//    } ;
//    public List<ItemAbnormalGradeJson> getT_Item_Abnormal_Grade(){
//    	return T_Item_Abnormal_Grade;
//    } ;
//    public LineInfoJson getT_Line(){return T_Line;} ;
//    public List<MeasureTypeJson> getT_Measure_Type(){return T_Measure_Type;} ;
//    public OrganizationInfoJson getT_Organization(){return T_Organization;} ;
//    public PeriodInfoJson getT_Period(){return T_Period;} ;
//    public List<TurnInfoJson> getT_Turn(){return T_Turn; } ;
//    public List<WorkerInfoJson> getT_Worker(){return T_Worker;} ;
    
    //get line item total counts
    public int getItemAllCounts(){
    	int count =0;
    	for(StationInfoJson info:StationInfo){
    		//for(int i=0;i<StationInfo.size();i++){
    			//StationInfoJson info = StationInfo.get(i);
    			List<DeviceItemJson> deviceList=info.DeviceItem;
    			//for(int m=0;m<deviceList.size();m++){
    			for(DeviceItemJson deviceItem:deviceList){
    				//DeviceItemJson deviceItem = deviceList.get(m);
    				count += deviceItem.PartItem.size();
    				//List<PartItemJson> partItemList = deviceItem.PartItem;
//    				for(int n=0;n<partItemList.size();n++){
//    					
//    				}
    			}
    	}
    	return count;
    }

}
