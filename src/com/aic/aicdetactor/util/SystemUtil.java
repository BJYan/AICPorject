package com.aic.aicdetactor.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.CRC32;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.setting.Setting;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;



public class  SystemUtil {

	static String TAG ="luotest";
	public static final int TIME_FORMAT_YYMMDDHHMM = 0;
	public static final int TIME_FORMAT_HHMM = 1;
	public static final int TIME_FORMAT_YYMMDD = 2;
	public static final int TIME_FORMAT_YYMMDD2 = 3;
	/**
	 * 
	 * @param type 0,yyyy-mm-dd hh:mm:ss;
	 * 1:hhmm;
	 * 2:yyyy-mm--dd
	 * @return
	 */
	public  static String getSystemTime(int type){
		String str = null;
		if(type ==TIME_FORMAT_YYMMDDHHMM){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str = df.format(new Date());
		MLog.Logd(TAG, "getSystemTime time is " + str);
		}else if (type ==TIME_FORMAT_HHMM){
			Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
			t.setToNow(); // 取得系统时间。  
			int year = t.year;  
			int month = t.month;  
			int date = t.monthDay;  
			int hour = t.hour; // 0-23  
			int minute = t.minute;  
			int second = t.second;  
			str  = (hour<10?("0"+hour):hour)+""+(minute<10?("0"+minute):minute);
		}else if(type ==TIME_FORMAT_YYMMDD){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			str = df.format(new Date());
		}else if(type ==TIME_FORMAT_YYMMDD2){
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			str = df.format(new Date());
		}
		
		
		
		
		
		return str;
	}
	public static String createGUID(){
		String str = null;
		UUID uuid = UUID.randomUUID();		
		str = uuid.toString();
		MLog.Logd(TAG, "createGUID create a new GUID is  " + str);
		return str;
	}
	
	public static List<Map<String, Object>> reverseListData(List<Map<String, Object>> inList){
		
		if(inList == null) return null;
		List<Map<String, Object>> OutList = new ArrayList<Map<String, Object>>();
		for(int i = inList.size()-1;i>=0;i--){
			OutList.add(inList.get(i));
		}
		inList.clear();
		inList.addAll(OutList);
		return inList;
	}
	public static String getDataRootStoragePath(){
		
		Setting setting = new Setting();
	
		return setting.getData_Root_Director();
	}
	
	/**
	 * 根据文件类型，返回不同的文件路径
	 * @param fileType 0：拍照图片路径，1：录音文件路径，2：快速记录文件类型
	 * @return
	 */
	public static String getDataMediaStoragePath(int type){
		
		Setting setting = new Setting();
	
		return setting.getData_Media_Director(type);
	}
	
	public static boolean deleteFile(String filePath) {
	    File file = new File(filePath);
	        if (file.isFile() && file.exists()) {
	        return file.delete();
	        }
	        return false;
	    }
	
	public static boolean deleteDirectory(String filePath) {
	    boolean flag = false;
	        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
	        if (!filePath.endsWith(File.separator)) {
	            filePath = filePath + File.separator;
	        }
	        File dirFile = new File(filePath);
	        if (!dirFile.exists() || !dirFile.isDirectory()) {
	            return false;
	        }
	        flag = true;
	        File[] files = dirFile.listFiles();
	        //遍历删除文件夹下的所有文件(包括子目录)
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isFile()) {
	            //删除子文件
	                flag = deleteFile(files[i].getAbsolutePath());
	                if (!flag) break;
	            } else {
	            //删除子目录
	                flag = deleteDirectory(files[i].getAbsolutePath());
	                if (!flag) break;
	            }
	        }
	        if (!flag) return false;
	        //删除当前空目录
	        return dirFile.delete();
	    }

	    /**
	     *  根据路径删除指定的目录或文件，无论存在与否
	     *@param filePath  要删除的目录或文件
	     *@return 删除成功返回 true，否则返回 false。
	     */
	public static boolean DeleteFolder(String filePath) {
	    File file = new File(filePath);
	        if (!file.exists()) {
	            return false;
	        } else {
	            if (file.isFile()) {
	            // 为文件时调用删除文件方法
	                return deleteFile(filePath);
	            } else {
	            // 为目录时调用删除目录方法
	                return deleteDirectory(filePath);
	            }
	        }
	    }
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	public static float ByteArrayToFloat(byte[] bytes) {
		int i = ((((bytes[0] & 0xff) << 8 | (bytes[1] & 0xff)) << 8) | (bytes[2] & 0xff)) << 8 | (bytes[3] & 0xff);
		return Float.intBitsToFloat(i);
	}
	
	public static float getTemperature(String s){
		byte[] bytes =hexStringToByteArray(s);
		return ByteArrayToFloat(bytes);
	}
	
	public static void writeFileToSD(String fileName, String StrContent)
			throws IOException {
		
		MLog.Logd("luotest", "writeFileToSD() StrContent is  "+StrContent);		
		File file = new File(fileName);
		;
		MLog.Logd("luotest", "writeFileToSD() name is "+file.getName());

		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(StrContent.getBytes("utf-8"));
		outStream.close();
	}  
	
	public static String openFile(String path) {
		if (null == path) {

			return null;
		}

		MLog.Logd("luotest", "path 1= " + path);
		File file = new File(path);
		if (file.exists()) {
			MLog.Logd("luotest", "path 2= " + path);
			try {
				StringBuffer sb = new StringBuffer();
				// HttpEntity entity = response.getEntity();
				InputStream is = new FileInputStream(path);// entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "utf-8"));
				String data = "";

				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				String result = sb.toString();
				
				// return result.getBytes("UTF-8");
				return result;
			} catch (Exception e) {
				MLog.Logd("luotest", "read data exception " + e.toString());
				e.printStackTrace();
			}
		}
		MLog.Logd("luotest", "path 3 = " + path);
		return null;
	}	
	
	/**
	 * 计算给定时间与系统当前时间的差
	 * 入参 time 格式必须是 "yyyy-MM-dd HH:mm:ss" 
	 * @param time
	 * @return
	 * 几分钟前
	 * 几小时前
	 * 昨天
	 * 几天前 
	 */
	public  static String getDiffDate(String time,Context ct){
		String str = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			
		  Date d1 = df.parse(time);		 
		  Date d2 = df.parse(getSystemTime(TIME_FORMAT_YYMMDDHHMM));
		  long diff = d2.getTime() - d1.getTime();
		  long days = diff / (1000 * 60 * 60 * 24);
		 
		  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
		  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		  
		  if(days<1){
			  if(hours<1){
				  //分钟前
				  String sMinutesFormat = ct.getString(R.string.time_tips_minutes); 
				  str = String.format(sMinutesFormat, minutes);
			  }else{
				  //小时前
				  String sHoursFormat = ct.getString(R.string.time_tips_hours); 
				  str = String.format(sHoursFormat, hours);
			  }
		  }else{
			  //几天前
			  if(days ==1){
				  str  = ct.getString(R.string.time_tips_yestoday);   
			  }else{
				  String sdayFormat = ct.getString(R.string.time_tips_days); 
				  str = String.format(sdayFormat, days);
				  
			  }
			  
		  }
		}
		catch (Exception e)
		{
		}
		return str;
	}

	/**
	 * 计算给定两个时间的差
	 * 入参 time 格式必须是 "yyyy-MM-dd HH:mm:ss" 
	 * @param time
	 * @param endTime
	 * @return 单位 秒
	 */
	public static String getDiffDate(String time, String endTime) {
		if(time==null || endTime==null ||"".equals(time)||"".equals(endTime)){return ""+0;}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(time);
			Date d2 = df.parse(endTime);
			long diff = d2.getTime() - d1.getTime();
			long minutes = diff / 1000;
			return "" + minutes;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ""+0;
	}
	public static void writeFile(String canonicalFilename, String text) 
			throws IOException
			{
			  File file = new File (canonicalFilename);
			  BufferedWriter out = new BufferedWriter(new FileWriter(file)); 
			  out.write(text);
			  out.close();
			}
	
	public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
	public static byte[] float2byte(float f) {  
	      
		    // 把float转换为byte[]  
		    int fbit = Float.floatToIntBits(f);  
		      
		    byte[] b = new byte[4];    
		    for (int i = 0; i < 4; i++) {    
		        b[i] = (byte) (fbit >> (24 - i * 8));    
		    }   
		      
		    // 翻转数组  
		    int len = b.length;  
		    // 建立一个与源数组元素类型相同的数组  
		    byte[] dest = new byte[len];  
		    // 为了防止修改源数组，将源数组拷贝一份副本  
		    System.arraycopy(b, 0, dest, 0, len);  
		    byte temp;  
		    // 将顺位第i个与倒数第i个交换  
		    for (int i = 0; i < len / 2; ++i) {  
		        temp = dest[i];  
		        dest[i] = dest[len - i - 1];  
		        dest[len - i - 1] = temp;  
		    }  
		      
		    return dest;  
		      
		}  
	
	 public static  String getCRC32(byte[] str){
	    	CRC32 crc32 = new CRC32(); 
	    	crc32.update(str); 
	    	return Long.toHexString(crc32.getValue());
	    	
	    }
	 
	 
	 final static int JianGe=6;
	 /**
	  * 
	  * @param CaiyangDian
	  * @param CaiyangPinLv
	  * @param ZhouShu
	  * @return
	  */
	 public static int getReceiveBLEDataTimeOut(int CaiyangDian,int CaiyangPinLv,int ZhouShu){
		 
		 int timeOut=0;
		 timeOut = ZhouShu*1000*(CaiyangDian/(CaiyangPinLv>0?CaiyangPinLv:1))+(CaiyangDian/20 +2)*JianGe+1500;
		 return timeOut;
	 }
}
