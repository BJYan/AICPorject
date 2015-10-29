package com.aic.aicdetactor.data;

import java.util.ArrayList;
import java.util.List;

public class JugmentParms {
	public LineInfoJsonEx T_Line;
	public PeriodInfoJson m_PeriodInfo;
	public List<TurnInfoJson> T_Turn;
	public WorkerInfoJson m_WorkerInfoJson;
	public RoutePeroid m_RoutePeroid;
	
	public JugmentParms(){
		T_Line = new LineInfoJsonEx();
		m_PeriodInfo = new PeriodInfoJson();
		T_Turn = new ArrayList<TurnInfoJson>();
		m_WorkerInfoJson = new WorkerInfoJson();
		m_RoutePeroid = new RoutePeroid();
	}
}
