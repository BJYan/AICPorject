package com.aic.aicdetactor.data;

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
