package com.aic.aicdetactor.data;

import java.util.List;

import com.aic.aicdetactor.util.SystemUtil;


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
    public List<PartItemJsonUp> PartItem ;
    
    
    /**
     * 设置巡检开始时间
     */
    public void setStartDate(){
    	Start_Check_Datetime = SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
    }
    
    public void setEndDate(){
    	End_Check_Datetime = SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
    }
    
    public void calcCheckDuration(){
    	Total_Check_Time=Integer.valueOf(SystemUtil.getDiffDate(Start_Check_Datetime, End_Check_Datetime));
    }
    
    public void setRFChecked(){
    	Is_RFID_Checked=1;
    }
    
    public void setDeviceChecked(){
    	Is_Device_Checked=1;
    }
    public void setDataExistGuid(){
    	if(Data_Exist_Guid.length()<20){
    	Data_Exist_Guid=SystemUtil.createGUID();
    	}
    }
    
    public void setIsOmissionCheck(int omissionCheck){
    	Is_Omission_Check=omissionCheck;
    }
    
    public void setWorkerInfos(String WorkerClassGroup,String WorkerClassShift,String WorkerNumber,String WorkerName,String WorkerGuid){
    	    T_Worker_Class_Group =WorkerClassGroup;
    	    T_Worker_Class_Shift= WorkerClassShift;
    	    T_Worker_Number =WorkerNumber;
    	    T_Worker_Name =WorkerName;
    	    T_Worker_Guid =WorkerGuid;
    }
}
