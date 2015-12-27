package com.aic.aicdetactor.data;

import android.util.Log;

import com.aic.aicdetactor.util.SystemUtil;

public class PartItemJsonUp implements Cloneable
{
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
    public String  Start_Check_Datetime; //上传数据时候 需要的项
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


    public void setExtralInfor(String value){
    	if(T_Measure_Type_Id==9
    			||T_Measure_Type_Id==10){
    		Extra_Information="";
    		}else {
    			Extra_Information=value;
        	}
    }
    
   /**
    * 当测量类型为加速度、速度、位移时，=0,=1,=2分别代表，X,Y,Z轴，其它类型为空。
    * @param XYZ,0:X,1:Y,2:Z
    */
    public void setVMSDir(){
    	
		if(T_Measure_Type_Id==4){
		VMSDir=0;
		}else if(T_Measure_Type_Id==5){
		VMSDir=1;
		}else if(T_Measure_Type_Id==6){
    		VMSDir=2;
		}else{
		VMSDir=-1;
    	}
    		
    }
    
    /**
     * 当测量类型为加速度、速度、位移时，对应值分别为0、1、2，其它数据类型为空
     */
    public void setSignalType(){
	if(T_Measure_Type_Id==4){
		SignalType=0;
		}else if(T_Measure_Type_Id==5){
			SignalType=1;
		}else if(T_Measure_Type_Id==6){
			SignalType=2;
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
	   Start_Check_Datetime=""; //上传数据时候 需要的项
	   End_Check_Datetime="";
	   Total_Check_Time=0;//单位S
	   SaveLab="";
	   RecordLab="";
	   SensorType=0;//始终为0
	   VMSDir=0;
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
   
   
   public void Clone(PartItemJsonUp up){
		  Check_Content=up.Check_Content;
		  T_Measure_Type_Id=up.T_Measure_Type_Id;
		  T_Measure_Type_Code=up.T_Measure_Type_Code;
		  Unit =up.Unit;
		  Start_Stop_Flag=up.Start_Stop_Flag;
		  Up_Limit=up.Up_Limit;
		  Middle_Limit=up.Middle_Limit;
		  Down_Limit=up.Down_Limit;
		  Emissivity=up.Emissivity;
		  Hint_Status=up.Hint_Status;
		  Axle_Number=up.Axle_Number;
		  Check_Mode=up.Check_Mode;
		  Extra_Information =up.Extra_Information;
		  T_Maintenance_Status_Id=up.T_Maintenance_Status_Id;
		  Fault_Diagnosis=up.Fault_Diagnosis;
		  Default_RPM=up.Default_RPM;
		  Is_Normal=up.Is_Normal;
		   Item_Define=up.Item_Define;
		   Start_Check_Datetime=up.Start_Check_Datetime;
		   End_Check_Datetime=up.End_Check_Datetime;
		   Total_Check_Time=up.Total_Check_Time;
		   SaveLab=up.SaveLab;
		   RecordLab=up.RecordLab;
		   SensorType=up.SensorType;
		   VMSDir=up.VMSDir;
		   SignalType=up.SignalType;
		   SampleFre=up.SampleFre;
		   SamplePoint=up.SamplePoint;
		   RPM=up.RPM;
		   Diagnose_Conclusion=up.Diagnose_Conclusion;
		   Remarks=up.Remarks;
		   Is_Timeout=up.Is_Timeout;
		   T_Item_Abnormal_Grade_Id=up.T_Item_Abnormal_Grade_Id;
		   T_Item_Abnormal_Grade_Code=up.T_Item_Abnormal_Grade_Code;
   }
   
  public void printdata(){
	   Log.d("PartItemJsonUp","Check_Content="+ Check_Content+",T_Measure_Type_Id="+
	  T_Measure_Type_Id+",T_Measure_Type_Code="+
	  T_Measure_Type_Code+",Unit= "+
	  Unit +",Start_Stop_Flag="+
	  Start_Stop_Flag+",Up_Limit="+
	  Up_Limit+",Middle_Limit="+
	  Middle_Limit+",Down_Limit="+
	  Down_Limit+",Emissivity="+
	  Emissivity+",Hint_Status="+
	  Hint_Status+",Axle_Number="+
	  Axle_Number+",Check_Mode="+
	  Check_Mode+",Extra_Information="+
	  Extra_Information +",T_Maintenance_Status_Id="+
	  T_Maintenance_Status_Id+",Fault_Diagnosis="+
	  Fault_Diagnosis+",Default_RPM="+
	  Default_RPM+",Is_Normal="+	
	  Is_Normal+",Item_Define="+     
	   Item_Define+",Start_Check_Datetime="+
	   Start_Check_Datetime+",End_Check_Datetime="+
	   End_Check_Datetime+",Total_Check_Time="+
	   Total_Check_Time+",SaveLab="+
	   SaveLab+",RecordLab="+
	   RecordLab+",SensorType="+
	   SensorType+",VMSDir="+
	   VMSDir+",SignalType="+
	   SignalType+",SampleFre="+
	   SampleFre+",SamplePoin="+
	   SamplePoint+",RPM="+
	   RPM+",Diagnose_Conclusion="+
	   Diagnose_Conclusion+",Remarks="+
	   Remarks+",Is_Timeout="+
	   Is_Timeout+",T_Item_Abnormal_Grade_Id="+
	   T_Item_Abnormal_Grade_Id+",T_Item_Abnormal_Grade_Code="+
	   T_Item_Abnormal_Grade_Code);
   }
  
  public Object clone() { 
	  PartItemJsonUp o = null; 
	    try { 
	      o = (PartItemJsonUp) super.clone(); 
	    } catch (CloneNotSupportedException e) { 
	      System.out.println(e.toString()); 
	    } 
	 
	    return o; 
	  } 
}
