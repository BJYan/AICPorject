package com.aic.aicdetactor.dataBean;

import org.json.JSONException;
import org.json.JSONObject;

public class T_Organization {

	private String CorporationCode;
	private String CorporationName;
	private String CorporationNameGuid;
	private String CorporationParentLevel;
	private String CorporationParentGuid;
	private String GroupCode;
	private String GroupName;
	private String GroupNameGuid;
	private String GroupParentLevel;
	private String GroupParentGuid;
	private String WorkShopCode;
	private String WorkShopName;
	private String WorkShopNameGuid;
	private String WorkShopParentLevel;
	private String WorkShopParentGuid;
	
	public T_Organization() {
		// TODO Auto-generated constructor stub
	}
	
	public void Init_T_Organization(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		try {
			
			if(jsonObject.has("CorporationCode")) {
				CorporationCode = jsonObject.getString("CorporationCode");
			}
			if(jsonObject.has("CorporationName")) {
				CorporationName = jsonObject.getString("CorporationName");
			}
			if(jsonObject.has("CorporationNameGuid")) {
				CorporationNameGuid = jsonObject.getString("CorporationNameGuid");
			}
			if(jsonObject.has("CorporationParentLevel")) {
				CorporationParentLevel = jsonObject.getString("CorporationParentLevel");
			}
			if(jsonObject.has("CorporationParentGuid")) {
				CorporationParentGuid = jsonObject.getString("CorporationParentGuid");
			}
			if(jsonObject.has("GroupCode")) {
				GroupCode = jsonObject.getString("GroupCode");
			}
			if(jsonObject.has("GroupName")) {
				GroupName = jsonObject.getString("GroupName");
			}
			if(jsonObject.has("GroupNameGuid")) {
				GroupNameGuid = jsonObject.getString("GroupNameGuid");
			}
			if(jsonObject.has("GroupParentLevel")) {
				GroupParentLevel = jsonObject.getString("GroupParentLevel");
			}
			if(jsonObject.has("GroupParentGuid")) {
				GroupParentGuid = jsonObject.getString("GroupParentGuid");
			}
			if(jsonObject.has("WorkShopCode")) {
				WorkShopCode = jsonObject.getString("WorkShopCode");
			}
			if(jsonObject.has("WorkShopName")) {
				WorkShopName = jsonObject.getString("WorkShopName");
			}
			if(jsonObject.has("WorkShopNameGuid")) {
				WorkShopNameGuid = jsonObject.getString("WorkShopNameGuid");
			}
			if(jsonObject.has("WorkShopParentLevel")) {
				WorkShopParentLevel = jsonObject.getString("WorkShopParentLevel");
			}
			if(jsonObject.has("WorkShopParentGuid")) {
				WorkShopParentGuid = jsonObject.getString("WorkShopParentGuid");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
