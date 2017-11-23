package com.fjutacm.exam.base.main;

/**
 * Created by sck on 2017/11/12.
 */
public abstract class CacheObj {
    public  boolean isupate=false;
    protected boolean isreadonly=false;

    public void write() throws Exception{
        if(isreadonly)
            throw new Exception("该对象为只读对象");
        if(isupate){
            writeWay();
            isupate=false;
        }
    }
    public void read(){
        readWay();
        isupate=false;
    }
    protected abstract void writeWay();
    protected abstract void readWay();
}
