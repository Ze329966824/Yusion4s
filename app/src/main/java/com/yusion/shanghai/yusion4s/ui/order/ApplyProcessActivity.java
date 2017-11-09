package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.ProcessTest;

import java.util.ArrayList;
import java.util.List;

public class ApplyProcessActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout mLl_parent;
    private List<ProcessTest> list;
    private String st;
    private String st2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_process);
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
        processTest.processTestList = new ArrayList<>();

        processTest.title = "征信影像审核";
        processTest.time = "34-2128-23";
        ProcessTest processTest4 = new ProcessTest();
        processTest4.title2 = "还款";
        processTest4.time2 = "6666-232-32";
        processTest.processTestList.add(processTest);
        processTest.processTestList.add(processTest4);

        list.add(processTest);

        for (int i = 0; i < list.size(); i++) {

            ProcessTest processTest3 = list.get(i);

            ProcessTest before;
            ProcessTest after;

            before = i == 0 ? null : list.get(i - 1);

            after = i == list.size() - 1 ? null : list.get(i + 1);


            if (processTest3.processTestList.size() > 0 && !processTest3.processTestList.isEmpty()) {
                View view = addView2();
                View view1 = view.findViewById(R.id.line_kuan);
                view1.setVisibility(View.GONE);
                View topLine = view.findViewById(R.id.top_line);
                View bottomLine = view.findViewById(R.id.bottom_line);

                ProcessTest processTest5 = processTest.processTestList.get(0);
                TextView tv = (TextView) view.findViewById(R.id.text);
                TextView time = (TextView) view.findViewById(R.id.time);

                tv.setText(processTest5.title);
                time.setText(processTest5.time);
                if (i == 0) {
                    topLine.setVisibility(View.INVISIBLE);
                } else if (i == list.size() - 1) {
                    bottomLine.setVisibility(View.INVISIBLE);
                }
                mLl_parent.addView(view);
                View view2 = addView5();
                View wholeLine = view2.findViewById(R.id.whole_line);
                TextView tv2 = (TextView) view2.findViewById(R.id.text2);
                TextView time2 = (TextView) view2.findViewById(R.id.time2);
                ProcessTest processTest6 = processTest.processTestList.get(1);
                tv2.setText(processTest6.title2);
                time2.setText(processTest6.time2);
                if (i == list.size() - 1) {
                    wholeLine.setVisibility(View.INVISIBLE);
                }
                mLl_parent.addView(view2);
            } else {
                View view = addView2();
                View topLine = view.findViewById(R.id.top_line);
                View bottomLine = view.findViewById(R.id.bottom_line);
                ImageView dianImg = (ImageView) view.findViewById(R.id.st_dian_img);
                ImageView stImg = (ImageView) view.findViewById(R.id.st_icon);
                TextView tv = (TextView) view.findViewById(R.id.text);
                TextView time = (TextView) view.findViewById(R.id.time);

                st = processTest3.st;
                if (before != null) {
                    st2 = before.st;
                    if (st2.equals("1")) {
                        topLine.setBackgroundColor(Color.parseColor("#06b7a3"));
                    }
                }
                if (st.equals("1")) {
                    dianImg.setImageResource(R.mipmap.test23);
                    topLine.setBackgroundColor(Color.parseColor("#06b7a3"));
                    bottomLine.setBackgroundColor(Color.parseColor("#06b7a3"));
                }
                time.setText(processTest3.time);
                tv.setText(processTest3.title);
                if (i == 0) {
                    topLine.setVisibility(View.INVISIBLE);
                } else if (i == list.size() - 1) {
                    bottomLine.setVisibility(View.GONE);
                }
                mLl_parent.addView(view);
            }
        }
    }

    private View addView2() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.test222, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView3);
        return view;
    }

    private View addView5() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.test555, null);
        return view;
    }

}
