package com.yusion.shanghai.yusion4s.base;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseResult<T> {
    public int code;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}