package com.aic.aicdetactor.dataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class T_Line {

	private String Name;
	private String T_Line_Content_Guid;
	private String T_Line_Guid;
	
	public T_Line() {
		// TODO Auto-generated constructor stub
	}
	
	public void Init_T_Line(JSONObject jsonObject) {
		try {
			
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
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
