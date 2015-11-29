package com.aic.aicdetactor.dataBean;

import org.json.JSONException;
import org.json.JSONObject;

public class T_Worker {

	private String Number;
	private String Name;
	private String Alias_Name;
	private String Class_Group;
	private String Password;
	private String Guid;
	private String T_Line_Guid;
	private String T_Organization_Guid;
	
	public T_Worker() {
		// TODO Auto-generated constructor stub
	}
	
	public void Init_T_Worker(JSONObject jsonObject){
		try {
			
			if(jsonObject.has("Number")) {
				Number = jsonObject.getString("Number");
			}
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
			}
			if(jsonObject.has("Alias_Name")) {
				Alias_Name = jsonObject.getString("Alias_Name");
			}
			if(jsonObject.has("Class_Group")) {
				Class_Group = jsonObject.getString("Class_Group");
			}
			if(jsonObject.has("Password")) {
				Password = jsonObject.getString("Password");
			}
			if(jsonObject.has("Guid")) {
				Guid = jsonObject.getString("Guid");
			}
			if(jsonObject.has("T_Line_Guid")) {
				T_Line_Guid = jsonObject.getString("T_Line_Guid");
			}
			if(jsonObject.has("T_Organization_Guid")) {
				T_Organization_Guid = jsonObject.getString("T_Organization_Guid");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
