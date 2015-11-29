package com.aic.aicdetactor.dataBean;

import org.json.JSONException;
import org.json.JSONObject;

public class T_Turn {

	private String End_Time;
	private String Name;
	private String Start_Time;
	private String Number;
	private String T_Line_Content_Guid;
	private String T_Line_Guid;
	
	public T_Turn() {
		// TODO Auto-generated constructor stub
	}
	
	public void Init_T_Turn(JSONObject jsonObject) {
		try {
			
			if(jsonObject.has("End_Time")) {
				End_Time = jsonObject.getString("End_Time");
			}
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
			}
			if(jsonObject.has("Start_Time")) {
				Start_Time = jsonObject.getString("Start_Time");
			}
			if(jsonObject.has("Number")) {
				Number = jsonObject.getString("Number");
			}
			if(jsonObject.has("T_Line_Content_Guid")) {
				T_Line_Content_Guid = jsonObject.getString("T_Line_Content_Guid");
			}
			if(jsonObject.has("T_Line_Guid")) {
				T_Line_Guid = jsonObject.getString("T_Line_Guid");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
