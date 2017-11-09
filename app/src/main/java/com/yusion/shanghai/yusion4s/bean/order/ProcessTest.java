package com.yusion.shanghai.yusion4s.bean.order;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/11/9.
 */

public class ProcessTest {

    public String title;
    public String title2;
    public String time;
    public String time2;
    public List<ProcessTest> processTestList = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
