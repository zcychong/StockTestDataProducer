package com.example.stocktestdataproducer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSONWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity{
	private final String TAG = "Stock-Test-Data" + getClass().getSimpleName();
	private Context context;

	private RelativeLayout rlSSE, rlPVolume, rlVolume;
	private LinearLayout llSseItems, llPVItems, llVolumeItems;
	private ImageView ivSseFold, ivPVFold, ivVolumeFold;

//	private CheckBox sse_big_data, sse_smal_Box, sse_zero_Box, sse_more_Box, sse_no_data_Box, sse_error_data_Box, sse_volume_Box, sse_price_volume_Box;
//	private CheckBox pv_big_data, pv_smal_data, pv_big_to_zero_data, pv_no_data, pv_smal_to_more_data, pv_big_to_more_data;
//	private CheckBox volume_big_data, volume_smal_data, volume_big_to_zero_data, volume_no_data, volume_smal_to_more_data, volume_big_to_more_data;
	private ListViewForScrollView lvSse, lvPv, lvVolume;
	private DataAdapter sseAdapter, pvAdapter, volumeAdapter;

//	private ArrayList<CheckBox> sseCbList = new ArrayList<>();
//	private ArrayList<CheckBox> pvCbList = new ArrayList<>();
//	private ArrayList<CheckBox> volumeCbList = new ArrayList<>();

	private ArrayList<Integer> sseStates = new ArrayList<Integer>();
	private ArrayList<Integer> pvStates = new ArrayList<Integer>();
	private ArrayList<Integer> volumeStates = new ArrayList<Integer>();

	private TextView tvSseCount, tvPvCount, tvVolumeCount;
	private int sseCount = 0, pvCount = 0, volumeCount = 0;

	private Button btnBegin, btnEnd;
	private int runningState = 0; //0-结束 1-运行 2-停止
	private Timer timer;
	private TimerTask timerTask;

	private EditText etCodeNum, etSseValue, etPvValue, etVolumeValue, etTimeInterval, etWeight, etLeateTime;

	private float sseValue;
	private long pvValue, volumeValue;

	private float sseNewValue, pvNewValue, volumeNewValue;

	private String codeNum;
	private int timeInterval, weight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;


		initUi();
		init();

	}

	private void init() {
		btnBegin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(runningState == 0){// 0->1
					btnBegin.setText("暂停");

					produceDataInit();
					timerTask = new TimerTask() {
						@Override
						public void run() {
							produceData();
						}
					};
					timer = new Timer();
					timer.schedule(timerTask, 0, timeInterval*1000);
					runningState = 1;
				}else if(runningState == 1){
					btnBegin.setText("继续");
					timer.cancel();
					timer.purge();
					timer = null;
					timerTask = null;
					runningState = 2;

					btnEnd.setVisibility(View.VISIBLE);
				}else{
					btnBegin.setText("暂停");
					timer = new Timer();
					timerTask = new TimerTask() {
						@Override
						public void run() {
							produceData();
						}
					};
					timer.schedule(timerTask, 0, timeInterval*1000);
					runningState = 1;
					btnEnd.setVisibility(View.INVISIBLE);
				}


			}
		});

		btnEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnEnd.setVisibility(View.INVISIBLE);
				btnBegin.setText("开始");
				runningState = 0;
			}
		});
	}

	private void produceDataInit() {
		codeNum = etCodeNum.getText().toString();
		sseValue = Float.valueOf(etSseValue.getText().toString());
		pvValue = Long.valueOf(etPvValue.getText().toString());
		volumeValue = Long.valueOf(etVolumeValue.getText().toString());
		timeInterval = Integer.valueOf(etTimeInterval.getText().toString());
		weight = Integer.valueOf(etWeight.getText().toString());

		SseProduce.getInstance().init(sseStates, sseValue, weight);
		PvProduce.getInstance().init(pvStates, pvValue, weight);
		VolumeProduce.getInstance().init(volumeStates, volumeValue, weight);
	}

//	private void initList(ArrayList<CheckBox> cbList, ArrayList<Integer> states) {
//		states.clear();
//		for(int i=0; i<cbList.size(); i++){
//			if(cbList.get(i).isChecked()){
//				states.add(i);
//			}
//		}
//	}

	private void produceData() {
		Log.e(TAG, "sseStates.size() = " + sseStates.size());
		sseNewValue = SseProduce.getInstance().produceData();
		pvNewValue = PvProduce.getInstance().produceData();
		volumeNewValue = VolumeProduce.getInstance().produceData();
		String leateTime = etLeateTime.getText().toString();

		hookFunction(sseNewValue, pvNewValue, volumeNewValue, leateTime);
	}

	private void hookFunction(float sse, float pv, float volume, String leateTime){
		Log.e(TAG, "sseNewValue = " + sseNewValue);
		Log.e(TAG, "pvNewValue = " + pvNewValue);
		Log.e(TAG, "volumeNewValue = " + volumeNewValue);
	}

	private void initUi() {
		rlSSE = findViewById(R.id.rl_sse);
		rlPVolume = findViewById(R.id.rl_pv);
		rlVolume = findViewById(R.id.rl_volume);

		llSseItems = findViewById(R.id.ll_sse_items);
		llPVItems = findViewById(R.id.ll_pv_items);
		llVolumeItems = findViewById(R.id.ll_v_items);

		ivSseFold = findViewById(R.id.iv_sse_fold);
		ivPVFold = findViewById(R.id.iv_prive_volume_fold);
		ivVolumeFold = findViewById(R.id.iv_volume_fold);

		tvSseCount = findViewById(R.id.tv_sse_count);
		tvPvCount = findViewById(R.id.tv_pv_count);
		tvVolumeCount = findViewById(R.id.tv_volume_count);

		btnBegin = findViewById(R.id.btn_begin);
		btnEnd = findViewById(R.id.btn_end);

		lvSse = findViewById(R.id.lv_sse_items);
		lvPv = findViewById(R.id.lv_pv_items);
		lvVolume = findViewById(R.id.lv_volume_items);

		rlSSE.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HiddenAnimUtils.getInstance().toggle(context,llSseItems,ivSseFold);
			}
		});

		rlPVolume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HiddenAnimUtils.getInstance().toggle(context,llPVItems,ivPVFold);
			}
		});

		rlVolume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HiddenAnimUtils.getInstance().toggle(context,llVolumeItems,ivVolumeFold);
			}
		});


		
		sseAdapter = new DataAdapter(context, getResources().getStringArray(R.array.sse_err_array), new CheckedCountCallBack() {
			@Override
			public void onChange(int position, boolean isChecked) {
				if(isChecked){
					sseStates.add(position);
					tvSseCount.setText("(" + ++sseCount + ")");
				}else{
					sseStates.remove((Object)position);
					tvSseCount.setText("(" + --sseCount + ")");
				}

			}
		});
		pvAdapter = new DataAdapter(context, getResources().getStringArray(R.array.pv_err_array), new CheckedCountCallBack() {
			@Override
			public void onChange(int position, boolean isChecked) {
				if(isChecked){
					pvStates.add(position);
					tvPvCount.setText("(" + ++pvCount + ")");
				}else{
					pvStates.remove((Object)position);
					tvPvCount.setText("(" + --pvCount + ")");
				}

			}
		});
		volumeAdapter = new DataAdapter(context, getResources().getStringArray(R.array.volume_err_array), new CheckedCountCallBack() {
			@Override
			public void onChange(int position, boolean isChecked) {
				if(isChecked){
					volumeStates.add(position);
					tvVolumeCount.setText("(" + ++volumeCount + ")");
				}else{
					volumeStates.remove((Object)position);
					tvVolumeCount.setText("(" + --volumeCount + ")");
				}

			}
		});

		lvSse.setAdapter(sseAdapter);
		lvPv.setAdapter(pvAdapter);
		lvVolume.setAdapter(volumeAdapter);


		initSse();
		initPv();
		initVolume();

		etCodeNum = findViewById(R.id.et_code_num);
		etSseValue = findViewById(R.id.et_sse);
		etPvValue = findViewById(R.id.et_price_volume);
		etVolumeValue = findViewById(R.id.et_volume);
		etTimeInterval = findViewById(R.id.et_time_interval);
		etWeight = findViewById(R.id.et_weight);
		etLeateTime = findViewById(R.id.et_leate_time);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
		saveData();
	}

	private void saveData() {
		FileOutputStream file;
		try {
			file = new FileOutputStream("/data/local/tmp/");

			ConfigData configData = new ConfigData();
			configData.setTimeInterval(timeInterval);
			configData.setWeight(weight);

			ItemInfo sseInfo = new ItemInfo();
			ItemInfo pvInfo = new ItemInfo();
			ItemInfo volumeInfo = new ItemInfo();

			sseInfo.setValue(sseValue);
			pvInfo.setValue(pvValue);
			volumeInfo.setValue(volumeValue);

			configData.setSseInfo(sseInfo);
			configData.setPvInfo(pvInfo);
			configData.setVolumeInfo(volumeInfo);

			JSONWriter(OutputStreamWriter(file, "utf-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initVolume() {
//		volume_big_data = findViewById(R.id.volume_big_data);
//		volume_smal_data = findViewById(R.id.volume_smal_data);
//		volume_big_to_zero_data = findViewById(R.id.volume_big_to_zero_data);
//		volume_no_data = findViewById(R.id.volume_no_data);
//		volume_smal_to_more_data = findViewById(R.id.volume_smal_to_more_data);
//		volume_big_to_more_data = findViewById(R.id.volume_big_to_more_data);
//
//		volumeCbList.add(volume_big_data);
//		volumeCbList.add(volume_smal_data);
//		volumeCbList.add(volume_big_to_zero_data);
//		volumeCbList.add(volume_no_data);
//		volumeCbList.add(volume_smal_to_more_data);
//		volumeCbList.add(volume_big_to_more_data);
//
//		volume_big_data.setOnCheckedChangeListener(this);
//		volume_smal_data.setOnCheckedChangeListener(this);
//		volume_big_to_zero_data.setOnCheckedChangeListener(this);
//		volume_no_data.setOnCheckedChangeListener(this);
//		volume_smal_to_more_data.setOnCheckedChangeListener(this);
//		volume_big_to_more_data.setOnCheckedChangeListener(this);
	}

	private void initPv() {
//		pv_big_data = findViewById(R.id.pv_big_data);
//		pv_smal_data = findViewById(R.id.pv_smal_data);
//		pv_big_to_zero_data = findViewById(R.id.pv_big_to_zero_data);
//		pv_no_data = findViewById(R.id.pv_no_data);
//		pv_smal_to_more_data = findViewById(R.id.pv_smal_to_more_data);
//		pv_big_to_more_data = findViewById(R.id.pv_big_to_more_data);
//
//		pvCbList.add(pv_big_data);
//		pvCbList.add(pv_smal_data);
//		pvCbList.add(pv_big_to_zero_data);
//		pvCbList.add(pv_no_data);
//		pvCbList.add(pv_smal_to_more_data);
//		pvCbList.add(pv_big_to_more_data);
//
//		pv_big_data.setOnCheckedChangeListener(this);
//		pv_smal_data.setOnCheckedChangeListener(this);
//		pv_big_to_zero_data.setOnCheckedChangeListener(this);
//		pv_no_data.setOnCheckedChangeListener(this);
//		pv_smal_to_more_data.setOnCheckedChangeListener(this);
//		pv_big_to_more_data.setOnCheckedChangeListener(this);
	}

	private void initSse() {
//		sse_big_data = findViewById(R.id.sse_big_data);
//		sse_smal_Box = findViewById(R.id.sse_smal_Box);
//		sse_zero_Box = findViewById(R.id.sse_zero_Box);
//		sse_more_Box = findViewById(R.id.sse_more_Box);
//		sse_no_data_Box = findViewById(R.id.sse_no_data_Box);
//		sse_error_data_Box = findViewById(R.id.sse_error_data_Box);
//		sse_volume_Box = findViewById(R.id.sse_volume_Box);
//		sse_price_volume_Box = findViewById(R.id.sse_price_volume_Box);
//
//		sseCbList.add(sse_big_data);
//		sseCbList.add(sse_smal_Box);
//		sseCbList.add(sse_zero_Box);
//		sseCbList.add(sse_more_Box);
//		sseCbList.add(sse_no_data_Box);
//		sseCbList.add(sse_error_data_Box);
//		sseCbList.add(sse_volume_Box);
//		sseCbList.add(sse_price_volume_Box);
//
//		sse_big_data.setOnCheckedChangeListener(this);
//		sse_smal_Box.setOnCheckedChangeListener(this);
//		sse_zero_Box.setOnCheckedChangeListener(this);
//		sse_more_Box.setOnCheckedChangeListener(this);
//		sse_no_data_Box.setOnCheckedChangeListener(this);
//		sse_error_data_Box.setOnCheckedChangeListener(this);
//		sse_volume_Box.setOnCheckedChangeListener(this);
//		sse_price_volume_Box.setOnCheckedChangeListener(this);

	}

	public interface CheckedCountCallBack {
		void onChange(int position, boolean isChecked);
	}

//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		switch (buttonView.getId()){
//			case R.id.sse_big_data:
//			case R.id.sse_smal_Box:
//			case R.id.sse_zero_Box:
//			case R.id.sse_more_Box:
//			case R.id.sse_no_data_Box:
//			case R.id.sse_error_data_Box:
//			case R.id.sse_volume_Box:
//			case R.id.sse_price_volume_Box:
//				chengeSseCount();
//				break;
//			case R.id.pv_big_data:
//			case R.id.pv_smal_data:
//			case R.id.pv_big_to_zero_data:
//			case R.id.pv_no_data:
//			case R.id.pv_smal_to_more_data:
//			case R.id.pv_big_to_more_data:
//				chengePvCount();
//				break;
//			case R.id.volume_big_data:
//			case R.id.volume_smal_data:
//			case R.id.volume_big_to_zero_data:
//			case R.id.volume_no_data:
//			case R.id.volume_smal_to_more_data:
//			case R.id.volume_big_to_more_data:
//				chengeVolumeCount();
//				break;
//
//		}
//
//	}

//	private void chengeSseCount() {
//		sseCount = 0;
//		for(CheckBox checkBox : sseCbList){
//			if(checkBox.isChecked()){
//				sseCount++;
//			}
//		}
//		tvSseCount.setText("("+sseCount+")");
//	}
//	private void chengePvCount() {
//		pvCount = 0;
//		for(CheckBox checkBox : pvCbList){
//			if(checkBox.isChecked()){
//				pvCount++;
//			}
//		}
//		tvPvCount.setText("("+pvCount+")");
//	}private void chengeVolumeCount() {
//		volumeCount = 0;
//		for(CheckBox checkBox : volumeCbList){
//			if(checkBox.isChecked()){
//				volumeCount++;
//			}
//		}
//		tvVolumeCount.setText("("+volumeCount+")");
//	}

}
