package com.lay.http.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019-11-01.
 */

public class ResponseBody {
    private Object data = "";
    private Map<String, List<String>> header;

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public int getReaponseCode() {
        return reaponseCode;
    }

    public void setReaponseCode(int reaponseCode) {
        this.reaponseCode = reaponseCode;
    }

    public int reaponseCode = 0;

    public void handle(InputStream inputStream){

    }

    public void handle(InputStream inputStream, ProcessListener listener){

    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T extends Object>T getData(){
        return (T)data;
    }

    public static interface ProcessListener{
        void process(long current, long totle);
    }
}
