package com.lay.http.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2019-11-04.
 */

public class DownloadResponse extends ResponseBody {

    @Override
    public void handle(InputStream inputStream) {
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            setData(bos.toByteArray());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
