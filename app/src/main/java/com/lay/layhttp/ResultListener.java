package com.lay.layhttp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2019-07-29.
 */

public abstract class ResultListener<T> {
    Context context;
    public ResultListener(Context context){
        this.context = context;
    }

    public abstract void success(T response);
    public void error(Throwable e){
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
