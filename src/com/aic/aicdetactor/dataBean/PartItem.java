package com.aic.aicdetactor.dataBean;

import org.json.JSONException;
import org.json.JSONObject;

public class PartItem {

	private String Check_Content;
	private String T_Measure_Type_Id;
	private String T_Measure_Type_Code;
	private String Unit;
	private String Start_Stop_Flag;
	private String Up_Limit;
	private String Middle_Limit;
	private String Down_Limit;
	private String Emissivity;
	private String Hint_Status;
	private String Axle_Number;
	private String Check_Mode;
	private String Extra_Information;
	private String T_Maintenance_Status_Id;
	private String Fault_Info;
	private String Default_RPM;
	private String Is_Normal;
	private String Item_Define;
	private String Fault_Diagnosis;
	private String Start_Check_Datetime;
	private String End_Check_Datetime;
	private String Total_Check_Time;
	private String SaveLab;
	private String RecordLab;
	private String SensorType;
	private String VMSDir;
	private String SignalType;
	private String SampleFre;
	private String SamplePoint;
	private String RPM;
	private String Diagnose_Conclusion;
	private String Remarks;
	private String Is_Timeout;
	private String T_Item_Abnormal_Grade_Id;
	private String T_Item_Abnormal_Grade_Code;
	private String PartItemData;
	
	public PartItem() {
		// TODO Auto-generated constructor stub
	}
	
	public void InitPartItem(JSONObject jsonObject) {
		try {
			
			if(jsonObject.has("Check_Content")) {
				Check_Content = jsonObject.getString("Check_Content");
			}
			if(jsonObject.has("T_Measure_Type_Id")) {
				T_Measure_Type_Id = jsonObject.getString("T_Measure_Type_Id");
			}
			if(jsonObject.has("T_Measure_Type_Code")) {
				T_Measure_Type_Code = jsonObject.getString("T_Measure_Type_Code");
			}
			if(jsonObject.has("Unit")) {
				Unit = jsonObject.getString("Unit");
			}
			if(jsonObject.has("Start_Stop_Flag")) {
				Start_Stop_Flag = jsonObject.getString("Start_Stop_Flag");
			}
			if(jsonObject.has("Up_Limit")) {
				Up_Limit = jsonObject.getString("Up_Limit");
			}
			if(jsonObject.has("Middle_Limit")) {
				Middle_Limit = jsonObject.getString("Middle_Limit");
			}
			if(jsonObject.has("Down_Limit")) {
				Down_Limit = jsonObject.getString("Down_Limit");
			}
			if(jsonObject.has("Emissivity")) {
				Emissivity = jsonObject.getString("Emissivity");
			}
			if(jsonObject.has("Hint_Status")) {
				Hint_Status = jsonObject.getString("Hint_Status");
			}
			if(jsonObject.has("Axle_Number")) {
				Axle_Number = jsonObject.getString("Axle_Number");
			}
			if(jsonObject.has("Check_Mode")) {
				Check_Mode = jsonObject.getString("Check_Mode");
			}
			if(jsonObject.has("Extra_Information")) {
				Extra_Information = jsonObject.getString("Extra_Information");
			}
			if(jsonObject.has("T_Maintenance_Status_Id")) {
				T_Maintenance_Status_Id = jsonObject.getString("T_Maintenance_Status_Id");
			}
			if(jsonObject.has("Fault_Info")) {
				Fault_Info = jsonObject.getString("Fault_Info");
			}
			if(jsonObject.has("Default_RPM")) {
				Default_RPM = jsonObject.getString("Default_RPM");
			}
			if(jsonObject.has("Is_Normal")) {
				Is_Normal = jsonObject.getString("Is_Normal");
			}
			if(jsonObject.has("Item_Define")) {
				Item_Define = jsonObject.getString("Item_Define");
			}
			if(jsonObject.has("Fault_Diagnosis")) {
				Fault_Diagnosis = jsonObject.getString("Fault_Diagnosis");
			}
			if(jsonObject.has("Start_Check_Datetime")) {
				Start_Check_Datetime = jsonObject.getString("Start_Check_Datetime");
			}
			if(jsonObject.has("End_Check_Datetime")) {
				End_Check_Datetime = jsonObject.getString("End_Check_Datetime");
			}
			if(jsonObject.has("Total_Check_Time")) {
				Total_Check_Time = jsonObject.getString("Total_Check_Time");
			}
			if(jsonObject.has("SaveLab")) {
				SaveLab = jsonObject.getString("SaveLab");
			}
			if(jsonObject.has("RecordLab")) {
				RecordLab = jsonObject.getString("RecordLab");
			}
			if(jsonObject.has("SensorType")) {
				SensorType = jsonObject.getString("SensorType");
			}
			if(jsonObject.has("VMSDir")) {
				VMSDir = jsonObject.getString("VMSDir");
			}
			if(jsonObject.has("SignalType")) {
				SignalType = jsonObject.getString("SignalType");
			}
			if(jsonObject.has("SampleFre")) {
				SampleFre = jsonObject.getString("SampleFre");
			}
			if(jsonObject.has("SamplePoint")) {
				SamplePoint = jsonObject.getString("SamplePoint");
			}
			if(jsonObject.has("RPM")) {
				RPM = jsonObject.getString("RPM");
			}
			if(jsonObject.has("Diagnose_Conclusion")) {
				Diagnose_Conclusion = jsonObject.getString("Diagnose_Conclusion");
			}
			if(jsonObject.has("Remarks")) {
				Remarks = jsonObject.getString("Remarks");
			}
			if(jsonObject.has("Is_Timeout")) {
				Is_Timeout = jsonObject.getString("Is_Timeout");
			}
			if(jsonObject.has("T_Item_Abnormal_Grade_Id")) {
				T_Item_Abnormal_Grade_Id = jsonObject.getString("T_Item_Abnormal_Grade_Id");
			}
			if(jsonObject.has("T_Item_Abnormal_Grade_Code")) {
				T_Item_Abnormal_Grade_Code = jsonObject.getString("T_Item_Abnormal_Grade_Code");
			}
			if(jsonObject.has("PartItemData")) {
				PartItemData = jsonObject.getString("PartItemData");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
