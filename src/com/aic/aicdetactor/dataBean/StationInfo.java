package com.aic.aicdetactor.dataBean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StationInfo {

	private String Code;
	private String Factory_Code;
	private String Is_In_Place;
	private String Name;
	private String T_Organization_Guid;
	private List<DeviceItem> DeviceItemList;
	
	public StationInfo() {
		// TODO Auto-generated constructor stub
		DeviceItemList = new ArrayList<DeviceItem>();
	}
	
	public void InitStationInfo(JSONObject jsonObject){
		try {
			
			if(jsonObject.has("Code")) {
				Code = jsonObject.getString("Code");
			}
			if(jsonObject.has("Factory_Code")) {
				Factory_Code = jsonObject.getString("Factory_Code");
			}
			if(jsonObject.has("Is_In_Place")) {
				Is_In_Place = jsonObject.getString("Is_In_Place");
			}
			if(jsonObject.has("Name")) {
				Name = jsonObject.getString("Name");
			}
			if(jsonObject.has("T_Organization_Guid")) {
				T_Organization_Guid = jsonObject.getString("T_Organization_Guid");
			}
			if(jsonObject.has("DeviceItem")) {
				JSONArray jsonArray = jsonObject.getJSONArray("DeviceItem");
				for(int i=0;i<jsonArray.length();i++){
					DeviceItem deviceItem = new DeviceItem();
					deviceItem.InitDeviceItem(jsonArray.getJSONObject(i));
					DeviceItemList.add(deviceItem);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
