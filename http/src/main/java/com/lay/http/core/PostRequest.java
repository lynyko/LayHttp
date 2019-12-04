package com.lay.http.core;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequest extends RequestBody {
    public PostRequest(String url) {
        super(url);
    }

    @Override
    public ResponseBody send() {
        PostResponse responseBody = new PostResponse();
        BufferedWriter writer = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL httpUrl = new URL(url);
            connection = (HttpURLConnection)httpUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(getConnectTimeout());
            connection.setReadTimeout(getReadTimeout());
            connection.setRequestMethod("POST");
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
            if(getParams() != null) {
                writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                String json = new Gson().toJson(getParams());
                writer.write(json);
                writer.flush();
            }
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
            if(connection != null){
                connection.disconnect();
            }
        }
        return responseBody;
    }
}
