package com.yusion.shanghai.yusion4s.utils.wheel;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public interface OndateSubmitCallBack {
        void OndateSubmitCallBack(View clickedView, String date);
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
                if (mWheelViewDialog != null && mWheelViewDialog.isShowing()) {
                    mWheelViewDialog.dismiss();
                    mWheelViewDialog = null;
                }
                clickedView.setEnabled(true);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWheelViewDialog != null && mWheelViewDialog.isShowing()) {
                    mWheelViewDialog.dismiss();
                    mWheelViewDialog = null;
                }
                clickedView.setEnabled(true);
            }
        });

        mWheelViewDialog = new Dialog(context, R.style.MyDialogStyle);
        mWheelViewDialog.setContentView(wheelViewLayout);
        mWheelViewDialog.setCanceledOnTouchOutside(false);
        mWheelViewDialog.setOnCancelListener(dialog -> clickedView.setEnabled(true));
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

    public static String formateDate(Integer s) {
        if (s < 10) {
            return "0" + s;
        } else {
            return s + "";
        }
    }

    private static void hideDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDatePick(final View clickView, final TextView showView, final String title, String min_reg_year, String max_reg_year, final OndateSubmitCallBack ondateSubmitCallBack) {
        clickView.setEnabled(false);
        Context context = clickView.getContext();
        View wheelViewLayout = LayoutInflater.from(context).inflate(R.layout.datepick_view_layout, null);
        TextView textTitle = (TextView) wheelViewLayout.findViewById(R.id.select_title);
        textTitle.setText(title);
        DatePicker datePicker = (DatePicker) wheelViewLayout.findViewById(R.id.date_pick);
        Button okBtn = (Button) wheelViewLayout.findViewById(R.id.select_ok);
        Button cancelBtn = (Button) wheelViewLayout.findViewById(R.id.select_cancel);
        String s = showView.getText().toString();
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        Integer[] s1 = new Integer[1];
        Integer[] s2 = new Integer[1];
        Integer[] s3 = new Integer[1];
        boolean[] ischang = {false};

        String minTime = min_reg_year + "-" + "01-01";

        String maxTime = max_reg_year + "-" + "12-31";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date date2 = null;
        try {
            date = simpleDateFormat.parse(minTime);
            date2 = simpleDateFormat.parse(maxTime);
            long mintime = date.getTime();
            long maxteime = date2.getTime();
            datePicker.setMinDate(mintime);
            datePicker.setMaxDate(maxteime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hideDay(datePicker);
        // ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(1)).getChildAt(0).setVisibility(View.GONE);
        //2017年12月12日
        if (!s.equals("")) {
            //String[] array = s.split("年|月|日");
            String[] array = s.split("-");
            s1[0] = Integer.valueOf(array[0]);
            s2[0] = Integer.valueOf(array[1]);
            s3[0] = 1;
            Log.e("TAG", "showDatePick: sssss");
            datePicker.init(s1[0], s2[0] - 1, s3[0], new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    s1[0] = year;
                    s2[0] = monthOfYear + 1;
                    s3[0] = dayOfMonth;
                }
            });
        } else {
            s1[0] = Integer.valueOf(min_reg_year);
            s2[0] = 0;
            s3[0] = 1;
            datePicker.init(s1[0], s2[0], s3[0], new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    s1[0] = year;
                    s2[0] = monthOfYear + 1;
                    s3[0] = dayOfMonth;
                    // s3[0] = dayOfMonth;
                    ischang[0] = true;
                }
            });
            if (!ischang[0]) {
                s2[0] = 1;
            }
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = s1[0] + "-" + formateDate(s2[0]);
                // String result = s1[0] + "-" + formateDate(s2[0]) + "-" + formateDate(s3[0]);
                showView.setText(result);
                if (ondateSubmitCallBack != null) {
                    ondateSubmitCallBack.OndateSubmitCallBack(clickView, result);
                }
                if (mWheelViewDialog != null && mWheelViewDialog.isShowing()) {
                    mWheelViewDialog.dismiss();
                    mWheelViewDialog = null;
                }
                clickView.setEnabled(true);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWheelViewDialog != null && mWheelViewDialog.isShowing()) {
                    mWheelViewDialog.dismiss();
                    mWheelViewDialog = null;
                }
                clickView.setEnabled(true);
            }
        });
        mWheelViewDialog = new Dialog(context, R.style.MyDialogStyle);
        mWheelViewDialog.setContentView(wheelViewLayout);
        mWheelViewDialog.setCanceledOnTouchOutside(false);
        mWheelViewDialog.setOnCancelListener(dialog -> clickView.setEnabled(true));
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

    public static void showCityWheelView(String tag, boolean showThirdCity, final View clickedView, final TextView showView, String title, final OnCitySubmitCallBack onCitySubmitCallBack, String cityJson) {
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

        View wheelViewLayout;
        if (showThirdCity) {
            wheelViewLayout = LayoutInflater.from(context).inflate(R.layout.city_wheel_view_layout, null);
        } else {
            wheelViewLayout = LayoutInflater.from(context).inflate(R.layout.no_third_city_wheel_view_layout, null);
        }

        TextView textTitle = (TextView) wheelViewLayout.findViewById(R.id.select_title);
        textTitle.setText(title);

        final WheelView wv_province = (WheelView) wheelViewLayout.findViewById(R.id.wv_province);
        final WheelView wv_city = (WheelView) wheelViewLayout.findViewById(R.id.wv_city);
        final WheelView wv_district = (WheelView) wheelViewLayout.findViewById(R.id.wv_district);
        Button okBtn = (Button) wheelViewLayout.findViewById(R.id.select_ok);
        Button cancelBtn = (Button) wheelViewLayout.findViewById(R.id.select_cancel);

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

        wv_province.setOffset(DEFAULT_OFFSET);
        wv_city.setOffset(DEFAULT_OFFSET);

        wv_province.setItems(mProvinceList);
        wv_province.setSelection(cityObj.mProvinceIndex);
        List<CityModel> cityList = mProvinceList.get(cityObj.mProvinceIndex).cityList;
        wv_city.setItems(cityList);
        wv_city.setSelection(cityObj.mCityIndex);
        List<DistrictModel> districtList = cityList.get(cityObj.mCityIndex).districtList;
        if (wv_district != null) {
            wv_district.setOffset(DEFAULT_OFFSET);
            wv_district.setItems(districtList);
            wv_district.setSelection(cityObj.mDistrictIndex);
            wv_district.setOnSelectedListener((selectedIndex, item) -> cityObj.mDistrictIndex = selectedIndex - DEFAULT_OFFSET);
        }

        wv_province.setOnSelectedListener((selectedIndex, item) -> {
            cityObj.mProvinceIndex = selectedIndex - DEFAULT_OFFSET;
            wv_city.setItems(mProvinceList.get(cityObj.mProvinceIndex).cityList);
            wv_city.setSelection(0);
        });
        wv_city.setOnSelectedListener((selectedIndex, item) -> {
            cityObj.mCityIndex = selectedIndex - DEFAULT_OFFSET;
            if (wv_district != null) {
                wv_district.setItems(mProvinceList.get(cityObj.mProvinceIndex).cityList.get(cityObj.mCityIndex).districtList);
                wv_district.setSelection(0);
            }
        });

        okBtn.setOnClickListener(v -> {
            String province = mProvinceList.get(cityObj.mProvinceIndex).name;
            String city = mProvinceList.get(cityObj.mProvinceIndex).cityList.get(cityObj.mCityIndex).name;
            String district = mProvinceList.get(cityObj.mProvinceIndex).cityList.get(cityObj.mCityIndex).districtList.get(cityObj.mDistrictIndex).name;
            String result = province + "/" + city + "/" + district;
            showView.setText(result);
            if (onCitySubmitCallBack != null) {
                onCitySubmitCallBack.onCitySubmitCallBack(clickedView, result);
            }
            if (mWheelViewDialog != null && mWheelViewDialog.isShowing()) {
                mWheelViewDialog.dismiss();
                mWheelViewDialog = null;
            }
            clickedView.setEnabled(true);
        });

        cancelBtn.setOnClickListener(v -> {
            if (mWheelViewDialog != null && mWheelViewDialog.isShowing()) {
                mWheelViewDialog.dismiss();
                mWheelViewDialog = null;
            }
            clickedView.setEnabled(true);
        });

        mWheelViewDialog = new Dialog(context, R.style.MyDialogStyle);
        mWheelViewDialog.setContentView(wheelViewLayout);
        mWheelViewDialog.setCanceledOnTouchOutside(false);
        mWheelViewDialog.getWindow().setWindowAnimations(R.style.dialogAnimationStyle);
        mWheelViewDialog.getWindow().setGravity(Gravity.BOTTOM);
        mWheelViewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWheelViewDialog.setOnCancelListener(dialog -> clickedView.setEnabled(true));
        mWheelViewDialog.show();
    }

    public static void showCityWheelView(String tag, final View clickedView, final TextView showView, String title, final OnCitySubmitCallBack onCitySubmitCallBack, String cityJson) {
        showCityWheelView(tag, true, clickedView, showView, title, onCitySubmitCallBack, cityJson);
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