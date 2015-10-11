package com.aic.aicdetactor.data;

import java.util.List;

public class AICData {
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
	        
//	        public GlobalInfoJson getGlobalInfo(){
//	        	return GlobalInfo; 
//	        }
//	        public List<StationInfoJson> getStationInfoList(){
//	        	return StationInfo;
//	        } ;
//	        public List<ItemAbnormalGradeJson> getT_Item_Abnormal_Grade(){
//	        	return T_Item_Abnormal_Grade;
//	        } ;
//	        public LineInfoJson getT_Line(){return T_Line;} ;
//	        public List<MeasureTypeJson> getT_Measure_Type(){return T_Measure_Type;} ;
//	        public OrganizationInfoJson getT_Organization(){return T_Organization;} ;
//	        public PeriodInfoJson getT_Period(){return T_Period;} ;
//	        public List<TurnInfoJson> getT_Turn(){return T_Turn; } ;
//	        public List<WorkerInfoJson> getT_Worker(){return T_Worker;} ;
	        
	        //get line item total counts
//	        public int getItemAllCounts(){
//	        	int count =0;
//	        	for(StationInfoJson info:StationInfo){
//	        		//for(int i=0;i<StationInfo.size();i++){
//	        			//StationInfoJson info = StationInfo.get(i);
//	        			List<DeviceItemJson> deviceList=info.getDeviceItem();
//	        			//for(int m=0;m<deviceList.size();m++){
//	        			for(DeviceItemJson deviceItem:deviceList){
//	        				//DeviceItemJson deviceItem = deviceList.get(m);
//	        				count += deviceItem.PartItem.size();
//	        				//List<PartItemJson> partItemList = deviceItem.PartItem;
////	        				for(int n=0;n<partItemList.size();n++){
////	        					
////	        				}
//	        			}
//	        	}
//	        	return count;
//	        }

	    }
	 public   class DownloadTemporaryData
	    {
	        public List<ItemAbnormalGradeJson> T_Item_Abnormal_Grade ;
	        public List<MeasureTypeJson> T_Measure_Type ;
	        public List<TemporaryLineJson> T_Temporary_Line ;
	        public List<MeasureUnitJson> T_Measure_Unit ;
	        
//	        public List<ItemAbnormalGradeJson> getT_Item_Abnormal_Grade(){return T_Item_Abnormal_Grade;} ;
//	        public List<MeasureTypeJson> getT_Measure_Type (){return T_Measure_Type;};
//	        public List<TemporaryLineJson> getT_Temporary_Line (){return T_Temporary_Line;};
//	        public List<MeasureUnitJson> getT_Measure_Unit (){return T_Measure_Unit;};
	    }


	    public class GlobalInfoJson
	    {
	        public String Check_Date ;
	        public String Guid ;
	        public String End_Time ;
	        public String Start_Time ;
	        public String T_Period_Name ;
	        public int Turn_Number ;
	        public int Task_Mode ;
	        
	        //Method
//	        public void setCheck_Date(String date){Check_Date = date;} ;
//	        public void setGuid(String guid) {Guid=guid;};
//	        public void setEnd_Time(String endtime){End_Time=endtime;} ;
//	        public void setStart_Time(String startTiem){Start_Time=startTiem;} ;
//	        public void setT_Period_Name(String PeriodName){T_Period_Name=PeriodName;} ;
//	        public void setTurn_Number(int TurnNumber){Turn_Number=TurnNumber;} ;
//	        public void setTask_Mode(int TaskMode){Task_Mode=TaskMode;} ;
	        
	    }
	    public class StationInfoJson
	    {
	        public String Code ;
	        public String Factory_Code ;
	        public String Guid ;
	        public int Is_In_Place ;
	        public String Name ;
	        public String T_Organization_Guid ;

	        public List<DeviceItemJson> DeviceItem ;
	        
	        //method
//	        public String getCode(){return Code;} ;
//	        public String getFactory_Code(){return Factory_Code;} ;
//	        public String getGuid(){return Guid;} ;
//	        public int getIs_In_Place(){return Is_In_Place;} ;
//	        public String getName(){return Name;} ;
//	        public String getT_Organization_Guid(){return T_Organization_Guid;} ;
//
//	        public List<DeviceItemJson> getDeviceItem(){return DeviceItem;} ;
	    }
	    public class DeviceItemJson//:ICloneable 
	    {
	        public String Asset_Number ;
	        public String Asset_Type ;
	        public String Capacity ;
	        public String Chart_Number ;
	        public String Class ;
	        public String Classification_Number ;
	        public String Code ;
	        public String Data_Exist_Guid ;
	        public String Date_Of_Entering ;

	        public String Date_Of_Production ;
	        public String Date_Of_Start ;
	        public String End_Check_Datetime ;
	        public String Energy_Consumption ;

	        public String English_Name ;
	        public String First_Maintenance ;
	        public String Grade ;
	        public String Installation_Site ;

	        public int Is_Device_Checked ;
	        public int Is_In_Place ;
	        public int Is_Omission_Check ;
	        public int Is_RFID_Checked ;
	        public int Is_Special_Inspection ;
	        public String Item_Define ;
	        public int Inspection_Type ;
	        public String Status_Array ;
	        public String Guid ;
	        public String Manufacturer ;
	        public String Material ;

	        public String Model ;
	        public String Name ;

	        public String Person_In_Charge ;
	        public String Precision ;
	        public String Price ;
	        public String Processing_Size ;
	        public String Rated_Power ;
	        public String Rated_RPM ;
	        public String Rated_Voltage ;
	        public String Rated_Current ;
	        public String Remarks ;
	        public String Safety_Coefficient ;
	        public String Second_Maintenance ;
	        public String Serial_Number ;
	        public String Start_Check_Datetime ;

	        public String Status ;
	        public String Third_Maintenance ;

	        public int Total_Check_Time ;
	        public String Vendor ;

	        public String T_Worker_Class_Group ;
	        public String T_Worker_Class_Shift ;
	        public String T_Worker_Number ;
	        public String T_Worker_Name ;
	        public String T_Worker_Guid ;
	        public List<PartItemJson> PartItem ;
	    }
	    public class PartItemJson
	    {
	     //   public String Fast_Record_Item_Name ;

	        //#if KEYREPLACEASTERISK
	        public String Check_Content ;
	        public long T_Measure_Type_Id ;
	        public String T_Measure_Type_Code ;
	        public String Unit ;
	        public int Start_Stop_Flag ;
	        public float Up_Limit ;
	        public float Middle_Limit ;
	        public float Down_Limit ;
	        public float Emissivity ;
	        public int Hint_Status ;
	        public int Axle_Number ;
	        public String Check_Mode ;
	        public String Extra_Information ;
	        public int T_Maintenance_Status_Id ;
	        public String Fault_Diagnosis ;
	        public float Default_RPM ;
	        //#else
	        public String PartItemData ;
	        //#endif
	        //      

	    }
	    public class ItemAbnormalGradeJson
	    {
	        public String Code ;
	        public int Id ;
	        public String Name ;
	        
//	        public String getCode(){return Code;} ;
//	        public int getId(){return Id;};
//	        public String getName(){return Name;};
	    }

	    public class LineInfoJson
	    {
	        public String Name ;
	        public String T_Line_Content_Guid ;
	        public String T_Line_Guid ;
	        
//	        public String getName(){return Name;} ;
//	        public String getT_Line_Content_Guid(){return T_Line_Content_Guid;} ;
//	        public String getT_Line_Guid(){return T_Line_Guid;} ;
	    }

	    public class MeasureTypeJson
	    {
	        public String Code ;
	        public int Id ;
	        public String Name ;
	    }
	    public class MeasureUnitJson
	    {
	        public String Name ;
	        public int Id ;

	    }
	    public class OrganizationInfoJson
	    {
	        public String CorporationCode ;
	        public String CorporationName ;
	        public String CorporationNameGuid ;
	        public String GroupCode ;
	        public String GroupName ;
	        public String GroupNameGuid ;
	        public String WorkShopCode ;
	        public String WorkShopName ;
	        public String WorkShopNameGuid ;
	    }
	    public class PeriodInfoJson
	    {
	        public String Base_Point ;
	        public int Frequency ;
	        public String Name ;
	        public String Status_Array ;

	        public String T_Line_Content_Guid ;
	        public String T_Line_Guid ;
	        public String T_Period_Unit_Code ;
	        public int T_Period_Unit_Id ;

	        public List<PeriodJson> Periods ;
	    }
	    public class PeriodJson
	    {
	        public int Is_Omission_Check ;
	        public int Is_Permission_Timeout ;
	        public int Span ;
	        public int Start_Point ;
	        public String T_Turn_Number_Array ;
	        public int Task_Mode ;
	        public int Turn_Finish_Mode ;
	    }
	    public class TurnInfoJson
	    {
	        public String End_Time ;
	        public String Name ;
	        public String Start_Time ;
	        public int Number ;
	        public String T_Line_Content_Guid ;
	        public String T_Line_Guid ;
	    }
	    public class WorkerInfoJson
	    {
	        public String Number ;
	        public String Name ;
	        public String Alias_Name ;
	        public String Class_Group ;
	        public String Password ;
	        public String Guid ;
	   //     public String T_Line_Content_Guid ;
	        public String T_Line_Guid ;
	        public String T_Organization_Guid ;
	    }


	    class TemporaryLineJson
	    {
	        public String Title ;
	        public String Content ;
	        public String GroupName ;
	        public String CorporationName ;
	        public String WorkShopName ;
	        public String DevName ;
	        public String DevSN ;
	        public String T_Device_Guid;
	        public int Task_Mode ;
	        public String T_Worker_Name ;
	        public String T_Worker_Number ;
	        public String T_Worker_Guid ;        
	        public String Create_Time ;
	        public String Receive_Time ;
	        public String Feedback_Time ;
	        public String Execution_Time ;
	        public String Finish_Time ;
	        public int Is_Normal ;
	        public String Result ;
	        public String Remarks ;
	        public String Unit ;
	        public int T_Measure_Unit_Id ;
	        public int T_Measure_Type_Id ;
	        public String T_Measure_Type_Code ;
	        public int T_Item_Abnormal_Grade ;
	        public String T_Item_Abnormal_Grade_Code ;
	        public float Up_Limit ;
	        public float Middle_Limit ;
	        public float Down_Limit ;
	        public String T_Temporary_Line_Guid ;
	        public String Data_Exist_Guid ;
	        public int Is_Original_Line ;
	        public int Is_Readed ;
	        public String SaveLab ;
	        public String RecordLab ;
	        public int SensorType ;
	        public int VMSDir ;
	        public int SignalType ;
	        public float SampleFre ;
	        public int SamplePoint ;
	        public float RPM ;
	        public String Diagnose_Conclusion ;

	    }

}
