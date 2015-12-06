package com.aic.aicdetactor.data;

public class TurnInfoJson
{
    public String End_Time ;
    public String Name ;
    public String Start_Time ;
    public int Number ;
    public String T_Line_Content_Guid ;
    public String T_Line_Guid ;
    public String Guid;//2015-12-06添加,APP不用任何处理，直接上传。
    public String T_Worker_Guid;  //人员Guid ,2015-12-06添加, 第一次谁检的填谁的GUID。
    public String Check_Datetime;  //2015-12-06添加, 第一次检的日期 时间
    public int Turn_Finish_Mode ; //2015-12-06添加,第一巡检从PeriodJson.Turn_Finish_Mode拷贝

}
