package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.order.ProcessTest;

import java.util.ArrayList;
import java.util.List;

public class ApplyProcessActivity extends BaseActivity {
    private Context mContext;
    private LinearLayout mLl_parent;
    private List<ProcessTest> list;

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
        list.add(processTest1);

        ProcessTest processTest2 = new ProcessTest();
        processTest2.time = "78-22-22";
        processTest2.title = "还款";
        list.add(processTest2);


        ProcessTest processTest = new ProcessTest();
        processTest.processTestList = new ArrayList<>();

        processTest.title = "征信影像审核";
        processTest.title2 = "征信结果查询";
        ProcessTest processTest4 = new ProcessTest();
        processTest4.time2 = "999999";
        processTest4.title2 = "6666";
        processTest.processTestList.add(processTest);
        processTest.processTestList.add(processTest4);
        list.add(processTest);

        for (int i = 0; i < list.size(); i++) {
            ProcessTest processTest3 = list.get(i);
            if (processTest3.processTestList.size() > 0 && !processTest3.processTestList.isEmpty()) {
                View view = addView2();
                View view1 = view.findViewById(R.id.line_kuan);
                view1.setVisibility(View.GONE);
                mLl_parent.addView(view);
                mLl_parent.addView(addView5());
            } else {
                mLl_parent.addView(addView2());
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
