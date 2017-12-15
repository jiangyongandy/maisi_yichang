package org.cocos2dx.lua.service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 功能
 * Created by Jiang on 2017/11/6.
 */

public class NullResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        if(value == null)
            return "";
        else
            return null;
    }
}
