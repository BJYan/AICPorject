package com.aic.aicdetactor.check;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.media.SoundRecordActivity;

public class TempPlanActivity extends Activity implements OnClickListener {

	private EditText mEditTextTitle = null;
	private EditText mEditTextContent = null;
	private Spinner mSpinner = null;
	private Button mButtonCamera = null;
	private Button mButtonSoundRecord = null;
	private Button mButtonSave = null;
	private static final int RESULT_CODE = 1; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.temporary_task_activity);
		mButtonCamera = (Button)findViewById(R.id.camera);
		mButtonCamera.setOnClickListener(this);
		mButtonSoundRecord = (Button)findViewById(R.id.soundRecord);
		mButtonSoundRecord.setOnClickListener(this);
		mButtonSave = (Button)findViewById(R.id.save);
		mButtonSave.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.camera:
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			 ContentValues values = new ContentValues(3);  
             values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");  
             values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");  
             values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");  
             Uri imageFilePath = TempPlanActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
             Log.d("test","main_media imageFilePath is "+imageFilePath);
             intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath); //这样就将文件的存储方式和uri指定到了Camera应用中  
               
             
			//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 			  
            startActivityForResult(intent, RESULT_CODE);  
            }
			break;
		case R.id.soundRecord:
			Intent intent = new Intent();
			intent.setClass(TempPlanActivity.this, SoundRecordActivity.class);
			startActivity(intent);
			break;
		case R.id.save:
			saveData();
			break;
		}
	}
	
	void saveData(){
		
	}

}
