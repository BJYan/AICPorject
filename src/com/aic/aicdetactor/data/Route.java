package com.aic.aicdetactor.data;

import org.json.JSONException;

public class Route {
	//正常巡检
	public final int Normal_Route=1;
	//临时巡检
	public final int Temp_Route=2;
	//是否临时巡检
	public boolean isTempRoute(){
		return false;
	}
	public void setPath(String path){}
	//获取轮次、工人及组织信息
	public void parseBaseInfo(){};
	//保存巡检结果
	public void SaveData(String fileName) throws JSONException{}
	
	public void ParseData(Object object){}
}
