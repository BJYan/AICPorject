package com.aic.aicdetactor.data;

public class GlobalInfoJson
{
    public String Check_Date ;
    public String Guid ;
    public String End_Time ;
    public String Start_Time ;
    public String T_Period_Name ;
    public int Turn_Number ;
    public int Task_Mode ;
    public String T_Worker_Guid;  //人员Guid ,2015-12-06添加, 第一次谁检的填谁的GUID。
    public String Check_Datetime;  //2015-12-06添加, 第一次检的日期 时间
    public int Turn_Finish_Mode ; //2015-12-06添加,第一次巡检从PeriodJson.Turn_Finish_Mode拷贝  
    public String T_Turn_Guid;  //2015-12-06添加,第一次巡检从T_turn中得到Guid。

    //Method
//    public void setCheck_Date(String date){Check_Date = date;} ;
//    public void setGuid(String guid) {Guid=guid;};
//    public void setEnd_Time(String endtime){End_Time=endtime;} ;
//    public void setStart_Time(String startTiem){Start_Time=startTiem;} ;
//    public void setT_Period_Name(String PeriodName){T_Period_Name=PeriodName;} ;
//    public void setTurn_Number(int TurnNumber){Turn_Number=TurnNumber;} ;
//    public void setTask_Mode(int TaskMode){Task_Mode=TaskMode;} ;
    
}
