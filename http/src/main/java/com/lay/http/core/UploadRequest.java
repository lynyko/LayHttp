package com.lay.http.core;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2019-11-18.
 */

public class UploadRequest extends RequestBody {
    private File uploadFile;

    public UploadRequest(String url) {
        super(url);
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    @Override
    public ResponseBody send() {
        if(uploadFile == null){
            throw new NullPointerException("file == null");
        }
        if(!uploadFile.exists()){
            throw new IllegalArgumentException("file is not exist");
        }
        PostResponse responseBody = new PostResponse();
        InputStream inputStream = null;
        OutputStream out = null;
        InputStream fileIn = null;
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
            out = connection.getOutputStream();
            fileIn = new FileInputStream(uploadFile);
            int size = 1024 * 1024;
            int len = 0;
            byte[] buffer = new byte[size];
            while(true){
                len = fileIn.read(buffer);
                if(len < 0){
                    fileIn.close();
                    break;
                }
                out.write(buffer, 0, len);
                out.flush();
            }

            responseBody.setReaponseCode(connection.getResponseCode());
            responseBody.setHeader(connection.getHeaderFields());
            inputStream = connection.getInputStream();
            responseBody.handle(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
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
