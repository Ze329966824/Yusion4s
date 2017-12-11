package com.yusion.shanghai.yusion4s.bean.order;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/11/9.
 */

public class ProcessTest {
    public String st;
    public String title;
    public String time;
    public List<ProcessTest> seriesList = new ArrayList<>();
    public List<ProcessTest> parallelList = new ArrayList<>();

    public List<ProcessTest> asyncProcessTestList = new ArrayList<>();
    //seriesList
//    //一块
    public List<ProcessTest> syncProcessTestList = new ArrayList<>();
    //parallelList


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
