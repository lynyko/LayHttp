package com.lay.http.core;

import android.os.Handler;

import com.lay.http.Intercetor;
import com.lay.http.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019-11-01.
 */

public abstract class RequestBody {
    protected List<Intercetor> intercetorList = new ArrayList<>();
    public ResponseListener responseListener = null;
    public String url;
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();
    private int connectTimeout = 0;
    private int readTimeout = 0;

    public Handler getH() {
        return H;
    }

    public void setH(Handler h) {
        H = h;
    }

    private Handler H = null;

    public <T extends RequestBody>T param(String key, String value){
        params.put(key, value);
        return (T)this;
    }

    public Map<String, Object> getParams(){
        return params;
    }

    public <T extends RequestBody>T header(String key, String value){
        headers.put(key, value);
        return (T)this;
    }

    public Map<String, Object> getHeaders(){
        return headers;
    }

    public RequestBody(String url){
        this.url = url;
    }

    public void setConnectTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        connectTimeout = timeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        readTimeout = timeout;
    }

    public <T extends RequestBody>T callback(ResponseListener responseListener){
        this.responseListener = responseListener;
        return (T)this;
    }

    public <T extends RequestBody>T addIntercetor(Intercetor intercetor){
        intercetorList.add(intercetor);
        return (T)this;
    }

    public ResponseBody process(){
        ResponseBody responseBody = null;
        if(!intercetorList.isEmpty()){
            Intercetor intercetor = intercetorList.remove(0);
            responseBody = intercetor.intercetor(this);
        } else {
            responseBody = send();
        }
        return responseBody;
    }

    public abstract ResponseBody send();
}
