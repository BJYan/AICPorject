package com.aic.aicdetactor.data;

public class PartItemJson
{
 //   public String Fast_Record_Item_Name ;

    //#if KEYREPLACEASTERISK
    public String Check_Content ;
    public long T_Measure_Type_Id ;
    public String T_Measure_Type_Code ;
    public String Unit ;
    /**
     * 启停标志位，某一位为0,表示不检；为1表示要检，默认只有第1位为1. 第1位对应T_Period.Status_Array的第一个“/”,以此类推，
     */
    public int Start_Stop_Flag ;
    public float Up_Limit ;
    public float Middle_Limit ;
    public float Down_Limit ;
    /**
     * 发射率。测温时用，不同的物体发射率不同，默认值为1.0，其它测量项禁止。
     */
    public float Emissivity ;
    /**
     * 本条巡检项在APP端提示是否检，=0必检，没有提示；=1在APP端提示用户是否检；=2不检。
     */
    public int Hint_Status ;
    /**
     * 振动轴数，当测量类型T_Measure_Type.Code=03(加速度)、=04(速度)、=05(位移)时,=1单轴，=2双轴，=3三轴。如测量不为这三种，为空.
     */
    public int Axle_Number ;
    public String Check_Mode ;
    /**
     * 1.测量类型为“观察”、“预设状况”时，需要额外的信息。
“观察”与“预设状况”在上位机编辑方法、格式都一样，在APP里处理“观察”方法是只能多选一，但有编辑功能；“预设状况”既可多选一，也可多选多，但无编辑功能。
    “观察”每个子项用“/”分开，如“压力正常/温度异常/漏油/噪声过大”，每个子项在最后两个字节可指定异常级别（T_Item_Abnormal Grade）,真正的格式如：”压力正常01/温度异常05/漏油08/噪声过大09“，
    “预设状况”每个子项用“/”分开，每个子项不需要加控制字符，但在APP里，整个“预设状况”作为异常开看待
   2.其它类型，为空。
     */
    public String Extra_Information ;
    public int T_Maintenance_Status_Id ;
    public String Fault_Diagnosis ;
    public float Default_RPM ;
    //#else
    public String PartItemData ;
    //#endif
    //  
   
    
    public boolean IsNoNeedHint(){
    	return Hint_Status==2?true:false;
    }
    public boolean CheckIsNeedHint(){
    	return Hint_Status==1?true:false;
    }
   public  PartItemJson(){
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
