package com.aic.aicdetactor.data;

import com.aic.aicdetactor.util.SystemUtil;

public class PartItemJsonUp
{
 //   public String Fast_Record_Item_Name ;

    //#if KEYREPLACEASTERISK
    public String Check_Content ;
    public long T_Measure_Type_Id ;
    public String T_Measure_Type_Code ;
    public String Unit ;   
    public int Start_Stop_Flag ;
    public double Up_Limit ;
    public double Middle_Limit ;
    public double Down_Limit ;    
    public double Emissivity ;
   
    public double Default_RPM ;    //  
    public int Hint_Status ;   
    public int Axle_Number ;
    public String Check_Mode ;    
    public String Extra_Information ;
    public int T_Maintenance_Status_Id ;
    public String Fault_Diagnosis ;
   
    public String Item_Define;
    
    //上传数据时候 需要的项
    public String  Start_Check_Datetime;
    public String End_Check_Datetime;
    public int Total_Check_Time;//单位S
    public String SaveLab;
    public String RecordLab;
    public int SensorType=0;//始终为0
    public int VMSDir;
    public int SignalType=-1;
   
    public int SamplePoint;
    
    public String Diagnose_Conclusion;
    public String Remarks;
    public int Is_Timeout;
    public long T_Item_Abnormal_Grade_Id;
    public String T_Item_Abnormal_Grade_Code;
    public Integer RPM;
    public int SampleFre;
    public int Is_Normal;
    
   
    private int Is3TypeData(){
    	String JiaSuDu="03";
    	String SuDu="04";
    	String WeiYi="05";
    	if(JiaSuDu.endsWith(T_Measure_Type_Code)
    			||SuDu.endsWith(T_Measure_Type_Code)
    			||WeiYi.endsWith(T_Measure_Type_Code)){
    		return Integer.valueOf(T_Measure_Type_Code);
    	}
    	return 0;
    }


    
    
   /**
    * 当测量类型为加速度、速度、位移时，=0,=1,=2分别代表，X,Y,Z轴，其它类型为空。
    * @param XYZ,0:X,1:Y,2:Z
    */
    public void setVMSDir(int XYZ){
    	if(Is3TypeData()>0){
    		VMSDir=XYZ;
    	}else{
    		VMSDir=-1;
    	}
    		
    }
    
    /**
     * 当测量类型为加速度、速度、位移时，对应值分别为0、1、2，其它数据类型为空
     */
    public void setSignalType(){
    	int type = Is3TypeData();
    	if(type>0){
    	if(type==3)	{
    		SignalType=0;
    	}else if(type==4)	{
    		SignalType=1;
    	}else if(type==5)	{
    		SignalType=2;
    	}
    	}else{
    		SignalType=-1;
    	}
    }
    
    
    
    /**
     * 当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
     * @param ValueStr
     */
    public void setSampleFre(double ValueStr){
    	if(Is3TypeData()>0){
    		SampleFre =2;//ValueStr;
    	}else{
    		ValueStr=-1;
    	}
    }
    
    /**
     * 当测量类型为加速度、速度、位移时，不为空；其它数据类型为空
     * @param SamplePointValue
     */
    public void setSamplePoint(int SamplePointValue){
    	if(Is3TypeData()>0){
    		SamplePoint = SamplePointValue;
    	}else{
    		SamplePoint=-1;
    	}
    }
    /**
     * 当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
     * @param RPMValue
     */
    public void setRPM(double RPMValue){
    	if(Is3TypeData()>0){
    		RPM = 3;//RPMValue;
    	}else{
    		RPM=0;
    	}
    }
    
    /**
     * 当测量类型为加速度、速度、位移时，不为空；其它数据类型为空
     * @param DiagnoseConclusionValue
     */
    public void setDiagnoseConclusion(String DiagnoseConclusionValue){
    	if(Is3TypeData()>0){
    		Diagnose_Conclusion = DiagnoseConclusionValue;
    	}else{
    		Diagnose_Conclusion="";
    	}
    }
    
    public void setSartDate(){
    	Start_Check_Datetime = SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
    }
    
    public void setEndDate(){
    	End_Check_Datetime = SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM);
    }
    
    public void calcCheckDuration(){
    	Total_Check_Time=Integer.valueOf(SystemUtil.getDiffDate(Start_Check_Datetime, End_Check_Datetime));
    }
    
    public void setIsTimeOut(boolean bIsTimeOut){
    	this.Is_Timeout=bIsTimeOut==true?1:0;
    }
    
    
    public boolean IsNoNeedHint(){
    	return Hint_Status==2?true:false;
    }
    public boolean CheckIsNeedHint(){
    	return Hint_Status==1?true:false;
    }
   public  PartItemJsonUp(){
	     Check_Content="" ;
	      T_Measure_Type_Id=-1 ;
	      T_Measure_Type_Code="-1";
	      Unit ="";
	      Start_Stop_Flag=0 ;
	      Up_Limit=0 ;
	      Middle_Limit=0 ;
	      Down_Limit=0 ;
	      Emissivity=0 ;
	      Hint_Status=0 ;
	      Axle_Number=0 ;
	      Check_Mode="" ;
	      Extra_Information ="";
	      T_Maintenance_Status_Id=0 ;
	      Fault_Diagnosis="" ;
	      Default_RPM=0 ;	
	      Is_Normal=0;
	      
	       Item_Define="";
	      
	      //上传数据时候 需要的项
	       Start_Check_Datetime="";
	       End_Check_Datetime="";
	       Total_Check_Time=0;//单位S
	       SaveLab="";
	       RecordLab="";
	       SensorType=0;//始终为0
	       VMSDir=-1;
	      SignalType=-1;
	      SampleFre=-1;
	       SamplePoint=-1;
	       RPM=0;
	       Diagnose_Conclusion="";
	       Remarks="";
	       Is_Timeout=0;
	       T_Item_Abnormal_Grade_Id=-1;
	       T_Item_Abnormal_Grade_Code="";
    }
}
