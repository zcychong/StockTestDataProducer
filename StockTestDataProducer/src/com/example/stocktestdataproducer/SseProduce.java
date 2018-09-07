package com.example.stocktestdataproducer;

import android.util.Log;
import android.widget.CheckBox;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zcy on 18-8-29.
 */

public class SseProduce implements ProduceInterface {
    private final String TAG = "Stock-Test-Data" + getClass().getSimpleName();
    private static SseProduce instance;
    private ArrayList<Integer> mList;
    private float mValue = 0;
    private int mWeight = 0;
    public static SseProduce getInstance(){
        if(instance == null){
            instance = new SseProduce();
        }
        return instance;
    }


//    @Override
//    public long produceData(ArrayList<CheckBox> list, long value) {
//
//        ArrayList<CheckBox> checkedList = new ArrayList<>();
//        for(CheckBox item : list){
//            if(item.isChecked()){
//                checkedList.add(item);
//            }
//        }
//
//        Random rand = new Random();
//        int i = rand.nextInt(100);
//
//        return 0;
//    }

    @Override
    public float produceErrorData(int index){
        Log.e(TAG, "index = " + index);
        float newValue = mValue;
        switch (index){
            case 0:
                newValue = mValue * 10;
                break;
            case 1:
                newValue = mValue / 10;
                break;
            case 2:
                newValue = 0;
                break;
        }
        return newValue;
    }

    @Override
    public SseProduce init(ArrayList<Integer> list, float value, int weight) {
        mList = list;
        mValue = value;
        mWeight = weight;
        return instance;
    }

    @Override
    public float produceData() {
        Random rand = new Random();
        int i = rand.nextInt(100);
        Log.e(TAG, "i=" + i);
        if(mList.size() == 0){
            return produceNormalData();
        }
        if(0 <= i && i < mWeight){
            return produceNormalData();
        }else{
            int average = (100 - mWeight)/(mList.size());
            Log.e(TAG, "average = " + average);
            int index = (i - mWeight)/average;
            Log.e(TAG, "index = " + index);
            return produceErrorData(index);
        }
    }

    @Override
    public float produceNormalData() {
        DecimalFormat df = new java.text.DecimalFormat("#.00");
        Random rand = new Random();
        float newValue = rand.nextFloat()*20;
        newValue = newValue - 10;
        newValue = Float.valueOf(df.format(newValue));
        mValue += newValue;
        return mValue;
    }
}
