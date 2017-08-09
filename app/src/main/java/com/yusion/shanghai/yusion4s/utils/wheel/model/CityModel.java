package com.yusion.shanghai.yusion4s.utils.wheel.model;

import java.util.List;

public class CityModel {
    public String name;
    public int code;
    public List<DistrictModel> districtList;

    @Override
    public String toString() {
        return name;
    }

}
