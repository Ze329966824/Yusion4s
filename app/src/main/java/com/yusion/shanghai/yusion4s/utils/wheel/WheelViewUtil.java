package com.yusion.shanghai.yusion4s.utils.wheel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.utils.InputMethodUtil;
import com.yusion.shanghai.yusion4s.utils.wheel.model.CityModel;
import com.yusion.shanghai.yusion4s.utils.wheel.model.DistrictModel;
import com.yusion.shanghai.yusion4s.utils.wheel.model.ProvinceModel;
import com.yusion.shanghai.yusion4s.utils.wheel.parser.XmlParserHandler;
import com.yusion.shanghai.yusion4s.widget.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by ice on 17/3/27.
 * ice is a big cow?
 */

public class WheelViewUtil {

    private static Dialog mWheelViewDialog = null;
    private static List<ProvinceModel> mProvinceList = null;
    private static int DEFAULT_OFFSET = 2;
    private static CityWheelViewUtil mCityWheelViewUtil;

    public interface OnSubmitCallBack {
        void onSubmitCallBack(View clickedView, int selectedIndex);
    }

    public interface OnCitySubmitCallBack {
        void onCitySubmitCallBack(View clickedView, String city);
    }

    private static <T> void showWheelView(final List<T> list, int selectedIndex, final TextView showView,
                                          final String title, final OnSubmitCallBack onSubmitCallBack) {
        showWheelView(list, selectedIndex, showView, showView, title, onSubmitCallBack);
    }

    public static <T> void showWheelView(final List<T> list, int selectedIndex, final View clickedView, final TextView showView,
                                         final String title, final OnSubmitCallBack onSubmitCallBack) {
        clickedView.setEnabled(false);

        Context context = clickedView.getContext();
        InputMethodUtil.hideInputMethod(context, clickedView);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(clickedView.getApplicationWindowToken(), 0);
        }

        //View wheelViewLayout = LayoutInflater.from(context).inflate(R.layout.wheel_view_layout, null);
        View wheelViewLayout = LayoutInflater.from(context).inflate(R.layout.wheel_view_layout, null);
        TextView textTitle = (TextView) wheelViewLayout.findViewById(R.id.select_title);
        textTitle.setText(title);

        final WheelView wv = (WheelView) wheelViewLayout.findViewById(R.id.wv);
        Button okBtn = (Button) wheelViewLayout.findViewById(R.id.select_ok);
        Button cancelBtn = (Button) wheelViewLayout.findViewById(R.id.select_cancel);

        wv.setOffset(2);
        wv.setItems(list);
        wv.setSelection(selectedIndex);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubmitCallBack != null) {
                    onSubmitCallBack.onSubmitCallBack(clickedView, wv.getSelectedIndex());
                }
                showView.setText(wv.getSelectedItem().toString());
                mWheelViewDialog.dismiss();
                mWheelViewDialog = null;
                clickedView.setEnabled(true);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWheelViewDialog.dismiss();
                mWheelViewDialog = null;
                clickedView.setEnabled(true);
            }
        });

        mWheelViewDialog = new Dialog(context, R.style.MyDialogStyle);
        mWheelViewDialog.setContentView(wheelViewLayout);
        mWheelViewDialog.setCanceledOnTouchOutside(false);
        mWheelViewDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clickedView.setEnabled(true);
            }
        });
//        mWheelViewDialog.setCanceledOnTouchOutside(true);
//        mWheelViewDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                mWheelViewDialog.dismiss();
//                mWheelViewDialog = null;
//            }
//     }
        mWheelViewDialog.getWindow().setWindowAnimations(R.style.dialogAnimationStyle);
        mWheelViewDialog.getWindow().setGravity(Gravity.BOTTOM);
        mWheelViewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWheelViewDialog.show();
    }

    private static void showCityWheelView(String tag, final TextView showView, String title, final OnCitySubmitCallBack onCitySubmitCallBack) {
        showCityWheelView(tag, showView, showView, title, onCitySubmitCallBack);
    }

    public static void showCityWheelView(String tag, final View clickedView, final TextView showView, String title, final OnCitySubmitCallBack onCitySubmitCallBack) {
        showCityWheelView(tag, clickedView, showView, title, onCitySubmitCallBack, null);
    }


    public static String currentCityJson = "";

    public static void showCityWheelView(String tag, final View clickedView, final TextView showView, String title, final OnCitySubmitCallBack onCitySubmitCallBack, String cityJson) {
        clickedView.setEnabled(false);
        if (mCityWheelViewUtil == null) {
            mCityWheelViewUtil = new CityWheelViewUtil();
        }
        final CityWheelViewUtil.CityObj cityObj = mCityWheelViewUtil.getCityObjByTag(tag);

        Context context = clickedView.getContext();
        InputMethodUtil.hideInputMethod(context, clickedView);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(clickedView.getApplicationWindowToken(), 0);
        }

        View wheelViewLayout = LayoutInflater.from(context).inflate(R.layout.city_wheel_view_layout, null);

        TextView textTitle = (TextView) wheelViewLayout.findViewById(R.id.select_title);
        textTitle.setText(title);

        final WheelView wv_province = (WheelView) wheelViewLayout.findViewById(R.id.wv_province);
        final WheelView wv_city = (WheelView) wheelViewLayout.findViewById(R.id.wv_city);
        final WheelView wv_district = (WheelView) wheelViewLayout.findViewById(R.id.wv_district);
        Button okBtn = (Button) wheelViewLayout.findViewById(R.id.select_ok);
        Button cancelBtn = (Button) wheelViewLayout.findViewById(R.id.select_cancel);

//        if (mProvinceList == null) {
        if (TextUtils.isEmpty(cityJson)) {
            mProvinceList = initProvinceData(context);
        } else {
            if (!currentCityJson.equals(cityJson)) {
                cityObj.reset();
            }
            currentCityJson = cityJson;
            mProvinceList = initProvinceData(cityJson);
            if (mProvinceList.size() == 0) {
                Toast.makeText(context, "上牌地列表为空，请联系相关人员添加数据。", Toast.LENGTH_SHORT).show();
                clickedView.setEnabled(true);
                return;
            }
        }
//        }

        wv_province.setOffset(DEFAULT_OFFSET);
        wv_city.setOffset(DEFAULT_OFFSET);
        wv_district.setOffset(DEFAULT_OFFSET);
        wv_province.setItems(mProvinceList);
        wv_province.setSelection(cityObj.mProvinceIndex);
        List<CityModel> cityList = mProvinceList.get(cityObj.mProvinceIndex).cityList;
        wv_city.setItems(cityList);
        wv_city.setSelection(cityObj.mCityIndex);
        List<DistrictModel> districtList = cityList.get(cityObj.mCityIndex).districtList;
        wv_district.setItems(districtList);
        wv_district.setSelection(cityObj.mDistrictIndex);
        wv_province.setOnSelectedListener(new WheelView.OnSelectedListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                cityObj.mProvinceIndex = selectedIndex - DEFAULT_OFFSET;
                wv_city.setItems(mProvinceList.get(cityObj.mProvinceIndex).cityList);
                wv_city.setSelection(0);
            }
        });
        wv_city.setOnSelectedListener(new WheelView.OnSelectedListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                cityObj.mCityIndex = selectedIndex - DEFAULT_OFFSET;
                wv_district.setItems(mProvinceList.get(cityObj.mProvinceIndex).cityList.get(cityObj.mCityIndex).districtList);
                wv_district.setSelection(0);
            }
        });
        wv_district.setOnSelectedListener(new WheelView.OnSelectedListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                cityObj.mDistrictIndex = selectedIndex - DEFAULT_OFFSET;
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = mProvinceList.get(cityObj.mProvinceIndex).name;
                String city = mProvinceList.get(cityObj.mProvinceIndex).cityList.get(cityObj.mCityIndex).name;
                String district = mProvinceList.get(cityObj.mProvinceIndex).cityList.get(cityObj.mCityIndex).districtList.get(cityObj.mDistrictIndex).name;
                String result = province + "/" + city + "/" + district;
                showView.setText(result);
                if (onCitySubmitCallBack != null) {
                    onCitySubmitCallBack.onCitySubmitCallBack(clickedView, result);
                }
                mWheelViewDialog.dismiss();
                mWheelViewDialog = null;
                clickedView.setEnabled(true);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWheelViewDialog.dismiss();
                mWheelViewDialog = null;
                clickedView.setEnabled(true);
            }
        });

        mWheelViewDialog = new Dialog(context, R.style.MyDialogStyle);
        mWheelViewDialog.setContentView(wheelViewLayout);
        mWheelViewDialog.setCanceledOnTouchOutside(false);
        mWheelViewDialog.getWindow().setWindowAnimations(R.style.dialogAnimationStyle);
        mWheelViewDialog.getWindow().setGravity(Gravity.BOTTOM);
        mWheelViewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWheelViewDialog.show();
    }

    private static List<ProvinceModel> initProvinceData(String cityJson) {
        List<ProvinceModel> provinceModels = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(cityJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ProvinceModel model = new Gson().fromJson(jsonObject.toString(), ProvinceModel.class);
                provinceModels.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return provinceModels;
    }


    private static List<ProvinceModel> initProvinceData(Context context) {
        try {
            AssetManager asset = context.getAssets();
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            return handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    static class CityWheelViewUtil {
        private Map<String, CityObj> maps;

        public CityWheelViewUtil() {
            maps = new HashMap<>();
        }

        class CityObj {
            public int mProvinceIndex = 0;
            public int mCityIndex = 0;
            public int mDistrictIndex = 0;

            public void reset() {
                mProvinceIndex = 0;
                mCityIndex = 0;
                mDistrictIndex = 0;
            }
        }

        public CityObj getCityObjByTag(String tag) {
            if (maps.get(tag) == null) {
                CityObj cityObj = new CityObj();
                maps.put(tag, cityObj);
                return cityObj;
            }
            return maps.get(tag);
        }

        public void updateCityObj(CityObj cityObj, int proIndex, int cityIndex, int disIndex) {
            cityObj.mProvinceIndex = proIndex;
            cityObj.mCityIndex = cityIndex;
            cityObj.mDistrictIndex = disIndex;
        }
    }
}