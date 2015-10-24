package com.aic.aicdetactor.condition;


import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.aic.aicdetactor.data.RoutePeroid;

import android.content.ContentValues;

import com.aic.aicdetactor.data.DownloadNormalData;
import com.aic.aicdetactor.data.LineInfoJson;
import com.aic.aicdetactor.data.PeriodJson;
import com.aic.aicdetactor.data.T_Period;
import com.aic.aicdetactor.data.TurnInfo;
import com.aic.aicdetactor.data.PeriodInfoJson;
import com.aic.aicdetactor.data.TurnInfoJson;
import com.aic.aicdetactor.data.WorkerInfoJson;

class T_Period_Code {
	
	public static  String DAY="00";
	public static  String WEEK="01";
	public static  String MONTH="02";
};
//条件判断函数
public class ConditionalJudgement {

//m_WorkerInfoJson	登录用户
//T_Line 用户选择的常规线路号
//m_PeriodInfo  用户选择的线路周期
//T_Turn  用户选择的线路轮次
//m_RoutePeroid 返回参数
//返回=true, 需要的信息从m_RoutePeroid得到，如m_RoutePeroid。Guid为空，则从原始的JSON中得到巡检信息；
//返回=false 错误信息从nInfo。err得到。
	public boolean GetUploadJsonFile( LineInfoJson T_Line,PeriodInfoJson m_PeriodInfo,List<TurnInfoJson> T_Turn,
			WorkerInfoJson m_WorkerInfoJson,RoutePeroid m_RoutePeroid,ContentValues nInfo)
	{
		boolean nRe=false;
		String StartDate,EndDate;
		int CpNum;
		// PeriodInfoJson m_PeriodInfo=mNormalLineJsonData.T_Period;
		// List<TurnInfoJson> T_Turn=mNormalLineJsonData.T_Turn;
		// LineInfoJson T_Line=mNormalLineJsonData.T_Line;
		  String m_CurTime;
		  String m_CurDate;
		  Calendar calendar;
		  calendar = Calendar.getInstance();//获得一个日历
		  m_CurTime=String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		  m_CurTime+=String.format("%02d", calendar.get(Calendar.MINUTE));
		  TurnInfoJson m_TurnInfoJson=new TurnInfoJson() ;
		  SimpleDateFormat SimpleDate= new SimpleDateFormat("yyyy-MM-dd");//设置日期格式 
		  String SimpleDateStr=SimpleDate.format(new Date());
		  GregorianCalendar gc=new GregorianCalendar(); 		  
		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		  PeriodJson m_CurPeriodJson=new PeriodJson();		  
		  
	//	  String ss;
		
		
		// gc.setTime(new Date()); 
	//	 gc.add(Calendar.DATE,81); 
		// ss=df.format(gc.getTime());
		  
		//  calendar.setTime(gc.getTime());
		//  int num=calendar.get(Calendar.DAY_OF_WEEK);//
			 gc.setTime(new Date()); 
			 m_CurDate=df.format(gc.getTime());
		   
 	try
	{

			 if(T_Line==null||m_PeriodInfo==null||m_WorkerInfoJson==null||m_RoutePeroid==null||nInfo==null)
			 {
				  nInfo.put("err", "空参数");
				  return false;		 
				 
			 }
		  
		  
		 if( m_PeriodInfo.Periods.size()==0)
		 {
			  nInfo.put("err", "没有周期信息");
			  return false;	
		 }; 

		 if(m_PeriodInfo.T_Period_Unit_Code.equals(T_Period_Code.DAY))
		 {
			 
			if(!GetCurBaseDateofDayRoute(m_PeriodInfo.Base_Point,m_PeriodInfo. Frequency, nInfo))
					return false;
				CpNum=Integer.parseInt((String)nInfo.get("CpNum"));
		 }
		 else if(m_PeriodInfo.T_Period_Unit_Code.equals(T_Period_Code.WEEK))
		 {
			 CpNum=GetCurBaseDateofWeekRoute(m_PeriodInfo.Base_Point);
			 
		 }
		else if(m_PeriodInfo.T_Period_Unit_Code.equals(T_Period_Code.MONTH))
		{
			CpNum=GetCurBaseDateofMonthRoute();				
		
		}
		else  //
		{
				
				  nInfo.put("err", "周期种类只支持：天，周，月");
				  return false;	
		}
		;
		
		for(int ii=0;ii<m_PeriodInfo.Periods.size();ii++)
		{
			
			m_CurPeriodJson=m_PeriodInfo.Periods.get(ii);
			if(m_PeriodInfo.T_Period_Unit_Code.equals(T_Period_Code.DAY)||m_PeriodInfo.T_Period_Unit_Code.equals(T_Period_Code.WEEK))
			{
			if(!IsInRangeofSpan(m_CurPeriodJson,CpNum))
				continue;
			}
			else 
			{
				
				if(!IsInRangeofSpan(m_CurPeriodJson))
					continue;
			};
			
			if(m_PeriodInfo.Periods.get(ii).Task_Mode==0)//=0每天多次\
			{
				if(T_Turn==null||T_Turn.size ()==0)
				{
					nInfo.put("err", "轮次信息为空");
				    return false;
				};
				
				if(!GetCurTurnInfo(T_Turn,m_CurTime,m_PeriodInfo.Periods.get(ii).T_Turn_Number_Array, m_TurnInfoJson))
				{
				// if(T_Turn.size ()==0)
				//	 nInfo.put("err", "轮次信息为空");
				// else
					 nInfo.put("err","巡检时间未到,现在的时间是:"+m_CurDate);
				 ;
				  return false;	
				}
		      ;				  
		      if(!GetCurTurnDate(m_TurnInfoJson,nInfo))
		    	  return false;
		    	;
		    	StartDate=(String)	nInfo.get("StartDate");
		    	EndDate=(String)nInfo.get("EndDate");
				
			}					
			else if(m_PeriodInfo.Periods.get(ii).Task_Mode==1) //=1总共一次
			{
				 gc.setTime(new Date()); 
				 
				 gc.add(Calendar.DATE,CpNum+m_PeriodInfo.Periods.get(ii).Start_Point); 
				 StartDate=SimpleDate.format(gc.getTime())+" 00:00:00";	
				 
				 gc.setTime(new Date()); 
				 gc.add(Calendar.DATE,CpNum+m_PeriodInfo.Periods.get(ii).Start_Point+m_PeriodInfo.Periods.get(ii).Span-1); 
				 EndDate=SimpleDate.format(gc.getTime())+" 23:59:59";	
			}		
			else
			{		
				nInfo.put("err", "周期格式有误，只支持一天多次及总共一次巡检");
			    return false;
			}	
			;
			if(!GetCurCheckedFileName(T_Line.T_Line_Guid,m_TurnInfoJson,m_WorkerInfoJson,m_PeriodInfo.Periods.get(ii),m_PeriodInfo.T_Period_Unit_Code,
					m_PeriodInfo.Base_Point,StartDate,	EndDate,nInfo))
			{
				 // nInfo.put("err", "发生意外错误");
				  return false;	
			}
			;
			nRe=true;
			break;			
			
		}	
	
       if(nRe)
       {
    	   m_RoutePeroid.T_Line_Name=T_Line.Name;
    	   m_RoutePeroid.T_Line_Guid=T_Line.T_Line_Guid;
    	   m_RoutePeroid.Task_Mode=m_CurPeriodJson.Task_Mode; 
    	   m_RoutePeroid.Start_Point=m_CurPeriodJson.Start_Point;
    	   m_RoutePeroid.Span=m_CurPeriodJson.Span;
    	   m_RoutePeroid.Turn_Finish_Mode=m_CurPeriodJson.Turn_Finish_Mode;
    	   m_RoutePeroid.T_Period_Unit_Code=m_PeriodInfo.T_Period_Unit_Code;
    	   m_RoutePeroid.Base_Point=  m_PeriodInfo.Base_Point;
    	   m_RoutePeroid.Worker_Name=m_WorkerInfoJson.Name;
    	   m_RoutePeroid.Worker_Number=m_WorkerInfoJson.Number;
    	   m_RoutePeroid.Class_Group = m_WorkerInfoJson.Class_Group;
    	   m_RoutePeroid.Turn_Name=m_TurnInfoJson.Name;
    	   m_RoutePeroid.Turn_Number=m_TurnInfoJson.Number;
    	   m_RoutePeroid.Start_Time=m_TurnInfoJson.Start_Time;
    	   m_RoutePeroid.End_Time=m_TurnInfoJson.End_Time;
    	   m_RoutePeroid.File_Guid=(String)nInfo.get("FileName");
    	   m_RoutePeroid.Is_Omission_Check=m_CurPeriodJson.Is_Omission_Check;
    	   m_RoutePeroid.Is_Permission_Timeout=m_CurPeriodJson.Is_Permission_Timeout;
    	   m_RoutePeroid.T_Period_Name=m_PeriodInfo.Name;  	   

       }
       else
       {
    	   nInfo.put("err","巡检时间未到,现在的时间是:"+m_CurDate);
    	   return false;    	   
       }
    } 
   	catch (Exception e)
   	{
   	 nInfo.put("err",e.getMessage());
   	 nRe=false;
   	}  ;
   	
		return nRe;
}	
	
	
private boolean GetCurCheckedFileName(String T_Line_Guid,TurnInfoJson m_TurnInfoJson,WorkerInfoJson m_WorkerInfoJson,
		PeriodJson m_PeriodJson,String T_Period_Unit_Code,String Base_Point,String Start_Date,String End_Date,ContentValues FileNameInfo)
{
	boolean nRe=false;
	String BaseSql="";
	String ExSql="";

  try{
	  

	if(m_PeriodJson.Turn_Finish_Mode==2)//个人完成一个轮次
	{
		ExSql+=" and Worker_Name='"+m_WorkerInfoJson.Name+"' and Worker_ Number='"+ m_WorkerInfoJson.Number+"' and Class_Group='"+m_WorkerInfoJson.Class_Group+"'";
		
	}
	else if(m_PeriodJson.Turn_Finish_Mode==1)//班组成员完成一个轮次
	{
		ExSql+=" and Class_Group'"+m_WorkerInfoJson.Class_Group+"'";
		
	}
	else if(m_PeriodJson.Turn_Finish_Mode==0)//全体成员完成一个轮次
	{
		ExSql+="";
	}
	else //默认个人完成一个轮次
	{
		m_PeriodJson.Turn_Finish_Mode=2;
		ExSql+=" and Worker_Name='"+m_WorkerInfoJson.Name+"' and Worker_ Number='"+ m_WorkerInfoJson.Number+"' and Class_Group='"+m_WorkerInfoJson.Class_Group+"'";
	}
		;
   if(m_PeriodJson.Task_Mode==0)//每天多次巡检
   {
	   ExSql+=" and Turn_ Number="+m_TurnInfoJson.Number+" and Turn_Name='"+m_TurnInfoJson.Name+"'";
   }
   else //总共一次
   {
	   ExSql+="";
   }
	;   
	
	BaseSql="select Guid from T_Line_Upload_Json where T_Line_Guid='"+T_Line_Guid+"' and Turn_Finish_Mode="+m_PeriodJson.Turn_Finish_Mode;
	BaseSql+=" and Task_Mode="+m_PeriodJson.Task_Mode;
	BaseSql+=ExSql;
	BaseSql+=" and Start_Point="+m_PeriodJson.Start_Point;
	BaseSql+=" and Span="+m_PeriodJson.Span;
	BaseSql+=" and T_Period_Unit_Code='"+T_Period_Unit_Code+"'";
	BaseSql+=" and Base_Point='"+Base_Point+"'";
	BaseSql+=" and Is_Special_Inspection=0";
	//BaseSql+=" and Date>='"+Start_Date+"' and Date<='"+End_Date +"'";
	BaseSql+=" and Date BETWEEN '"+Start_Date+"' AND '"+End_Date +"'";
	
	FileNameInfo.put("FileName", GetUploadJsonFile(BaseSql));
	nRe=true; 
  }
  catch(Exception e)
  {
	  FileNameInfo.put("err",e.getMessage());
	  nRe=false;  	  
  };
  
	return nRe;
}




//得到当前轮次信息
//=true,成功获取；=false 当前巡检时间没到。
//返回参数m_TurnInfoJson
private boolean	GetCurTurnInfo(List<TurnInfoJson> T_Turn,String m_CurTime,String Turn_Number_Array, TurnInfoJson m_TurnInfoJson)
{
	boolean nRe=false;
	String NumberStr;
	String Start_Time,End_Time;
	String NewTurn_Number_Array="/"+Turn_Number_Array+"/";
	if(T_Turn==null)
		return false;
	;
	if(T_Turn.size()==0)
	  return false;
	;
	for(int ii=0;ii<T_Turn.size ();ii++)
	{
		NumberStr="/"+Integer.toString(T_Turn.get(ii).Number)+"/"; 
		
		Start_Time=T_Turn.get(ii).Start_Time;
		End_Time=T_Turn.get(ii).End_Time;
		
	          		
	    if(NewTurn_Number_Array.indexOf(NumberStr)!=-1)
	    {
	    	if(Start_Time.compareTo(End_Time)<=0)
	    	{
	    		if(m_CurTime.compareTo(T_Turn.get(ii).Start_Time)>=0&&m_CurTime.compareTo(T_Turn.get(ii).End_Time)<=0)
	    		{
	    			m_TurnInfoJson.End_Time=T_Turn.get(ii).End_Time;
	    			m_TurnInfoJson.Name=T_Turn.get(ii).Name;
	    			m_TurnInfoJson.Start_Time=T_Turn.get(ii).Start_Time;
	    			m_TurnInfoJson.Number=T_Turn.get(ii).Number;
	    			nRe=true;
	    			break;
	    		}
	    	}
	    	else
	    	{
	    	   if((m_CurTime.compareTo(Start_Time)>=0&&m_CurTime.compareTo(End_Time)>=0)||(m_CurTime.compareTo(Start_Time)<=0&&m_CurTime.compareTo(End_Time)<=0))
	    	   {
	   			m_TurnInfoJson.End_Time=T_Turn.get(ii).End_Time;
				m_TurnInfoJson.Name=T_Turn.get(ii).Name;
				m_TurnInfoJson.Start_Time=T_Turn.get(ii).Start_Time;
				m_TurnInfoJson.Number=T_Turn.get(ii).Number;
				nRe=true;
				break;
	    	   }
	    	}	    	
	    	
	    }
		
	}

	return nRe;
}

//按天巡检时，	频度的第一天相对今天偏移的天数
private boolean GetCurBaseDateofDayRoute(String Base_Point,int Frequency,ContentValues nInfo)
{
	int CpNum=0;
	boolean nRe=false;
	
	String m_CurDateStr; 
	Date m_CurDate=new Date();
	Date BasePointDate=new Date();
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	 GregorianCalendar gc=new GregorianCalendar(); 
	 gc.setTime(m_CurDate); 
	 m_CurDateStr=df.format(gc.getTime());
   			 
     if(m_CurDateStr.compareTo(Base_Point)<0)
     {
    	 nInfo.put("err", "巡检日期未到");
        return false;
     }
     ;
     if(Frequency<=0)
     {    	 
    	 nInfo.put("err", "巡检频度错误："+String.valueOf(Frequency));
         return false;
     };
     
	try {
			
		BasePointDate = df.parse(Base_Point);
		long diffmills=(m_CurDate.getTime()-BasePointDate.getTime())/(24*3600*1000); 
		CpNum=Integer.parseInt(String.valueOf(diffmills));
		CpNum%=Frequency;	
		CpNum=0-CpNum;
		nInfo.put("CpNum", String.valueOf(CpNum));
		
		nRe=true;
		
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		 nInfo.put("err", e.getMessage());
		  return false;
		
	};	
  
	return nRe;
	
}

//按周巡检时，	基准点相对今天偏移的天数
private int GetCurBaseDateofWeekRoute(String Base_Point)
{
   int CpNum=0,NumofWeek=0;
   
   Calendar  calendar = Calendar.getInstance();//获得一个日历
	GregorianCalendar gc=new GregorianCalendar(); 
	 gc.setTime(new Date());  
	  calendar.setTime(gc.getTime());
	   NumofWeek=calendar.get(Calendar.DAY_OF_WEEK);//得到今天是星期几
	   if(NumofWeek>0)
		   NumofWeek-=1;
	   ;
	  int nPoint= Integer.parseInt(Base_Point) ;
	   if(nPoint>NumofWeek)
		   CpNum=nPoint-NumofWeek-7;
	   else
		   CpNum=  nPoint-NumofWeek;
	;
	return CpNum;

}


private int GetCurBaseDateofMonthRoute()
{
	  int CurNumofDay;
	   Calendar  calendar = Calendar.getInstance();//获得一个日历
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(new Date());  
		calendar.setTime(gc.getTime());
		CurNumofDay=calendar.get(Calendar.DAY_OF_MONTH);//得到今天是一个月中的第几天
		
	 return 1-CurNumofDay;
}
  
private String  GetUploadJsonFile(String Sql){
	  String FileName="";
	  //调用sqlite,表中Guid赋给FileName
	  
	 
	return FileName;
}

 //按天、周
//跨度在当前时间内。
  private boolean IsInRangeofSpan(PeriodJson m_CurPeriodJson,int CpNum)
  {
		
		if((m_CurPeriodJson.Start_Point+CpNum)<=0&&(m_CurPeriodJson.Start_Point+m_CurPeriodJson.Span+CpNum)>0)
			return true;
		else
			return false;
  }
  
  //按月
//跨度在当前时间内。
  private boolean IsInRangeofSpan(PeriodJson m_CurPeriodJson)
  {
	 
	  int CurNumofDay;
	  int MaxDaysofMonth;
	  SimpleDateFormat SimpleDate= new SimpleDateFormat("yyyy-MM-dd");//设置日期格式 
	  String SimpleDateStr=SimpleDate.format(new Date());
	
	  
	   Calendar  calendar = Calendar.getInstance();//获得一个日历
	   MaxDaysofMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	   
		GregorianCalendar gc=new GregorianCalendar(); 
		gc.setTime(new Date());  
		calendar.setTime(gc.getTime());
		CurNumofDay=calendar.get(Calendar.DAY_OF_MONTH);//得到今天是一个月中的第几天
		
	    
		if((m_CurPeriodJson.Start_Point+1)<=CurNumofDay&&(m_CurPeriodJson.Start_Point+m_CurPeriodJson.Span+1)>CurNumofDay)
			return true;
		else
			return false;		
	  
  }
//每次巡检判断巡检过程中是否超时
//=true,没有超时，可继续巡检；=false,超时，不能巡检，退出巡检过程
public  static boolean Is_NoTimeout(RoutePeroid m_RoutePeroid)
{

	boolean nRe=false;
	 String m_CurTime;
	  Calendar calendar;
	  
	  if(m_RoutePeroid.Is_Permission_Timeout==1)
		  return true;
	  ;
	  calendar = Calendar.getInstance();//获得一个日历
	  m_CurTime=String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
	  m_CurTime+=String.format("%02d", calendar.get(Calendar.MINUTE));
	  
	  if(m_RoutePeroid.Start_Time.compareTo(m_RoutePeroid.End_Time)<=0)
	  {
	     if((m_CurTime.compareTo(m_RoutePeroid.Start_Time)>=0&&m_CurTime.compareTo(m_RoutePeroid.End_Time)<=0)) 
		   nRe=true;
	  }
	  else
	  {
		  if((m_CurTime.compareTo(m_RoutePeroid.Start_Time)>=0&&m_CurTime.compareTo(m_RoutePeroid.End_Time)>=0)||(m_CurTime.compareTo(m_RoutePeroid.Start_Time)<=0&&m_CurTime.compareTo(m_RoutePeroid.End_Time)<=0))
			  nRe=true;
	  
	  };
	  
	  
	return nRe;	
   }

//得到当前轮次巡检的起始、结束日期
 private boolean GetCurTurnDate(TurnInfoJson m_TurnInfoJson,ContentValues nInfo)
 {
	 boolean nRe=false;
	  GregorianCalendar gc=new GregorianCalendar(); 		  
	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	  SimpleDateFormat SimpleDate= new SimpleDateFormat("yyyy-MM-dd");//设置日期格式 
	  String SimpleDateStr=SimpleDate.format(new Date());
		String StartDate,EndDate;
		StartDate=SimpleDateStr+" "+m_TurnInfoJson.Start_Time.substring(0, 2)+":"+m_TurnInfoJson.Start_Time.substring(2)+":00";
		EndDate=SimpleDateStr+" "+m_TurnInfoJson.End_Time.substring(0, 2)+":"+m_TurnInfoJson.End_Time.substring(2)+":00";
		if(m_TurnInfoJson.End_Time.compareTo(m_TurnInfoJson.Start_Time)<0)
	  	 {
			
			try {
				Date xz = df.parse(EndDate);
				 gc.setTime(xz); 
				 gc.add(Calendar.DATE,1); 
				 EndDate=df.format(gc.getTime());
				

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				  nInfo.put("err", "时间转换发生错误");
				  return false;	
				
			};
	
		 }	 ;
		 
		 nRe=true;
		 nInfo.put("StartDate",StartDate);
		 nInfo.put("EndDate",EndDate); 
		 
	 return nRe;
 }
}
