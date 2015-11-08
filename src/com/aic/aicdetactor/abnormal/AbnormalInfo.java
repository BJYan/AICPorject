package com.aic.aicdetactor.abnormal;

import java.util.List;

import com.aic.aicdetactor.data.ItemAbnormalGradeJson;

public class AbnormalInfo {

public static int getIdByCode(List<ItemAbnormalGradeJson>list,String abnormalCode){
	if(list==null){return -1;}
	int id=-1;
	for(ItemAbnormalGradeJson ab:list){
		if(ab.Code.equals(abnormalCode)){
			id= ab.Id;
			break;
		}		
	}	
	return id;
}


public static int getIdByName(List<ItemAbnormalGradeJson>list,String Name){
	if(list==null){return -1;}
	int id=-1;
	for(ItemAbnormalGradeJson ab:list){
		if(ab.Name.equals(Name)){
			id= ab.Id;
			break;
		}		
	}	
	return id;
}

public static String getCodeByName(List<ItemAbnormalGradeJson>list,String Name){
	if(list==null){return "";}
	String code="";
	for(ItemAbnormalGradeJson ab:list){
		if(ab.Name.equals(Name)){
			code= ab.Code;
			break;
		}		
	}	
	return code;
}

}
