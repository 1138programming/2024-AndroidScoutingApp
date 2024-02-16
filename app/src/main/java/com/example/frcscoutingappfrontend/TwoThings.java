package com.example.frcscoutingappfrontend;

public class TwoThings<Index,Timestamp> {
    public Index index;
    public Timestamp timestamp;
    public TwoThings(Index index, Timestamp timestamp) {
        this.index = index;
        this.timestamp = timestamp;
    }
}
