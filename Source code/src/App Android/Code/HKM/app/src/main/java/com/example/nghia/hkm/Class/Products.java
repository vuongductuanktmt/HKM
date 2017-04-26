package com.example.nghia.hkm.Class;

import java.util.ArrayList;

/**
 * Created by huu21 on 25/04/2017.
 */

public class Products extends Event{
    private String __CurrentPrice__;
    private String __OldPrice__;
    private String __Status__;
    private ArrayList<Category> __Category__ = new ArrayList<>();

    public Products(String __Title__, String __LinkTitle__, String __HomePage__, String __LinkImage__, long __ViewCount__,
                    long __LoveCount__, String __CurrentPrice__, String __OldPrice__, java.util.Date __DateInsert__,
                    boolean __Delete__,String __Status__, ArrayList<Category> __Category__) {
        super(__Title__, __LinkTitle__, __HomePage__, __LinkImage__, __ViewCount__, __LoveCount__, __DateInsert__,
                __Delete__);
        this.__CurrentPrice__ = __CurrentPrice__;
        this.__OldPrice__ = __OldPrice__;
        this.__Category__ = __Category__;
        this.__Status__=__Status__;
    }

    public String get__OldPrice__() {
        return __OldPrice__;
    }

    public void set__OldPrice__(String __OldPrice__) {
        this.__OldPrice__ = __OldPrice__;
    }

    public String get__CurrentPrice__() {
        return __CurrentPrice__;
    }

    public void set__CurrentPrice__(String __CurrentPrice__) {
        this.__CurrentPrice__ = __CurrentPrice__;
    }

    public ArrayList<Category> get__Category__() {
        return __Category__;
    }

    public void set__Category__(ArrayList<Category> __Category__) {
        this.__Category__ = __Category__;
    }

    public String get__Status__() {
        return __Status__;
    }

    public void set__Status__(String __Status__) {
        this.__Status__ = __Status__;
    }
}