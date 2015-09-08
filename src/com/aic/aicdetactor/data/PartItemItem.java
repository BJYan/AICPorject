package com.aic.aicdetactor.data;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class PartItemItem implements Parcelable{
	String Axle_Number = null;
	String Check_Content = null;
	String Check_Mode = null;
	float Down_Limit = 0;
	int Emissivity = 0;
	String Extra_Information = null;
	String Fast_Record_Item_Name = null;
	String Hint_Status = null;
	String Middle_Limit = null;
	String Start_Stop_Flag = null;
	String T_Maintenance_Status_Id = null;
	String T_Measure_Type_Code = null;
	String T_Measure_Type_Id = null;
	String Unit = null;
	float Up_Limit = 0;
	
	public String getAxleNumber(){
		return Axle_Number;
	}
	public String getCheckContent(){
		return Check_Content;
	}
	public String getCheckMode(){
		return Check_Mode;
	}
	public float getDown_Limit(){
		return Down_Limit;
	}
	public int getEmissivity(){
		return Emissivity;
	}
	public String getExtra_Information(){
		return Extra_Information;
	}
	
	public void setExtra_Information(String info){
		 Extra_Information=info;
	}
	
	public String getFast_Record_Item_Name(){
		return Fast_Record_Item_Name;
	}
	public String getHint_Status(){
		return Hint_Status;
	}
	public String getMiddle_Limit(){
		return Middle_Limit;
	}
	public String getStart_Stop_Flag(){
		return Start_Stop_Flag;
	}
	public void setStart_Stop_Flag(String flagStr){
		Start_Stop_Flag=flagStr;
	}
	public String getT_Maintenance_Status_Id(){
		return T_Maintenance_Status_Id;
	}
	public String getT_Measure_Type_Code(){
		return T_Measure_Type_Code;
	}
	public String getT_Measure_Type_Id(){
		return T_Measure_Type_Id;
	}
	public String getUnit(){
		return Unit;
	}
	public float getUp_Limit(){
		return Up_Limit;
	}
	
	public JSONObject toJson(){
		JSONObject object= new JSONObject();
		return object;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString(){
		String s="{"
				+"Axle_Number:"+Axle_Number+","
				+"Check_Content:"+Check_Content+","
				+"Start_Stop_Flag:"+Start_Stop_Flag+","
				+"T_Measure_Type_Code:"+T_Measure_Type_Code+","
				+"Up_Limit:"+Up_Limit+","
	+"}";
		return s;
	}
}
