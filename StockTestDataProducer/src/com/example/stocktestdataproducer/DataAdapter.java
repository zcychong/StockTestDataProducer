package com.example.stocktestdataproducer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by zcy on 18-9-3.
 */

public class DataAdapter extends BaseAdapter {
    private final String TAG = "Stock-Test-Data" + getClass().getSimpleName();

    private String[] detailInfos;
    private Context context;
    private MainActivity.CheckedCountCallBack callBack;
    public int checkedCount=0;
    private CheckBox[] checkBoxes;
    public DataAdapter(Context activity, String[] item, MainActivity.CheckedCountCallBack countCallBack){
        context = activity;
        detailInfos = item;
        checkBoxes = new CheckBox[detailInfos.length];
        callBack = countCallBack;
        Log.e(TAG, "detailInfos.length=" + detailInfos.length);
    }

    @Override
    public int getCount() {
        return detailInfos.length;
    }

    @Override
    public Object getItem(int position) {
        return detailInfos[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_data, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = convertView.findViewById(R.id.cb_item);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.checkBox.setText(detailInfos[position]);
        checkBoxes[position] = viewHolder.checkBox;
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callBack.onChange(position, isChecked);
            }
        });



        convertView.setTag(viewHolder);

        return convertView;
    }

    class ViewHolder{
        private CheckBox checkBox;
    }
}
