package network.aic.xj.common.json.normal.result;

import java.util.ArrayList;

public class NormalPlanUploadResult {
    public GlobalInfo GlobalInfo = null;
    public ArrayList<StationInfo> StationInfo = null;
    public ArrayList<ItemAbnormalGrade> T_Item_Abnormal_Grade = null;
    public LineInfo T_Line = null;
    public ArrayList<MeasureType> T_Measure_Type = null;
    public OrganizationInfo T_Organization = null;
    public PeriodInfo T_Period = null;
    public ArrayList<TurnInfo> T_Turn = null;
    public ArrayList<WorkerInfo> T_Worker = null;
}
