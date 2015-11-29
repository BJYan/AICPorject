package com.aic.aicdetactor.dataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class T_Measure_Type {

	private String Code;
	private String Id;
	private String Name;
	
	public T_Measure_Type() {
		// TODO Auto-generated constructor stub
	}
	
	public void Init_T_Measure_Type(JSONObject jsonObject) {
		try {
			
			if(jsonObject.has("Code")) {
				Code = jsonObject.getString("Code");
			}
			if(jsonObject.has("Id")) {
				Id = jsonObject.getString("Id");
			}
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
