package com.aic.aicdetactor.bluetooth.analysis;

import android.util.Log;

public class DataAnalysis {
	private static final String TAG = "DataAnalysis";

	/*String str = "7d7d7d7d0101040009e5f6ecf221edd0ea03e744e570e444e4b5e60de818ebb5ef78f40df91efe7003e908ee0dc0121d158c"
			+ "18381a061ab01a4b18e8169a13040ef90a7a0519fff0fa8cf57af09fec67e903e6a6e4dfe472e4dee68de914ec8af0e3f5b1faa"
			+ "dfff105750a6d0f131334167b18c11a3d1a4b19a717cd153a11d90d7f08d20351fe1cf8f3f3c6ef58eb56e835e615e4c5e4a3e5"
			+ "7ce76aea65edeef27cf74dfc8d01dc070f0bc91099147b174019931a851a801976173d1421105f0bdf070b018dfc41f74df230e"
			+ "dd6ea17e73ae554e47ce4c0e5c8e830eb48ef3af3d8f8d0fe39036f08980dad11ff155f182319f31afc1a5718e1167413410f41"
			+ "0a7f055e000cfac5f5a9f0e7ec9ae91ae6aee4f6e4a1e4eae69ae911ec7cf0b1f579fa84ffee05410a3c0ef61312165c18a91a3"
			+ "d1a5c19d017c2156811de0d7e08ee03b0fe39f8d2f3bdef60eb4ae815e5e0e4c7e475e565e77bea05edf8f227f708fc8201af07"
			+ "0c0bf5109214441756198f1a881a861967177c147210a60bef072b01e9fc4df734f240ede0ea14e771e54de45be4b1e5c2e801e"
			+ "b1fef39f3a4f892fe17035608d30d7311981541181519cc1ab21a311917166e13620f190a91059c0036fad5f5bcf0f0ecbbe95c"
			+ "e6aee50ae491e516e683e931ec82f08ff570fa7efff6050f0a740eec12fe164018c819fc1abb19d0182f158c11e70db108c603a"
			+ "cfe21f906f3f7ef73eb4fe82ee609e49de46fe54ee725ea26edd6f218f718fc1101ae06bd0bdd10641431173619891aa01a8f19"
			+ "8c17a314d310b20c3f07750207fc74f749f28eee06ea36e741e57fe467e498e5c8e807eb00ef12f35cf884fdd60329085e0d401"
			+ "1911552180419be1aa81a4818f21685136c0f4d0acc059f0016fb02f5f2f107eca2e969e6a0e53ae455e516e691e8fdec64f078"
			+ "f551fa47ffa604ec0a210eea12e1165818c41a371a9a19f718051583122e0de308f7039efe7ef90af40eefa1eb70e7fee60be4a"
			+ "4e47ee563e6f8ea0fed4ef1e6f6c7fc07015f06de0bd610181424174019651a6a1a91197d17be148b10f70c7207650256fcecf7"
			+ "9df2b5ee35ea5be767e568e4a4e4abe5c1e821eacbeee0f381f881fdb90330084a0d26118e154617b419ca1a911a3618f116ab1"
			+ "3560f550aa605c60058fb13f5e0f10cecd5e93fe6a3e507e44fe4b5e684e911ec23f07ef505fa64ffa705110a200eda12e01642"
			+ "18731a0c1aa719eb183d15b1123d0df4093003e1feb0f935f42eef93eb88e855e602e495e478e534e6e2e9f9ed68f19bf687fbb"
			+ "f00e706a00ba70fdf13d1173b194c1a811a5619a5179914c510fc0c670796024efcdbf7c5f2ffee3dea93e79ce59ee48ee4b6e5"
			+ "e3e806ead8eee4f39cf868fd9a0308082c0cf6114f151417f919c91a5f1a6818e616ca138c0f810ab505bf005dfb3bf608f13ce"
			+ "cd3e973e6efe517e442e4d7e648e8caec38f01ef50afa14ff8304e009e70ea412ff162318c01a451ab81a0c188a160412240dfb"
			+ "0956042dfec7f96bf430efe4ebc4e8a0e60ee4b1e474e524e70de9a1ed63f1acf677fbc60104067a0b6e0fdb13da16e919161a6"
			+ "a1a70198b178214ea10ed0ca307af0272fd72f801f2d4ee59eabbe7aee5a5e486e4bae5d8e7f3eb00eec4f356f833fd7202e807"
			+ "f20d0e118214f217c219c01ac01a55191516c2138e0f840ae405f30091fb53f617f13eecfbe969e6b2e50be453e4d1e634e899e"
			+ "c45f05ff4ccf9cbff44049c099c0e6c12b6162518bb1a0e1aa01a291878160412870e4809870468ff0df9a7f48beff2ebdbe862"
			+ "e602e4f3e499e53ee6ace9abed17f179f641fb9a0100062d0b5a0fd313f116db19231a521a77198b178014c711010cec079f025"
			+ "6fd3cf7bcf30eee8eea8fe7dfe5b6e4b2e4b8e5cce7d7eadeeeb3f34df841fd6602df08130d00113d151317ab19d41a901a5b19"
			+ "4016e413850fcc0b21061a00ccfb64f63df18aed0ee981e6cae505e468e4c1e628e87febd2eff4f4b3f9c1ff270478099e0e231"
			+ "24e15ee18731a291a781a2c189915eb127a0e8b099c0485ff1afa08f4a8f005ebf6e8c6e63ae4fbe4b5e540e6cce999ed07f1b6"
			+ "f64ffb9700de06210b310fe813cf16be18fc1a591a7b19b617b714e611180cc307c702b3fd29f80df301ee73eabfe7c4e5c6e49"
			+ "9e468e581e7a9eac4ee95f301f81ffd3702cf07db0cd2114014c517c919c31ac71a59195e170113db0fdf0b5a062c00defbb5f6"
			+ "a1f182ed18e9b2e6b5e529e459e4d0e613e8a2ebafefe2f47df9a6fee0043f09680e1a127d15be18601a101a9a1a2f189415cc1"
			+ "2c30e8609bf0486ff2efa00f4cdf02eec13e8bbe65de4e4e491e538e6ebe9c1ed33f178f65afb7500cf06070b330fc913a516f1"
			+ "18ef1a5a1a74199e17e214f011630ce907f002d3fd5cf806f321eeaceacde7bae592e492e482e58ce79aea9aee7cf2e1f7e7fd0"
			+ "8028807a10cb310e914ea17b719db1aa61a841932173613ef100a0b910689015efbbff68cf1e2ed5ee9c4e72ae56fe47ce4b6e6"
			+ "1ee886eb9eefbbf465f976fe800430094e0e29121d15c4184e19e61a6b19fe186b161712b60e6009c2047aff51fa15f4e0f046e"
			+ "c04e8d8e670e4d3e489e540e6e2e97cecf9f168f5fffb71009e06000b230fb313b016de190c1a6d1a7f199817ca14ec11870cf4"
			+ "082402f0fdaef849f35feed3eae7e7dae599e498e491e58de76bea84ee33f29af7b6fcf00242078f0c6710c014b81771198b1a8"
			+ "31a2a1972171713f710380bac0692016bfbddf6bef1b7ed82e9c9e734e55ee472e4ece650e88febbcef8ef44ff98efea6043409"
			+ "290dd7124f15a5183919fd1aa51a08189c164012b60ea909e804cbff8afa04f4eaf03fec47e8d0e66fe50ee472e500e6c4e9470"
			+ "0001afce4440000000000000000000000000000279e0000070489572024";*/

	String str ="7d7d7d7d0101040009e5ebf3f02cf4c4f9e6ff4b049f09c00e5312a61615187c19fb1a9f1a20189d15d212850e44097f046bf"
			+ "f08f9d2f49af006ebc4e88ee619e512e42de54ae6d6e9c7ed7bf17ff66efbc000f206560b5e0fd513c816e719101a531a62198e"
			+ "179814b111270c9507ad0284fd1bf7eef2d6eeabeab4e77ee56be4a4e496e58de7bdeae2eec1f318f836fd5702d808110cf5115"
			+ "5150517c419eb1a991a7d194416fd13e40fa00b52060800d9fb59f660f15ded15e994e6dfe51de475e4b2e60fe898ebd3effef4"
			+ "b1f9d5fef5047509670e3d127a160b188d19f11aa21a1c1860160a12670e4f09a8045eff22f9ddf4c2f001ebece8b0e645e4fae"
			+ "471e54ee6f5e9b9ed20f1a3f652fb7700b906310b5f0fd2138216dc19151a4e1ab0199917b714c511520cbf07e102b3fd28f803"
			+ "f306ee91eaade7a5e597e46de491e5eae79bea7eee5cf306f80efd4802b707d80cc8114314db17b119b21a9b1a721911172413b"
			+ "f0fce0b7806450117fb9af68df174ed07e9b6e707e55ce48de4cce639e88bebb7efeaf482f98cfec4044b09610e2d124f15c318"
			+ "3b19df1a821a2a185a15ee12aa0e6309d20481ff3cf9dcf4dff029ec1ee8bde64ce4e2e492e520e6f9e99eed1af18cf63efba10"
			+ "0c4063f0b0e0fc9139516e619091a291a7519bb17cb14fb11760ce2081e02b6fd61f812f333ee9feab9e7ace5d9e463e491e599"
			+ "e78deaa3ee60f2fdf7b8fd200294077e0c6410d914d117ba19b31a741a52193916f413ea10130b6206990118fbdcf691f1dbed5"
			+ "ae9f3e6f3e530e45ee4bde650e87eebc6efa9f481f97ffef40411094d0e0a124615b1184e19f11a7319f8186315fd128f0e7209"
			+ "a3048dff44f9e3f50cf023ec26e8c2e647e4dce462e549e6c5e968ed10f151f63bfb5d00aa06140b0a0fae13a616c7190c1a7a1"
			+ "aad19bc17ea154011a80d47084202dbfdc7f875f32aeebfead7e7dbe586e49ae48ce56ee77eea69ee29f2b7f7a7fcf1022a077d"
			+ "0c4610e214aa17b119761a801a71198c1732140510060b9706b40150fbf9f6b1f1f4ed89e9f2e705e558e479e4eae61fe893ebc"
			+ "0efb6f451f94afec0042109210e121232159d18541a021a951a0318b215f112bc0ea909d104c9ff5cfa53f4eaf027ec46e89ce6"
			+ "3ee4e7e457e530e6cce912ed03f0f5f5e6fb34007105b90af60f8a138116a319131a691aab19ee1813154711730d68085c0344f"
			+ "dc8f896f36aeea9eb6ae825e5bee4c2e487e58ce782ea66ee24f288f788fcb7022d07670c4a10b51482177519441a5f1a531951"
			+ "1713140210350b8b06920160fbe1f6e1f1fded97ea03e72ee565e494e4d9e603e871eb98efb6f454f96afeb903ed09310de7123"
			+ "b159a183119d81a711a2b18c3160913090eb30a1f0512ff9bfa46f511f068ec34e8e9e687e4f6e475e519e665e959ecb5f10ff5"
			+ "b6face005205a70aa50f7713661697190a1a501acd19d0181c155011bb0d7108710376fdd9f8c3f37eef2beb39e81ee5d1e4d1e"
			+ "457e593e781ea6cee29f27ff763fc8a0233072c0c3b109714871753196c1a6b1a5a192d171f141f102f0bce06c70193fc24f70b"
			+ "f217ed9ee9ffe714e545e4b6e4bde5e2e823eb9bef9ef41ef92dfe7703f309190dd4122315ad186b1a061ac51a3118e3166d133"
			+ "50ede09eb051effebfa84f555f076ec5fe8fce688e4d5e45de4ffe66de937ecabf0bff576faf7fff605550a970f261335167818"
			+ "da1a371a5a19fe17f0154511f40d7408ab0350fe18f8cff3a7ef34eb32e82ee5f7e4cee497e58de779ea74ee0af297f74afc650"
			+ "21607640c4810b3143b174b195f1a7c1a7319531754142b107f0bca06c601a2fc2ef6daf1e0eda3e9efe715e532e46ee474e615"
			+ "e82aeb6eef3cf3f1f8fbfe52039508e60db311e11599182f1a011aaf1a3818d9168b13520f120a5e052affe2fa9af53af09aec8"
			+ "ae91fe68ae4e3e466e501e68ce922ec88f0a9f564faa0ffef05610a460ef91317166118b51a1e1a9819d71816155e11fd0dab08"
			+ "b403a9fe6af8f6f3edef60eb67e82fe604e4b4e48ee572e74fea4dedfcf27ef737fc9301b807290c1510a01470175819391a7d1"
			+ "a9e19771761144910750bfe070a01a7fc73f71af245ede9ea0de74ee572e45de49fe5f9e7f5eb30ef06f3bdf8cbfdfd03810897"
			+ "0d8d11b91563182719db1aa61a3618d0168913320f530a850596002ffacdf5b9f0ececaee90fe67ee51ee488e4f5e6cee932ec9"
			+ "3f078f561fa6effcf052e0a5f0edd130c164918ae1a201a6e19cc180a157511ea0d9b08c4039efe69f8e6f3dfef49eb3fe81ce5"
			+ "eae4bee491e560e774ea0aedd2f219f71afc6b01a1070d0bee10981450175b197a1a8b1a8d197b175714a910ca0c20075501dcf"
			+ "cbbf732f259edecea37e746e555e476e4a2e5b8e7ffeb23ef18f3a8f88dfdf00357088a0d6011a91546182a19c61a9b1a2518f1"
			+ "169f133b0f590a9305a00028faf5f5aef104eccfe951e6c2e51ee488e504e6a3e918ec79f07bf54cfa55ffb205240a2e0ee912e"
			+ "b164718b91a191ac419bf1848159911fa0dcc08df03e7fe4af8f9f402ef70eb4fe843e5e7e4c1e480e532e732ea08ed6df1d3f6"
			+ "f3fc0f01ac06c00bdb105b1431175219511aab1a8419b2179c14a210bd0c60076d0236fc9ef762f270ee5dea60e78de572e465e"
			+ "4a9e5bce80ceafbef23f352f875fdd4033708600d0711b9152817ed19b31aa21a4e18df166813700f480a94058b0077fafdf5bc"
			+ "f118eccae967e6b4e520e472e505e65de907ec58f065f539fa52ffd604fb0a280ecf12e4162818ee1a411a9519f51854159c122"
			+ "f0dea090d03e9fe89f936f3f8efa0eb5ce86de5f5e4a3e450e53fe707e9d1ed82f1edf6bbfc0b015406cb0bc1103c140e16f900"
			+ "001ab0e42d00000000000000000000000000002632000007028a512a64";
			
	private DataHead dataHead;
	private float[] data;
	private int[] stringdata;
	private int[] waveformTypeA;
	private int[] waveformTypeB;
	private int temperature;
	private int electric;
	private int checkCode;
	
	public DataAnalysis() {
		// TODO Auto-generated constructor stub
		getResult(str);
	}
	
	//电压换算为加速度的算法常亮
	int mstandardMv=30;
	int macceleratedSpeed=10;
	int mVmv=2500;
	String StrZearo="0000";
	String Str7f="7fff";
	String Str80="8000";
	String Str4f="ffff";
	
	//获取得实际加速度数值
	float getVoValue(String StrHex){
		float value =0f;
		/*if(StrHex.compareTo(StrZearo)>=0 && StrHex.compareTo(Str7f)<=0){
			value=Integer.valueOf(StrHex,16)+mVmv;
		}
		
		
		if(StrHex.compareTo(Str80)>=0 && StrHex.compareTo(Str4f)<=0){
			value=Integer.valueOf(StrHex,16)-mVmv;
		}*/
		value = (float)Integer.valueOf(StrHex,16);
		if(value>50000) value = value-65536;
		value= (value/65536)*5000;
		
		value = value*macceleratedSpeed/mstandardMv;
		
		return value;
	}
	
	public void getResult(String str) {
		// TODO Auto-generated constructor stub
		//this.str = str;
		//String str = str1 +str2;
		Log.i(TAG, "str.length() = "+str.length());
		Log.i(TAG, "analysis str = "+str.toString());
		dataHead = new DataHead(str.substring(0, 20));
		waveformTypeA = new int[3];
		waveformTypeB = new int[3];
		
		data = new float[dataHead.dataNum];
		stringdata = new int[dataHead.dataNum];
		int j=0;
		for(int i=0;i<dataHead.dataNum*4;i+=4){
		
			//data[j] = ((float)Integer.valueOf(str.substring(i+20, i+24),16)/65536)*5000;
			data[j] = getVoValue(str.substring(i+20, i+24));
			stringdata[j] = Integer.valueOf(str.substring(i+20, i+24),16);
			j=j+1;
			//Log.i(TAG, "data["+j+"] = "+data[j] + ", 0X ="+str.substring(i+20, i+24));
			
		}
		
		waveformTypeA[0] = Integer.valueOf(str.substring(str.length()-60, str.length()-58),16);
		Log.i(TAG, "waveformTypeA[0] = "+waveformTypeA[0] + ", 0X ="+str.substring(str.length()-60, str.length()-58));
		waveformTypeA[1] = Integer.valueOf(str.substring(str.length()-58, str.length()-50),16);
		Log.i(TAG, "waveformTypeA[1] = "+waveformTypeA[1] + ", 0X ="+str.substring(str.length()-58, str.length()-50));
		//waveformTypeA[2] = Integer.valueOf(str.substring(str.length()-50, str.length()-42),16);
		Log.i(TAG, "waveformTypeA[2] = "+waveformTypeA[2] + ", 0X ="+str.substring(str.length()-50, str.length()-42));
		
		waveformTypeB[0] = Integer.valueOf(str.substring(str.length()-42, str.length()-40),16);
		Log.i(TAG, "waveformTypeB[0] = "+waveformTypeB[0] + ", 0X ="+str.substring(str.length()-42, str.length()-40));
		waveformTypeB[1] = Integer.valueOf(str.substring(str.length()-40, str.length()-32),16);
		Log.i(TAG, "waveformTypeB[1] = "+waveformTypeB[1] + ", 0X ="+str.substring(str.length()-40, str.length()-32));
		waveformTypeB[2] = Integer.valueOf(str.substring(str.length()-32, str.length()-24),16);
		Log.i(TAG, "waveformTypeB[2] = "+waveformTypeB[2] + ", 0X ="+str.substring(str.length()-32, str.length()-24));
		
		temperature = Integer.valueOf(str.substring(str.length()-24, str.length()-16),16);
		Log.i(TAG, "temperature = "+temperature + ", 0X ="+str.substring(str.length()-24, str.length()-16));
		electric = Integer.valueOf(str.substring(str.length()-16, str.length()-8),16);
		Log.i(TAG, "electric = "+electric + ", 0X ="+str.substring(str.length()-16, str.length()-8));
		//checkCode  = Integer.valueOf(str.substring(str.length()-8, str.length()),16);
		Log.i(TAG, "checkCode = "+checkCode + ", 0X ="+str.substring(str.length()-8, str.length()));
	}
	
	public float[] getData() {
		return data;
	}
	
}
