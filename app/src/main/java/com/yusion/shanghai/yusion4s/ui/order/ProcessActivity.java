package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.ProcessTest;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends BaseActivity {
    private LinearLayout mLl_parent;
    private Context mContext;
    private List<ProcessTest> list;
    private String st;
    private String st2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initTitleBar(this, "订单进度");
        mContext = this;
        mLl_parent = (LinearLayout) findViewById(R.id.ll_parent);
        list = new ArrayList<>();
        ProcessTest processTest1 = new ProcessTest();
        processTest1.title = "填订单信息";
        processTest1.time = "123-43243";
        processTest1.st = "1";
        list.add(processTest1);

        ProcessTest processTest2 = new ProcessTest();
        processTest2.time = "78-22-22";
        processTest2.title = "还款";
        processTest2.st = "2";
        list.add(processTest2);


        ProcessTest processTest = new ProcessTest();
        processTest.st = "3";
        processTest.processTestList = new ArrayList<>();

        processTest.title = "征信影像审核";
        processTest.time = "34-2128-23";
        ProcessTest processTest4 = new ProcessTest();
        processTest4.title = "还款";
        processTest4.time = "6666-232-32";
        processTest.processTestList.add(processTest);
        processTest.processTestList.add(processTest4);

        list.add(processTest);

        for (int i = 0; i < list.size(); i++) {

            ProcessTest current = list.get(i);

            ProcessTest before;
            ProcessTest after;

            before = i == 0 ? null : list.get(i - 1);

            after = i == list.size() - 1 ? null : list.get(i + 1);


            View view = addViewEmpty();
            View topLine = view.findViewById(R.id.top_line);
            View bottomLine = view.findViewById(R.id.bottom_line);
            ImageView dianImg = (ImageView) view.findViewById(R.id.st_dian_img);//左边状态点
            ImageView stImg = (ImageView) view.findViewById(R.id.st_icon);//右侧是否通过的状态图片

            if (before == null) {
                topLine.setVisibility(View.INVISIBLE);
            } else if (after == null) {
                bottomLine.setVisibility(View.INVISIBLE);
            }
            //设置线条颜色
            st = current.st;
            if (before != null) {
                st2 = before.st;
                if (st2.equals("1")) {
                    topLine.setBackgroundColor(Color.parseColor("#06b7a3"));
                }
            }
            if (st.equals("1")) {
                bottomLine.setBackgroundColor(Color.parseColor("#06b7a3"));
                dianImg.setImageResource(R.mipmap.test25);
            } else if (st.equals("2")) {
                dianImg.setImageResource(R.mipmap.test24);
            }

            mLl_parent.addView(view);
            LinearLayout ll_empty = (LinearLayout) view.findViewById(R.id.ll_empty);
            if (current.processTestList.size() > 0 && !current.processTestList.isEmpty()) {
                for (int j = 0; j < current.processTestList.size(); j++) {
                    ll_empty.addView(addViewFill());
                    View view1 = new View(this);
                    view1.setBackgroundColor(Color.parseColor("#ffffff"));
                    ll_empty.addView(view1);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view1.getLayoutParams();
                    lp.height = DensityUtil.dip2px(this, 15);
                    if (j == current.processTestList.size() - 1) {
                        ll_empty.removeView(view1);
                    }
                }
            } else {
                ll_empty.addView(addViewFill());
            }
        }
    }

    private View addViewEmpty() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.process_empty, null);
        return view;
    }

    private View addViewFill() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.process_fill, null);
        return view;
    }
}
