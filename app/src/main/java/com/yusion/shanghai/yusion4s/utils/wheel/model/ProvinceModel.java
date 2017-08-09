package com.yusion.shanghai.yusion4s.utils.wheel.model;

import java.util.List;

public class ProvinceModel {
    public String name;
    public int code;
    public List<CityModel> cityList;

    @Override
    public String toString() {
        return name;
    }

}
