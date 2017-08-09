package com.yusion.shanghai.yusion4s.utils.wheel.parser;


import com.yusion.shanghai.yusion4s.utils.wheel.model.CityModel;
import com.yusion.shanghai.yusion4s.utils.wheel.model.DistrictModel;
import com.yusion.shanghai.yusion4s.utils.wheel.model.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler extends DefaultHandler {

    /**
     * 存储所有的解析对象
     */
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

    public XmlParserHandler() {

    }

    public List<ProvinceModel> getDataList() {
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    ProvinceModel provinceModel = new ProvinceModel();
    CityModel cityModel = new CityModel();
    DistrictModel districtModel = new DistrictModel();

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            provinceModel = new ProvinceModel();
            provinceModel.name = attributes.getValue(0);
            provinceModel.code = Integer.parseInt(attributes.getValue(1));
            provinceModel.cityList = new ArrayList<>();
        } else if (qName.equals("city")) {
            cityModel = new CityModel();
            cityModel.name = attributes.getValue(0);
            cityModel.code = Integer.parseInt(attributes.getValue(1));
            cityModel.districtList = new ArrayList<>();
        } else if (qName.equals("district")) {
            districtModel = new DistrictModel();
            districtModel.name = attributes.getValue(0);
            districtModel.code = Integer.parseInt(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            cityModel.districtList.add(districtModel);
        } else if (qName.equals("city")) {
            provinceModel.cityList.add(cityModel);
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    }

}

