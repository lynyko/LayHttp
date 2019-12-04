package com.lay.http;

import com.lay.http.core.RequestBody;
import com.lay.http.core.ResponseBody;

public interface Intercetor {
    public ResponseBody intercetor(RequestBody request);

}
