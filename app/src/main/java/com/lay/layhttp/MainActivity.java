package com.lay.layhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lay.http.core.DownloadRequest;
import com.lay.http.core.DownloadResponse;
import com.lay.http.NetClient;
import com.lay.http.core.PostRequest;
import com.lay.http.core.PostResponse;
import com.lay.http.core.RequestBody;
import com.lay.http.ResponseListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NetClient client = NetClient.getClient();
        client.init();
        client.addIntercetor(new LogInterctor());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        RequestBody requestBody = client.newDownloadRequest("https://www.baidu.com/img/bd_logo1.png")
                PostRequest requestBody = new PostRequest("http://192.168.1.100:8081/ybpht/sendsms")
//                GetRequest requestBody = new GetRequest("https://www.baidu.com/")
//                GetRequest requestBody = new GetRequest("http://112.74.161.79:8080/api/base/showMsg")
                        .param("mobile", "13802759304")
//                        .param("password", "13802759304")
                                .callback(new ResponseListener<PostResponse>() {
                                    @Override
                                    public void response(final PostResponse responseBody) {

                                    }
                                });
                        client.sendAsync(requestBody);
//                    }
//                }).start();

            }
        });

    }
}