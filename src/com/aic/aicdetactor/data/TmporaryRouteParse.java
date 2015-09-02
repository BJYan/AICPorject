package com.aic.aicdetactor.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.aic.aicdetactor.util.SystemUtil;

public class TmporaryRouteParse {
	private final String TAG="luotest";
	public List<T_Temporary_Line>  parseTemporaryRouteData(String path){
		List<T_Temporary_Line> list = new ArrayList<T_Temporary_Line>();
		
		String data = SystemUtil.openFile(path);	
		
		if (data != null) {
			Log.d(TAG, "parseTemporaryRouteData() data is not null");
			try {
				JSONTokener jsonTokener = new JSONTokener(data);		
				JSONObject object = (JSONObject) jsonTokener.nextValue();
				//Temporary_RouteBean				
				JSONArray array= (JSONArray) object.getJSONArray(Temporary_RouteBean.RootNodeName);				
				for(int i=0;i<array.length();i++){
					T_Temporary_Line temp_Line = new T_Temporary_Line();
					JSONObject sub_object = (JSONObject)array.get(i);
					
					temp_Line.Content = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.CorporationName = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Create_Time = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Data_Exist_Guid = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.DevName = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.DevSN = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Diagnose_Conclusion = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.DownLimit = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Execution_Time = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Feedback_Time = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Finish_Time = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.GroupName = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Is_Original_Line = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Is_Readed = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Middle_Limit = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Receive_Time = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.RecordLab = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Remarks = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Result = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.RPM = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.SampleFre = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.SamplePoint = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.SaveLab = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.SensorType = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.SignalType = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.T_Item_Abnormal_Grade_Code = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.T_Item_Abnormal_Grade_Id = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.T_Measure_Type_Code = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.T_Measure_Type_Id = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.T_Temporary_Line_Guid = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);					
					temp_Line.T_Worker_R_Mumber = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.T_Worker_R_Name = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Task_Mode = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Title = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.Unit = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.UpLimit = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
					temp_Line.VMSDir = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);					
					temp_Line.WorkShopName = sub_object.optString(Temporary_RouteBean.temp_arrary_Const.Key_Content);
				
					list.add(temp_Line);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return list;
		}
		Log.d(TAG, "parseTemporaryRouteData() data is null");
		return null;

	}	
}
