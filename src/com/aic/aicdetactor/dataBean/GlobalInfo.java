package com.aic.aicdetactor.dataBean;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalInfo {

	private String Check_Date;
	private String Guid;
	private String End_Time;
	private String Start_Time;
	private String T_Period_Name;
	private String Turn_Number;
	private String Task_Mode;
	
	public GlobalInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public void InitGlobalInfo(JSONObject jsonObject) {
		
			try {
				
				if(jsonObject.has("Check_Date")) {
					Check_Date = jsonObject.getString("Check_Date");
				}
				if(jsonObject.has("Guid")) {
					Guid = jsonObject.getString("Guid");
				}
				if(jsonObject.has("End_Time")) {
					End_Time = jsonObject.getString("End_Time");
				}
				if(jsonObject.has("Start_Time")) {
					Start_Time = jsonObject.getString("Start_Time");
				}
				if(jsonObject.has("T_Period_Name")) {
					T_Period_Name = jsonObject.getString("T_Period_Name");
				}
				if(jsonObject.has("Turn_Number")) {
					Turn_Number = jsonObject.getString("Turn_Number");
				}
				if(jsonObject.has("Task_Mode")) {
					Task_Mode = jsonObject.getString("Task_Mode");
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
