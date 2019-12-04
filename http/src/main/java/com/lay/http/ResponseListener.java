package com.lay.http;

import com.lay.http.core.ResponseBody;

/**
 * Created by Administrator on 2019-11-04.
 */

public abstract class ResponseListener<T extends ResponseBody> {
    public abstract void response(T responseBody);
    public void process(long current, long totle){

    }
}
