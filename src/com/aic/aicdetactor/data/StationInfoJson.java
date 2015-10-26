package com.aic.aicdetactor.data;

import java.util.List;


public class StationInfoJson
{
    public String Code ;
    public String Factory_Code ;
    public String Guid ;
    public int Is_In_Place ;
    public String Name ;
    public String T_Organization_Guid ;

    public List<DeviceItemJson> DeviceItem ;
    
    //method
//    public String getCode(){return Code;} ;
//    public String getFactory_Code(){return Factory_Code;} ;
//    public String getGuid(){return Guid;} ;
//    public int getIs_In_Place(){return Is_In_Place;} ;
//    public String getName(){return Name;} ;
//    public String getT_Organization_Guid(){return T_Organization_Guid;} ;
//
//    public List<DeviceItemJson> getDeviceItem(){return DeviceItem;} ;
    /**
     * 
     * @param bChecked ,true:只获取已巡检的；false，获取全部
     * @return
     */
	public int getDeviceCount(boolean bChecked) {
		int count = 0;
		if (bChecked) {
			for (DeviceItemJson json : DeviceItem) {
				if (json.Is_Device_Checked > 0) {count++;}
			}
		} else {
			count = DeviceItem.size();
		}
		return count;
	}
}
