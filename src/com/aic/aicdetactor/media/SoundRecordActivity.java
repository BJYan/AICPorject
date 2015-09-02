package com.aic.aicdetactor.media;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.PartItem_Contact;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.MediaDao;
import com.aic.aicdetactor.util.SystemUtil;

public class SoundRecordActivity extends Activity implements OnClickListener{
	private Button mButton_Start = null;
	private Button mButton_Playback = null;
	private TextView mTextView = null;
	private MediaRecorder mMediaRecorder = null;
	private int recLen= 0;
	private boolean bRecording = false;
	private boolean bPlaying = false;
	long mStartMicroSecond =0;
	long mDuration = 0;
	String mFileName = null;
	String mFilePath = null;
	private Cursor mCursor = null;
	MediaDao mMediaDao = null;
	MediaPlayer mMediaPlayer = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.media_sound_record);
		super.onCreate(savedInstanceState);
		mButton_Start = (Button) findViewById(R.id.start);
		mButton_Start.setOnClickListener(this);
		mButton_Playback = (Button) findViewById(R.id.playback);
		mButton_Playback.setOnClickListener(this);
		mTextView = (TextView) findViewById(R.id.time);
		mMediaDao = new MediaDao(this.getApplicationContext());
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				mButton_Start.setEnabled(true);
			}
			
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.start:
			if (!bRecording) {
				mButton_Start.setText(getString(R.string.sound_save));
				handler.postDelayed(runnable, 1000);
				initializeAudio();
				mButton_Playback.setEnabled(false);
			} else {
				if (mMediaRecorder != null) {
					mDuration = System.currentTimeMillis() - mStartMicroSecond;
					mMediaRecorder.stop();
					mMediaRecorder.release();
					mMediaRecorder = null;
					mButton_Start.setText(getString(R.string.sound_start));
					save();
					bRecording = !bRecording;
					mButton_Playback.setEnabled(true);

				}
			}
			break;
		case R.id.playback: {
			if (!bRecording) {
				mButton_Start.setEnabled(false);
				if (mFilePath == null) {
					mCursor = mMediaDao.queryRecord(CommonDef.FILE_TYPE_AUDIO);
					if (mCursor != null) {
						mCursor.moveToFirst();
						mFilePath = mCursor.getString(mCursor
								.getColumnIndex(DBHelper.MEDIA_PATH_NAME));
						mCursor.close();
						mCursor = null;
					}else{
						mButton_Start.setEnabled(true);
					}
				}
				player(mFilePath);

			}
		}
			break;
		default:
			break;
		}
	}
	
	private void stopPlayer(){
		if(mMediaPlayer != null){
			mMediaPlayer.stop();
			
		}
	}
	private void releasePlayer(){
		if(mMediaPlayer != null){
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	private void player(String filepath){
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(filepath);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			bPlaying = true;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 private void initializeAudio() { 
		 mFileName = SystemUtil.createGUID()+".amr";		
		 
		// mFileName = "adbcderewe124543234335435534534543543534543"+".amr";	
		 mFilePath = SystemUtil.getDataMediaStoragePath(CommonDef.FILE_TYPE_AUDIO)+mFileName;		
		 Log.d("luotest","initializeAudio()"+ mFilePath);
		 bRecording = true;
		 mMediaRecorder = new MediaRecorder();// new出MediaRecorder对象  
		 mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
         // 设置MediaRecorder的音频源为麦克风  
		 mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);  
         // 设置MediaRecorder录制的音频格式  
		 mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
         // 设置MediaRecorder录制音频的编码为amr.  
		 mMediaRecorder.setOutputFile(mFilePath);  
         // 设置录制好的音频文件保存路径  
         try {  
        	 mMediaRecorder.prepare();// 准备录制  
        	  mMediaRecorder.start();// 开始录制  
         	 mStartMicroSecond= System.currentTimeMillis();
        	
        	
         } catch (IllegalStateException e) {  
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
         } 
         }  
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopPlayer();
		releasePlayer();
		super.onDestroy();
	}

	@Override
    public void onBackPressed() {
        //Log.i(TAG, "onBackPressed");
		Intent intent = new Intent();
		intent.putExtra(CommonDef.AUDIO_PATH, mFilePath);
        setResult(PartItem_Contact.PARTITEM_NOTEPAD_RESULT,intent);
        super.onBackPressed();
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private  void save(){
		if(bRecording){
		
		mMediaDao.insertInfo(CommonDef.FILE_TYPE_AUDIO, mFileName, mFilePath, null, mDuration);
		}
	 }
	 Handler handler = new Handler(); 
	    Runnable runnable = new Runnable() { 
	        @Override 
	        public void run() { 
	            recLen++; 
	            mTextView.setText("" + recLen); 
	            handler.postDelayed(this, 1000); 
	        } 
	    }; 
}
