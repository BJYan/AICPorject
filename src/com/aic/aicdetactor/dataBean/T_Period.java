package com.aic.aicdetactor.dataBean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class T_Period {

	private String Base_Point;
	private String Frequency;
	private String Name;
	private String Status_Array;
	private String T_Line_Content_Guid;
	private String T_Line_Guid;
	private String T_Period_Unit_Code;
	private String T_Period_Unit_Id;
	private List<Periods> PeriodsList;
	
	public T_Period() {
		// TODO Auto-generated constructor stub
		PeriodsList = new ArrayList<Periods>();
	}
	
	public void Init_T_Period(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		try {
			
			if(jsonObject.has("Base_Point")) {
				Base_Point = jsonObject.getString("Base_Point");
			}
			if(jsonObject.has("Frequency")) {
				Frequency = jsonObject.getString("Frequency");
			}
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
			}
			if(jsonObject.has("Status_Array")) {
				Status_Array = jsonObject.getString("Status_Array");
			}
			if(jsonObject.has("T_Line_Content_Guid")) {
				T_Line_Content_Guid = jsonObject.getString("T_Line_Content_Guid");
			}
			if(jsonObject.has("T_Line_Guid")) {
				T_Line_Guid = jsonObject.getString("T_Line_Guid");
			}
			if(jsonObject.has("T_Period_Unit_Code")) {
				T_Period_Unit_Code = jsonObject.getString("T_Period_Unit_Code");
			}
			if(jsonObject.has("T_Period_Unit_Id")) {
				T_Period_Unit_Id = jsonObject.getString("T_Period_Unit_Id");
			}
		    if(jsonObject.has("Periods")) {
		    	JSONArray jsonArray = jsonObject.getJSONArray("Periods");
		    	for(int i=0;i<jsonArray.length();i++){
		    		Periods periods = new Periods();
		    		periods.InitPeriods(jsonArray.getJSONObject(i));
		    		PeriodsList.add(periods);
		    	}
		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
