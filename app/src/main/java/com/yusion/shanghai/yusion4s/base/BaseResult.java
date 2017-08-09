package com.yusion.shanghai.yusion4s.base;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseResult<T> {
    public int code;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}