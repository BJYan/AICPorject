package com.aic.aicdetactor.dataBean;

import org.json.JSONException;
import org.json.JSONObject;

public class Periods {

	private String Is_Omission_Check;
	private String Is_Permission_Timeout;
	private String Span;
	private String Start_Point;
	private String T_Turn_Number_Array;
	private String Task_Mode;
	private String Turn_Finish_Mode;
	
	public Periods() {
		// TODO Auto-generated constructor stub
	}
	
	public void InitPeriods(JSONObject jsonObject) {
		try {
			
			if(jsonObject.has("Is_Omission_Check")) {
				Is_Omission_Check = jsonObject.getString("Is_Omission_Check");
			}
			if(jsonObject.has("Is_Permission_Timeout")) {
				Is_Permission_Timeout = jsonObject.getString("Is_Permission_Timeout");
			}
			if(jsonObject.has("Span")) {
				Span = jsonObject.getString("Span");
			}
			if(jsonObject.has("Start_Point")) {
				Start_Point = jsonObject.getString("Start_Point");
			}
			if(jsonObject.has("T_Turn_Number_Array")) {
				T_Turn_Number_Array = jsonObject.getString("T_Turn_Number_Array");
			}
			if(jsonObject.has("Task_Mode")) {
				Task_Mode = jsonObject.getString("Task_Mode");
			}
			if(jsonObject.has("Turn_Finish_Mode")) {
				Turn_Finish_Mode = jsonObject.getString("Turn_Finish_Mode");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
