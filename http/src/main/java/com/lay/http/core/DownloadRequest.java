package com.lay.http.core;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2019-11-13.
 */

public class DownloadRequest extends RequestBody {
    public DownloadRequest(String url) {
        super(url);
    }

    @Override
    public ResponseBody send() {
        DownloadResponse responseBody = new DownloadResponse();
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
            connection.connect();
            responseBody.setReaponseCode(connection.getResponseCode());
            responseBody.setHeader(connection.getHeaderFields());
            inputStream = connection.getInputStream();
            responseBody.handle(inputStream, new ResponseBody.ProcessListener() {
                @Override
                public void process(final long current, final long totle) {
                    if(getH() != null) {
                        getH().post(new Runnable() {
                            @Override
                            public void run() {
                                if(responseListener != null){
                                    responseListener.process(current, totle);
                                }
                            }
                        });
                    } else {
                        if(responseListener != null){
                            responseListener.process(current, totle);
                        }
                    }
                }
            });
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

    class ProcessRunnable implements Runnable {
        public long current = 0;
        public long totle = 0;

        public void setCurrent(long current) {
            this.current = current;
        }

        public void setTotle(long totle) {
            this.totle = totle;
        }

        @Override
        public void run() {

        }
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
