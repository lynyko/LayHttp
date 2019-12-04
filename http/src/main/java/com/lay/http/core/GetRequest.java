package com.lay.http.core;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class GetRequest extends RequestBody {
    public GetRequest(String url) {
        super(url);
    }

    @Override
    public ResponseBody send() {
        GetResponse responseBody = new GetResponse();
        BufferedWriter writer = null;
        InputStream inputStream = null;
        try {
            String p = getParamString();
            HttpURLConnection connection = null;
            if(!TextUtils.isEmpty(p)){
                connection = (HttpURLConnection)new URL(url + "?" + p).openConnection();
            } else {
                connection = (HttpURLConnection)new URL(url).openConnection();
            }
            connection.setDoInput(true);
            connection.setConnectTimeout(getConnectTimeout());
            connection.setReadTimeout(getReadTimeout());
            connection.setRequestMethod("GET");
            if(getHeaders().get("Connection") != null){
                connection.setRequestProperty("Connection", getHeaders().get("Connection").toString());
            } else {
                connection.setRequestProperty("Connection", "Keep-Alive");
            }
            if(getHeaders().get("Charset") != null){
                connection.setRequestProperty("Charset", getHeaders().get("Charset").toString());
            } else {
                connection.setRequestProperty("Charset", "UTF-8");
            }
            if(getHeaders().get("Content-Type") != null){
                connection.setRequestProperty("Content-Type", getHeaders().get("Content-Type").toString());
            } else {
                connection.setRequestProperty("Content-Type", "application/json");
            }

            connection.connect();
            responseBody.setReaponseCode(connection.getResponseCode());
            responseBody.setHeader(connection.getHeaderFields());
            inputStream = connection.getInputStream();
            responseBody.handle(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseBody;
    }

    private String getParamString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Object> entry : getParams().entrySet()){
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        if(sb.length() > 0){
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }
}
