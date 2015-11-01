package com.aic.aicdetactor.data;

import java.util.List;



public  class DownloadNormalRootData
{
    public GlobalInfoJson GlobalInfo ;
    public List<StationInfoJson> StationInfo ;
    public List<ItemAbnormalGradeJson> T_Item_Abnormal_Grade ;
    public LineInfoJson T_Line ;
    public List<MeasureTypeJson> T_Measure_Type ;
    public OrganizationInfoJson T_Organization ;
    public PeriodInfoJson T_Period ;
    public List<TurnInfoJson> T_Turn ;
    public List<WorkerInfoJson> T_Worker ;
  

    public int getStationCount(){
    	if(StationInfo!=null){
    		return StationInfo.size();
    	}
    	return 0;
    }
    /**
     * 
     * @param type; line 0;station 1; device 2
     * @param index
     * @param isChecked
     * @return
     */
	public int getItemCounts(int type, int index, boolean isChecked,boolean bIncludeSpecial) {
		int count = 0;
		// for(StationInfoJson info:StationInfo){
		for (int i = 0; i < StationInfo.size(); i++) {
			if (type == 0) {
				StationInfoJson info = StationInfo.get(i);
				List<DeviceItemJson> deviceList = info.DeviceItem;
				for (int m = 0; m < deviceList.size(); m++) {
					// for(DeviceItemJson deviceItem:deviceList){
					DeviceItemJson deviceItem = deviceList.get(m);
					if (isChecked) {
						if (deviceItem.Is_RFID_Checked > 0) {
							count += deviceItem.PartItem.size();
						} else {
							continue;
						}
					} else {
						count += deviceItem.PartItem.size();
					}

					// List<PartItemJson> partItemList = deviceItem.PartItem;
					// for(int n=0;n<partItemList.size();n++){
					//
					// }

				}
			} else if (type == 2) {
				StationInfoJson info = StationInfo.get(i);
				List<DeviceItemJson> deviceList = info.DeviceItem;
				for (int m = 0; m < deviceList.size(); m++) {
					// for(DeviceItemJson deviceItem:deviceList){
					if (m == index) {
						DeviceItemJson deviceItem = deviceList.get(m);
						if (isChecked) {
							if (deviceItem.Is_RFID_Checked > 0) {
								count += deviceItem.PartItem.size();
							} else {
								continue;
							}
						} else {
							count += deviceItem.PartItem.size();
						}
					}
				}
			} else if (type == 1) {
				if (i == index) {
					StationInfoJson info = StationInfo.get(i);
					List<DeviceItemJson> deviceList = info.DeviceItem;
					for (int m = 0; m < deviceList.size(); m++) {
						// for(DeviceItemJson deviceItem:deviceList){

						DeviceItemJson deviceItem = deviceList.get(m);
						if (isChecked) {
							if (deviceItem.Is_RFID_Checked > 0) {
								count += deviceItem.PartItem.size();
							} else {
								continue;
							}
						} else {
							count += deviceItem.PartItem.size();
						}
					}
				}
			}
		}
		return count;
	}
	
}
