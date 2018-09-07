package com.example.stocktestdataproducer;

import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by zcy on 18-8-29.
 */

public interface ProduceInterface {
    ProduceInterface init(ArrayList<Integer> list, float value, int weight);
    float produceData();
    float produceNormalData();
    float produceErrorData(int index);
}
