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
    public String Item_Define;
    
    //上传数据时候 需要的项
    public String  Start_Check_Datetime;
    public String End_Check_Datetime;
    public int Total_Check_Time;//单位S
    public String SaveLab;
    public String RecordLab;
    public int SensorType=0;//始终为0
    public String VMSDir;
    public  String SignalType="";
    public String SampleFre;
    public String SamplePoint;
    public String RPM;
    public String Diagnose_Conclusion;
    public String Remarks;
    public String Is_Timeout;
    public String T_Item_Abnormal_Grade_Id;
    public String T_Item_Abnormal_Grade_Code;
    
    
    
   
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
    		VMSDir=String.valueOf(XYZ);
    	}else{
    		VMSDir="";
    	}
    		
    }
    
    /**
     * 当测量类型为加速度、速度、位移时，对应值分别为0、1、2，其它数据类型为空
     */
    public void setSignalType(){
    	int type = Is3TypeData();
    	if(type>0){
    	if(type==3)	{
    		SignalType="0";
    	}else if(type==4)	{
    		SignalType="1";
    	}else if(type==5)	{
    		SignalType="2";
    	}
    	}else{
    		SignalType="";
    	}
    }
    
    
    
    /**
     * 当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
     * @param ValueStr
     */
    public void setSampleFre(String ValueStr){
    	if(Is3TypeData()>0){
    		SampleFre = ValueStr;
    	}else{
    		ValueStr="";
    	}
    }
    
    /**
     * 当测量类型为加速度、速度、位移时，不为空；其它数据类型为空
     * @param SamplePointValue
     */
    public void setSamplePoint(String SamplePointValue){
    	if(Is3TypeData()>0){
    		SamplePoint = SamplePointValue;
    	}else{
    		SamplePoint="";
    	}
    }
    /**
     * 当测量类型为加速度、速度、位移时，不为空，编码参考上限值；其它数据类型为空。
     * @param RPMValue
     */
    public void setRPM(String RPMValue){
    	if(Is3TypeData()>0){
    		RPM = RPMValue;
    	}else{
    		RPM="";
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
	    //#else
	      PartItemData="" ;
	    //#endif
    }
}
