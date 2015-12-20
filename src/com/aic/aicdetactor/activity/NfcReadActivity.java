package com.aic.aicdetactor.activity;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NfcReadActivity extends CommonActivity{
	NfcAdapter nfcAdapter;
	TextView nfcContent;
	Button mCancleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.dialog_nfc_layout);
		
		nfcContent = (TextView) findViewById(R.id.nfc_content);
		mCancleButton = (Button) findViewById(R.id.dialog_nfc_cancel_btn);
		mCancleButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        //得到是否检测到ACTION_TECH_DISCOVERED触发  
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {  
            //处理该intent  
            processIntent(getIntent());  
        } 
	}

	private void processIntent(Intent intent) {
		// TODO Auto-generated method stub
        //取出封装在intent中的TAG  
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
        for (String tech : tagFromIntent.getTechList()) {  
            System.out.println(tech);  
        }  
        boolean auth = false;  
        //读取TAG  
        MifareClassic mfc = MifareClassic.get(tagFromIntent);  
        try {  
            String metaInfo = "";  
            //Enable I/O operations to the tag from this TagTechnology object.  
            mfc.connect();  
            int type = mfc.getType();//获取TAG的类型  
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数  
            String typeS = "";  
            switch (type) {  
            case MifareClassic.TYPE_CLASSIC:  
                typeS = "TYPE_CLASSIC";  
                break;  
            case MifareClassic.TYPE_PLUS:  
                typeS = "TYPE_PLUS";  
                break;  
            case MifareClassic.TYPE_PRO:  
                typeS = "TYPE_PRO";  
                break;  
            case MifareClassic.TYPE_UNKNOWN:  
                typeS = "TYPE_UNKNOWN";  
                break;  
            }  
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"  
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";  
            for (int j = 0; j < sectorCount; j++) {  
                //Authenticate a sector with key A.  
                auth = mfc.authenticateSectorWithKeyA(j,  
                        MifareClassic.KEY_DEFAULT);  
                int bCount;  
                int bIndex;  
                if (auth) {  
                    metaInfo += "Sector " + j + ":验证成功\n";  
                    // 读取扇区中的块  
                    bCount = mfc.getBlockCountInSector(j);  
                    bIndex = mfc.sectorToBlock(j);  
                    for (int i = 0; i < bCount; i++) {  
                        byte[] data = mfc.readBlock(bIndex);  
                        metaInfo += "Block " + bIndex + " : "  
                                + bytesToHexString(data) + "\n";  
                        bIndex++;  
                    }  
                } else {  
                    metaInfo += "Sector " + j + ":验证失败\n";  
                }  
            }  
            nfcContent.setText(metaInfo);  
            //Toast.makeText(this, metaInfo, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}

	private String bytesToHexString(byte[] src) {
		// TODO Auto-generated method stub
        StringBuilder stringBuilder = new StringBuilder("0x");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        char[] buffer = new char[2];  
        for (int i = 0; i < src.length; i++) {  
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);  
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);  
            System.out.println(buffer);  
            stringBuilder.append(buffer);  
        }  
        return stringBuilder.toString();  
	}
}
