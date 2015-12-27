package com.aic.aicdetactor.data;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.util.SystemUtil;
import com.alibaba.fastjson.JSON;


public class DeviceItemJson implements Cloneable//:ICloneable 
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
    
    public  void clone(DeviceItemJson device){
    	this.	Asset_Number	=	device.	Asset_Number;
    	this.Asset_Type = device.Asset_Type;
    	this.Capacity=device.Asset_Type;
    	this.Chart_Number=device.Chart_Number;
    	this.Class=device.Class;
    	this.Classification_Number=device.Classification_Number;
    	this.Code=device.Code;
    	this.Data_Exist_Guid= device.Data_Exist_Guid;
    	this.Date_Of_Entering= device.Date_Of_Entering;
    	this.Date_Of_Production= device.Date_Of_Production;
    	this.Date_Of_Start= device.Date_Of_Start;    	
    	this.End_Check_Datetime= device.End_Check_Datetime;  
    	this.Energy_Consumption=device.Energy_Consumption;
    	this.English_Name	=	device.	English_Name;
    	this.First_Maintenance	=	device.	First_Maintenance;
    	this.Grade=device.Grade;
    	this.Guid	=	device.	Guid	;
    	this.	Installation_Site	=	device.	Installation_Site	;
    	this.	Is_Device_Checked	=	device.	Is_Device_Checked	;
    	this.	Is_In_Place	=	device.	Is_In_Place	;
    	this.	Is_Omission_Check	=	device.	Is_Omission_Check	;
    	this.	Is_RFID_Checked	=	device.	Is_RFID_Checked	;
    	this.	Is_Special_Inspection	=	device.	Is_Special_Inspection	;
    	this.	Item_Define	=	device.	Item_Define	;
    	this.	Inspection_Type	=	device.	Inspection_Type	;    	  	
    	this.	Manufacturer	=	device.	Manufacturer	;
    	this.	Material	=	device.	Material	;
    	this.	Model	=	device.	Model	;
    	this.	Name	=	device.	Name	;
    	this.	Person_In_Charge	=	device.	Person_In_Charge	;
    	this.	Precision	=	device.	Precision	;
    	this.	Price	=	device.	Price	;
    	this.	Processing_Size	=	device.	Processing_Size	;
    	this.	Rated_Power	=	device.	Rated_Power	;
    	this.	Rated_RPM	=	device.	Rated_RPM	;
    	this.	Rated_Voltage	=	device.	Rated_Voltage	;
    	this.	Rated_Current	=	device.	Rated_Current	;
    	this.	Remarks	=	device.	Remarks	;
    	this.	Safety_Coefficient	=	device.	Safety_Coefficient	;
    	this.	Second_Maintenance	=	device.	Second_Maintenance	;
    	this.	Serial_Number	=	device.	Serial_Number	;
    	this.	Start_Check_Datetime	=	device.	Start_Check_Datetime	;
    	this.	Status	=	device.	Status	;
    	this.	Status_Array	=	device.	Status_Array	;  
    	this.	Third_Maintenance	=	device.	Third_Maintenance	;
    	this.	Total_Check_Time	=	device.	Total_Check_Time	;    	
    	this.	T_Worker_Class_Group	=	device.	T_Worker_Class_Group	;
    	this.	T_Worker_Class_Shift	=	device.	T_Worker_Class_Shift	;
    	this.	T_Worker_Number	=	device.	T_Worker_Number	;
    	this.	T_Worker_Name	=	device.	T_Worker_Name	;
    	this.	T_Worker_Guid	=	device.	T_Worker_Guid	;
    	this.	Vendor	=	device.	Vendor	;
    	this.	PartItem	=	new ArrayList<PartItemJsonUp>();
    	for(int k=0;k<device.PartItem.size();k++){
    		//PartItemJsonUp up= new PartItemJsonUp();
    		//up.Clone(device.PartItem.get(k));
    		PartItemJsonUp	up=(PartItemJsonUp) device.PartItem.get(k).clone();
    		this.	PartItem.add(up);
    	}
    		;

    }
  
    
//    public Object clone() { 
//    	DeviceItemJson o = null; 
//        try { 
//          o = (DeviceItemJson) super.clone(); 
//        } catch (CloneNotSupportedException e) { 
//          System.out.println(e.toString()); 
//        } 
//        for(int i =0;i<PartItem.size();i++)
//        {
//        o.PartItem.add((PartItemJsonUp) PartItem.get(i).clone());
//        	}
//        return o; 
//    	//String jsonData=JSON.toJSONString(this);
//    	
//    	//o = JSON.parseObject(jsonData,DeviceItemJson.class); 
//    	
//    	//序列化 后 再反序列化
//    //	 return o; 
//      } 
}
