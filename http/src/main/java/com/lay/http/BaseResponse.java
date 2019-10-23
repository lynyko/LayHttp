package com.lay.http;


import com.google.gson.Gson;

/**
 * Created by Administrator on 2019-07-29.
 */

public class BaseResponse {
    public String resultCode = "0";
    public String resultMsg = "";
    public String count = "1";
    public boolean isSucceed(){
        return "0".equals(resultCode);
    }

    public <T extends BaseResponse> T parse(String result){
        return (T)new Gson().fromJson(result, getClass());
    }
}