package com.aic.aicdetactor.dataBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Handler;
import android.util.Log;

public class JsonData {
	public static final int INIT_JSON_DATA_FINISHED = 200;
	public static final String TAG = "JsonData";

	private GlobalInfo globalInfo;
	private List<StationInfo> stationInfoList;
	private List<T_Item_Abnormal_Grade> T_Item_Abnormal_Grade_list;
	private T_Line T_line;
	private List<T_Measure_Type> T_Measure_Type_list;
	private T_Organization T_organization;
	private T_Period T_period;
	private List<T_Turn> T_Turn_list;
	private List<T_Worker> T_Worker_list;
	
	Handler handler;
	
	public JsonData() {
		// TODO Auto-generated constructor stub
		globalInfo = new GlobalInfo();
		stationInfoList = new ArrayList<StationInfo>();
		T_Item_Abnormal_Grade_list = new ArrayList<T_Item_Abnormal_Grade>();
		T_line = new T_Line();
		T_Measure_Type_list = new ArrayList<T_Measure_Type>();
		T_organization = new T_Organization();
		T_period = new T_Period();
		T_Turn_list = new ArrayList<T_Turn>();
		T_Worker_list = new ArrayList<T_Worker>();
	}
	
	public void InitJsonData(String path, Handler handler) {
		this.handler = handler;
		InitJsonDataThread InitThread = new InitJsonDataThread(path);
		InitThread.start();
	}
	
	class InitJsonDataThread extends Thread{
		
		String path;
		
		public InitJsonDataThread(String path) {
			// TODO Auto-generated constructor stub
			this.path = path;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			//读取json文件
			String sb = ""; 
			String jsonString = ""; 
			try {  
			    File file = new File(path); 
			    if(!file.exists()) {
			    	Log.e(TAG, "JSON text path error!!!");
			    	return;
			    }
			    BufferedReader br = new BufferedReader(new FileReader(file));  
			     
			     
			    while ((sb = br.readLine()) != null) {
			    	jsonString = jsonString + sb;
			    }  
			    br.close(); 
 
			} catch (Exception e) {  
			    e.printStackTrace();
			}

			//解析json文件
			try {  
			    JSONTokener jsonParser = new JSONTokener(jsonString);  
			    // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。  
			    // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）  
			    JSONObject jsonItem = (JSONObject) jsonParser.nextValue();  
			    // 接下来的就是JSON对象的操作了  
			    if(jsonItem.has("GlobalInfo")) {
			    	globalInfo.InitGlobalInfo(jsonItem.getJSONObject("GlobalInfo"));
			    }
			    if(jsonItem.has("StationInfo")) {
			    	JSONArray jsonArray = jsonItem.getJSONArray("StationInfo");
			    	for(int i=0;i<jsonArray.length();i++){
			    		StationInfo stationInfo = new StationInfo();
			    		stationInfo.InitStationInfo(jsonArray.getJSONObject(i));
			    		stationInfoList.add(stationInfo);
			    	}
			    }
			    if(jsonItem.has("T_Item_Abnormal_Grade")) {
			    	JSONArray jsonArray = jsonItem.getJSONArray("T_Item_Abnormal_Grade");
			    	for(int i=0;i<jsonArray.length();i++){
			    		T_Item_Abnormal_Grade T_Item_Abnormal_grade = new T_Item_Abnormal_Grade();
			    		T_Item_Abnormal_grade.Init_T_Item_Abnormal_Grade(jsonArray.getJSONObject(i));
			    		T_Item_Abnormal_Grade_list.add(T_Item_Abnormal_grade);
			    	}
			    }
			    if(jsonItem.has("T_Line")) {
			    	T_line.Init_T_Line(jsonItem.getJSONObject("T_Line"));
			    }
			    if(jsonItem.has("T_Measure_Type")) {
			    	JSONArray jsonArray = jsonItem.getJSONArray("T_Measure_Type");
			    	for(int i=0;i<jsonArray.length();i++){
			    		T_Measure_Type T_Measure_type = new T_Measure_Type();
			    		T_Measure_type.Init_T_Measure_Type(jsonArray.getJSONObject(i));
			    		T_Measure_Type_list.add(T_Measure_type);
			    	}
			    }
			    if(jsonItem.has("T_Organization")) {
			    	T_organization.Init_T_Organization(jsonItem.getJSONObject("T_Organization"));
			    }
			    if(jsonItem.has("T_Period")) {
			    	T_period.Init_T_Period(jsonItem.getJSONObject("T_Period"));
			    }
			    if(jsonItem.has("T_Turn")) {
			    	JSONArray jsonArray = jsonItem.getJSONArray("T_Turn");
			    	for(int i=0;i<jsonArray.length();i++){
			    		T_Turn T_turn = new T_Turn();
			    		T_turn.Init_T_Turn(jsonArray.getJSONObject(i));
			    		T_Turn_list.add(T_turn);
			    	}
			    }
			    if(jsonItem.has("T_Worker")) {
			    	JSONArray jsonArray = jsonItem.getJSONArray("T_Worker");
			    	for(int i=0;i<jsonArray.length();i++){
			    		T_Worker T_worker = new T_Worker();
			    		
			    		T_Worker_list.add(T_worker);
			    	}
			    }
			} catch (JSONException ex) {  
			    // 异常处理代码  
			} 
			handler.sendEmptyMessage(INIT_JSON_DATA_FINISHED);
		}
	}
}
