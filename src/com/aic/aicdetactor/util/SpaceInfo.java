package com.aic.aicdetactor.util;

import java.io.File;

import android.os.StatFs;

public class SpaceInfo {
	
	private long  available =0;
	private long  Totals =0;
	public  SpaceInfo(String path){
		
		 if(path!=null&& path.length()>0&& new File(path).exists()){
			
		 
		 StatFs stateFs = new StatFs(path);
		 long blockSize = stateFs.getBlockSize();
		 long totalBlocks = stateFs.getBlockCount();
		 
		 long AvalibleBlocks = stateFs.getAvailableBlocks();
		 Totals = getPhoneSace(totalBlocks*blockSize);
		 available = AvalibleBlocks*blockSize;
		 }
	 }
	 
long	 getPhoneSace (long result){
		 final long Phone_Space_Values[]={
				 1*1024*1024*1024,
				 2*1024*1024*1024,
				 4*1024*1024*1024,
				 8*1024*1024*1024,
				 
				 16*1024*1024*1024,
				 32*1024*1024*1024,
				 64*1024*1024*1024 };
		 for(int i=0;i<Phone_Space_Values.length;i++){
			 if(result<=Phone_Space_Values[i]){
				 return Phone_Space_Values[i];
			 }
			
		 }
		 
		 return result;
	 }

public long getAvaliableSpace(){
	return available;
}

public long getAllSpace(){
	return Totals;
}

}
