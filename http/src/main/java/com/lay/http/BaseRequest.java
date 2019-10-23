package com.lay.http;

import android.os.Handler;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019-07-29.
 */

public abstract class BaseRequest {
    public static String TOKEN = "";
    private static Handler H = new Handler();
    protected List<Intercetor> intercetorList = new ArrayList<>();


    @Parameter
    public String token = "";

    private Class<? extends BaseResponse> responseClass;
    public String path = "";
    ResultListener listener;

    public BaseRequest(Class<? extends BaseResponse> rClass){
        responseClass = rClass;
    }

    public void send(ResultListener l){
        listener = l;
        token = TOKEN;
        String s = checkParams();
        if(!TextUtils.isEmpty(s)){
            if(listener != null){
                listener.error(new Exception(s));
            }
            return;
        }

        send();
    }

    public BaseRequest addIntercetor(Intercetor intercetor){
        intercetorList.add(intercetor);
        return this;
    }

    public abstract void send();

//    private void sendGet(){
//        NetClient.getClient().sendGet(this, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if(listener != null){
//                    listener.error(e);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String r = response.body().string();
//                handleResult(r);
//            }
//        });
//    }
//
//    private void sendPost(){
//        NetClient.getClient().sendPost(this, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if(listener != null){
//                    listener.error(e);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String r = response.body().string();
//                handleResult(r);
//            }
//        });
//    }

    public void handleResult(final String r){
        H.post(new Runnable() {
            @Override
            public void run() {
                try {
                    BaseResponse response = responseClass.newInstance().parse(r);
                    if(response.isSucceed()){
                        if(listener != null){
                            listener.success(response);
                        }
                    } else {
                        if(listener != null) {
                            listener.error(new Exception(response.resultMsg));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(listener != null) {
                        listener.error(new Exception("failed"));
                    }
                }
            }
        });
    }

    private String checkParams(){
        StringBuilder sb = new StringBuilder();
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            Parameter parameter = field.getAnnotation(Parameter.class);
            if (parameter != null) {
                field.setAccessible(true);
                try {
                    if("String".equals(field.getType().getSimpleName())) {
                        String value = (String) field.get(this);
                        if(TextUtils.isEmpty(value) && !parameter.nullable()) {
                            sb.append(field.getName()).append(" is empty,");
                        }

                        String regex = parameter.regex();
                        if(!TextUtils.isEmpty(regex) && !TextUtils.isEmpty(value)){
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(value);
                            if(!matcher.matches()){
                                sb.append(field.getName() + " = " + value + ":").append(" is not match （" + regex + "),");
                            }
                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
