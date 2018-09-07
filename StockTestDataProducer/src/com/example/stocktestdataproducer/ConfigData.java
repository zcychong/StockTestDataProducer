package com.example.stocktestdataproducer;

/**
 * Created by zcy on 18-9-6.
 */

public class ConfigData {
    private String stockName;
    private String stockCode;

    private ItemInfo sseInfo;
    private ItemInfo pvInfo;
    private ItemInfo volumeInfo;

    private int timeInterval;
    private int weight;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public ItemInfo getSseInfo() {
        return sseInfo;
    }

    public void setSseInfo(ItemInfo sseInfo) {
        this.sseInfo = sseInfo;
    }

    public ItemInfo getPvInfo() {
        return pvInfo;
    }

    public void setPvInfo(ItemInfo pvInfo) {
        this.pvInfo = pvInfo;
    }

    public ItemInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(ItemInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}



class ItemInfo{
    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float sseValue) {
        this.value = sseValue;
    }
}


