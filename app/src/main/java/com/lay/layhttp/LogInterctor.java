package com.lay.layhttp;

import com.lay.http.Intercetor;
import com.lay.http.core.PostResponse;
import com.lay.http.core.RequestBody;
import com.lay.http.core.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019-11-04.
 */

public class LogInterctor implements Intercetor {
    @Override
    public ResponseBody intercetor(RequestBody request) {
        System.out.println("URL：" + request.url);
        System.out.println("####################request####################");
        for(Map.Entry<String, Object> entry : request.getHeaders().entrySet()){
            System.out.println("header:\t" + entry.getKey() + ":" + entry.getValue());
        }
        for(Map.Entry<String, Object> entry : request.getParams().entrySet()){
            System.out.println("param:\t" + entry.getKey() + ":" + entry.getValue());
        }
        ResponseBody responseBody = request.process();
        System.out.println("####################response####################");
        if(responseBody.getHeader() != null){
            for(Map.Entry<String, List<String>> entry : responseBody.getHeader().entrySet()){
                List<String> list = entry.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append("header:\t").append(entry.getKey()).append(":");
                for(String s : list){
                    sb.append(s);
                }
                System.out.println(sb.toString());
            }
        }

        if(responseBody instanceof PostResponse) {
            System.out.println("接收数据:\t" + (responseBody.getData()));
        }
        return responseBody;
    }
}
