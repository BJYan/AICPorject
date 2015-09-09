package com.aic.aicdetactor.condition;

import java.util.List;

import android.content.ContentValues;

import com.aic.aicdetactor.data.RoutePeroid;
import com.aic.aicdetactor.data.T_Period;
import com.aic.aicdetactor.data.TurnInfo;

public class ConditionalJudgement {
	/**
	 * bool GetUploadJsonFile(JSON周期数组,轮次数组， 当前APP时间, T_Line_Guid，m_CurPeriod，m_FileName);// 
 参数说明：
（1）	JSON周期数组来自表T_Line_Original_Json
（2）	当前APP时间—调用函数那一刻APP时间
（3）	T_Line_Guid来自T_Line_Original_Json
（4）	m_CurPeriod 机构数组，返回参数、
（5）	m_FileName –巡检文件名，返回参数

	 * @return
	 */
public static boolean GetUploadJsonFile(List<T_Period>tp,List<TurnInfo>t,
		String systeTime,RoutePeroid m_CurPeriod,String m_FileName){
	return true;
}

/**
 *   BOOL Is_Timeout((in)JSON周期数组,(in)轮次数组，(in)当前APP时间，(out)Is_permission_Timeout);
        当函数返回值=true,表示没有超时，可继续检，同时设置：
         PartItemData的第33项为”0”; 

当函数返回值=false且Is_permission_Timeout=true, 可继续检，同时设置：
         PartItemData的第33项为”1”.

       当函数返回值=false且Is_permission_Timeout=false,提示超时，退出巡检。

 */
public static boolean Is_Timeout(List<T_Period>tp,List<TurnInfo>t,
		String systeTime,ContentValues Is_permission_Timeout){
	//ContentValues 用法，外面只需通过	a.getAsBoolean("IsTimeOut"); 即可获取结果
	ContentValues a= new ContentValues();
	a.put("IsTimeOut", true);
	return true;
	
}
}
