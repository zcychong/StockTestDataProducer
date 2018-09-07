package com.example.stocktestdataproducer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zcy on 18-9-5.
 */

public class VolumeProduce implements ProduceInterface {
    private final String TAG = "Stock-Test-Data" + getClass().getSimpleName();
    private static VolumeProduce instance;
    private ArrayList<Integer> mList;
    private float mValue;
    private int mWeight;
    public static VolumeProduce getInstance(){
        if(instance == null){
            instance = new VolumeProduce();
        }
        return instance;
    }

    @Override
    public ProduceInterface init(ArrayList<Integer> list, float value, int weight) {
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
        //        DecimalFormat df = new java.text.DecimalFormat("#.00");
        Random rand = new Random();
        float newValue = rand.nextInt(100000);
//        newValue = Float.valueOf(df.format(newValue));
        mValue += newValue;
        return mValue;
    }

    @Override
    public float produceErrorData(int index) {
        return mValue;
    }
}
