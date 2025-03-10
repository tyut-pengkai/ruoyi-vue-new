package com.kekecha.xiantu.domain;

public class HistoryCount {
    long timestamp;
    int count;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "HistoryCount{" +
                "timestamp=" + timestamp +
                ", count=" + count +
                '}';
    }
}
