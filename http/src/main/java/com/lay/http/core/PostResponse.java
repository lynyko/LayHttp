package com.lay.http.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2019-11-04.
 */

public class PostResponse<T> extends ResponseBody {

    @Override
    public void handle(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
            if(sb.length() > 0){
                sb.deleteCharAt(sb.length()-1);
            }
            setData(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
