package com.dhq.mytalk;

import com.dhq.mytalk.response.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class ParseUtil {

    private static String result="{\"code\":\"200\",\"msg\":\"success\",\"data\":{\"name\":\"用户名\",\"gender\":\"男\"}}";

    public <T> BaseResponse<T> parseJson(CallBack<T> callBack ) {
        Gson gson = new Gson();
        Type jsonType = new TypeToken<BaseResponse<T>>() {
        }.getType();
        BaseResponse<T> response=gson.fromJson(result, jsonType);
        callBack.success(response.getData());
        return gson.fromJson(result, jsonType);
//        return null;
    }


    public static <T> void parseFromJson(Class<T> pClass,CallBack<T> callBack){
        Gson gson = new Gson();
//        BaseResponse<T> bean = gson.fromJson(result, pClass);
        Type jsonType = new TypeToken<BaseResponse<T>>() {
        }.getType();
        BaseResponse<T> response = gson.fromJson(result, jsonType);
        callBack.success(response.getData());

    }

    public interface CallBack<T>{
        void success(T data);
        void fail();
    }
}
