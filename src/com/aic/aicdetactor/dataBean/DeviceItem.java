package com.aic.aicdetactor.dataBean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceItem {

	private String Asset_Number;
	private String Asset_Type;
	private String Capacity;
	private String Chart_Number;
	private String Class;
	private String Classification_Number;
	private String Code;
	private String Data_Exist_Guid;
	private String Date_Of_Entering;
	private String Date_Of_Production;
	private String Date_Of_Start;
	private String End_Check_Datetime;
	private String Energy_Consumption;
	private String English_Name;
	private String First_Maintenance;
	private String Grade;
	private String Installation_Site;
	private String Is_Device_Checked;
	private String Is_In_Place;
	private String Is_Omission_Check;
	private String Is_RFID_Checked;
	private String Is_Special_Inspection;
	private String Item_Define;
	private String Inspection_Type;
	private String Status_Array;
	private String Guid;
	private String Manufacturer;
	private String Material;
	private String Model;
	private String Name;
	private String Person_In_Charge;
	private String Precision;
	private String Price;
	private String Processing_Size;
	private String Rated_Power;
	private String Rated_RPM;
	private String Rated_Voltage;
	private String Rated_Current;
	private String Remarks;
	private String Safety_Coefficient;
	private String Second_Maintenance;
	private String Serial_Number;
	private String Start_Check_Datetime;
	private String Status;
	private String Third_Maintenance;
	private String Total_Check_Time;
	private String Vendor;
	private String T_Worker_Class_Group;
	private String T_Worker_Class_Shift;
	private String T_Worker_Number;
	private String T_Worker_Name;
	private String T_Worker_Guid;
	private List<PartItem> PartItemList;
	
	public DeviceItem() {
		// TODO Auto-generated constructor stub
		PartItemList = new ArrayList<PartItem>();
	}
	
	public void InitDeviceItem(JSONObject jsonObject) {
		try {
			
			if(jsonObject.has("Asset_Number")) {
				Asset_Number = jsonObject.getString("Asset_Number");
			}
			if(jsonObject.has("Asset_Type")) {
				Asset_Type = jsonObject.getString("Asset_Type");
			}
			if(jsonObject.has("Capacity")) {
				Capacity = jsonObject.getString("Capacity");
			}
			if(jsonObject.has("Chart_Number")) {
				Chart_Number = jsonObject.getString("Chart_Number");
			}
			if(jsonObject.has("Class")) {
				Class = jsonObject.getString("Class");
			}
			if(jsonObject.has("Classification_Number")) {
				Classification_Number = jsonObject.getString("Classification_Number");
			}
			if(jsonObject.has("Code")) {
				Code = jsonObject.getString("Code");
			}
			if(jsonObject.has("Data_Exist_Guid")) {
				Data_Exist_Guid = jsonObject.getString("Data_Exist_Guid");
			}
			if(jsonObject.has("Date_Of_Entering")) {
				Date_Of_Entering = jsonObject.getString("Date_Of_Entering");
			}
			if(jsonObject.has("Date_Of_Production")) {
				Date_Of_Production = jsonObject.getString("Date_Of_Production");
			}
			if(jsonObject.has("Date_Of_Start")) {
				Date_Of_Start = jsonObject.getString("Date_Of_Start");
			}
			if(jsonObject.has("End_Check_Datetime")) {
				End_Check_Datetime = jsonObject.getString("End_Check_Datetime");
			}
			if(jsonObject.has("Energy_Consumption")) {
				Energy_Consumption = jsonObject.getString("Energy_Consumption");
			}
			if(jsonObject.has("English_Name")) {
				English_Name = jsonObject.getString("English_Name");
			}
			if(jsonObject.has("First_Maintenance")) {
				First_Maintenance = jsonObject.getString("First_Maintenance");
			}
			if(jsonObject.has("Grade")) {
				Grade = jsonObject.getString("Grade");
			}
			if(jsonObject.has("Installation_Site")) {
				Installation_Site = jsonObject.getString("Installation_Site");
			}
			if(jsonObject.has("Is_Device_Checked")) {
				Is_Device_Checked = jsonObject.getString("Is_Device_Checked");
			}
			if(jsonObject.has("Is_In_Place")) {
				Is_In_Place = jsonObject.getString("Is_In_Place");
			}
			if(jsonObject.has("Is_Omission_Check")) {
				Is_Omission_Check = jsonObject.getString("Is_Omission_Check");
			}
			if(jsonObject.has("Is_RFID_Checked")) {
				Is_RFID_Checked = jsonObject.getString("Is_RFID_Checked");
			}
			if(jsonObject.has("Is_Special_Inspection")) {
				Is_Special_Inspection = jsonObject.getString("Is_Special_Inspection");
			}
			if(jsonObject.has("Item_Define")) {
				Item_Define = jsonObject.getString("Item_Define");
			}
			if(jsonObject.has("Inspection_Type")) {
				Inspection_Type = jsonObject.getString("Inspection_Type");
			}
			if(jsonObject.has("Status_Array")) {
				Status_Array = jsonObject.getString("Status_Array");
			}
			if(jsonObject.has("Guid")) {
				Guid = jsonObject.getString("Guid");
			}
			if(jsonObject.has("Manufacturer")) {
				Manufacturer = jsonObject.getString("Manufacturer");
			}
			if(jsonObject.has("Material")) {
				Material = jsonObject.getString("Material");
			}
			if(jsonObject.has("Model")) {
				Model = jsonObject.getString("Model");
			}
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
			}
			if(jsonObject.has("Person_In_Charge")) {
				Person_In_Charge = jsonObject.getString("Person_In_Charge");
			}
			if(jsonObject.has("Precision")) {
				Precision = jsonObject.getString("Precision");
			}
			if(jsonObject.has("Price")) {
				Price = jsonObject.getString("Price");
			}
			if(jsonObject.has("Processing_Size")) {
				Processing_Size = jsonObject.getString("Processing_Size");
			}
			if(jsonObject.has("Rated_Power")) {
				Rated_Power = jsonObject.getString("Rated_Power");
			}
			if(jsonObject.has("Rated_RPM")) {
				Rated_RPM = jsonObject.getString("Rated_RPM");
			}
			if(jsonObject.has("Rated_Voltage")) {
				Rated_Voltage = jsonObject.getString("Rated_Voltage");
			}
			if(jsonObject.has("Rated_Current")) {
				Rated_Current = jsonObject.getString("Rated_Current");
			}
			if(jsonObject.has("Remarks")) {
				Remarks = jsonObject.getString("Remarks");
			}
			if(jsonObject.has("Safety_Coefficient")) {
				Safety_Coefficient = jsonObject.getString("Safety_Coefficient");
			}
			if(jsonObject.has("Second_Maintenance")) {
				Second_Maintenance = jsonObject.getString("Second_Maintenance");
			}
			if(jsonObject.has("Serial_Number")) {
				Serial_Number = jsonObject.getString("Serial_Number");
			}
			if(jsonObject.has("Start_Check_Datetime")) {
				Start_Check_Datetime = jsonObject.getString("Start_Check_Datetime");
			}
			if(jsonObject.has("Status")) {
				Status = jsonObject.getString("Status");
			}
			if(jsonObject.has("Third_Maintenance")) {
				Third_Maintenance = jsonObject.getString("Third_Maintenance");
			}
			if(jsonObject.has("Total_Check_Time")) {
				Total_Check_Time = jsonObject.getString("Total_Check_Time");
			}
			if(jsonObject.has("Vendor")) {
				Vendor = jsonObject.getString("Vendor");
			}
			if(jsonObject.has("T_Worker_Class_Group")) {
				T_Worker_Class_Group = jsonObject.getString("T_Worker_Class_Group");
			}
			if(jsonObject.has("T_Worker_Class_Shift")) {
				T_Worker_Class_Shift = jsonObject.getString("T_Worker_Class_Shift");
			}
			if(jsonObject.has("T_Worker_Number")) {
				T_Worker_Number = jsonObject.getString("T_Worker_Number");
			}
			if(jsonObject.has("T_Worker_Name")) {
				T_Worker_Name = jsonObject.getString("T_Worker_Name");
			}
			if(jsonObject.has("T_Worker_Guid")) {
				T_Worker_Guid = jsonObject.getString("T_Worker_Guid");
			}
			if(jsonObject.has("PartItem")) {
				JSONArray jsonArray = jsonObject.getJSONArray("PartItem");
				for(int i=0;i<jsonArray.length();i++){
					PartItem partItem = new PartItem();
					partItem.InitPartItem(jsonArray.getJSONObject(i));
					PartItemList.add(partItem);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
