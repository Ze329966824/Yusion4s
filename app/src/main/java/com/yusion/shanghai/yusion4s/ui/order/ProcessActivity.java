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
        processTest1.time = "2016-07-05  14:32";
        processTest1.st = "1";
        list.add(processTest1);

        ProcessTest processTest2 = new ProcessTest();
        processTest2.time = "DW UW";
        processTest2.title = "2016-07-05  14:32";
        processTest2.st = "2";
        list.add(processTest2);

        ProcessTest processTest3 = new ProcessTest();
        processTest3.st = "3";
        processTest3.asyncProcessTestList = new ArrayList<>();
        processTest3.syncProcessTestList = new ArrayList<>();
        ProcessTest processTest31 = new ProcessTest();
        ProcessTest processTest32 = new ProcessTest();
        processTest32.title = "予见电话审核";
        processTest32.time = "2016-07-05  14:32";
        processTest3.asyncProcessTestList.add(processTest31);
        processTest3.asyncProcessTestList.add(processTest32);

        ProcessTest processTest311 = new ProcessTest();
        processTest311.title = "征信影像件审核";
        ProcessTest processTest312 = new ProcessTest();
        processTest312.title = "征信结果查询";
        processTest31.syncProcessTestList.add(processTest311);
        processTest31.syncProcessTestList.add(processTest312);
        list.add(processTest3);
//
//
//        ProcessTest processTest = new ProcessTest();
//        processTest.st = "3";
//        processTest.asyncProcessTestList = new ArrayList<>();
//
//        processTest.title = "征信影像审核";
//        processTest.time = "34-2128-23";
//        ProcessTest processTest4 = new ProcessTest();
//        processTest4.title = "还款";
//        processTest4.time = "6666-232-32";
//        processTest.asyncProcessTestList.add(processTest);
//        processTest.asyncProcessTestList.add(processTest4);
//
//        list.add(processTest);

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
            fill(current, ll_empty);
//            if (current.asyncProcessTestList.size() > 0) {
//                for (int j = 0; j < current.asyncProcessTestList.size(); j++) {
//                    View fillView = addViewFill();
//                    ll_empty.addView(fillView);
//                    View view1 = new View(this);
//                    view1.setBackgroundColor(Color.parseColor("#ffffff"));
//                    ll_empty.addView(view1);
//                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view1.getLayoutParams();
//                    lp.height = DensityUtil.dip2px(this, 15);
//                    if (j == current.asyncProcessTestList.size() - 1) {
//                        ll_empty.removeView(view1);
//                    }
//                }
//            } else {
//                for (int k = 0; k < current.syncProcessTestList.size(); k++) {
//                    View fillView = addViewFill();
//                    ll_empty.addView(fillView);
//                }
//            }
        }
    }

    private void fill(ProcessTest current, LinearLayout ll_empty) {
        if (current.asyncProcessTestList.size() == 0 && current.syncProcessTestList.size() == 0) {
            //没有子view 直接设置title
            LinearLayout fillView = addViewFill();
            fillView.addView(addViewFillContent());
            ll_empty.addView(fillView);
        } else if (current.asyncProcessTestList.size() > 0) {
            //多个异步块子view (需要白线隔离)
            for (int j = 0; j < current.asyncProcessTestList.size(); j++) {
                LinearLayout fillView = addViewFill();
                if (current.asyncProcessTestList.get(j).syncProcessTestList.size() > 0) {
                    //多个同步块子View
                    for (int k = 0; k < current.asyncProcessTestList.get(j).syncProcessTestList.size(); k++) {
                        //设置图和线
                        ProcessTest current2 = current.asyncProcessTestList.get(j).syncProcessTestList.get(k);
                        ProcessTest before2;
                        ProcessTest after2;
                        before2 = k == 0 ? null : current.asyncProcessTestList.get(j).syncProcessTestList.get(k - 1);
                        after2 = k == current.asyncProcessTestList.get(j).syncProcessTestList.size() - 1 ? null : current.asyncProcessTestList.get(j).syncProcessTestList.get(k + 1);
                        View syncfillView = addViewFillContent();
                        View topLine = syncfillView.findViewById(R.id.top_line);
                        View bottomLine = syncfillView.findViewById(R.id.bottom_line);
                        View xuxian = syncfillView.findViewById(R.id.xuxian);
                        ImageView dian = (ImageView) syncfillView.findViewById(R.id.dian_icon);
                        dian.setVisibility(View.VISIBLE);
                        if (before2 == null) {
                            topLine.setVisibility(View.INVISIBLE);
                        } else {
                            topLine.setVisibility(View.VISIBLE);
                        }
                        if (after2 == null) {
                            xuxian.setVisibility(View.GONE);
                            bottomLine.setVisibility(View.INVISIBLE);
                        } else {
                            xuxian.setVisibility(View.VISIBLE);
                            bottomLine.setVisibility(View.VISIBLE);
                        }

//                        else if (after2 != null) {
//                            bottomLine.setVisibility(View.VISIBLE);
//                        }
//                        if (before == null) {
//                            topLine.setVisibility(View.INVISIBLE);
//                        } else if (after == null) {
//                            bottomLine.setVisibility(View.INVISIBLE);
//                        }
//                        syncfillView.findViewById(R.id.dian_icon);
                        fillView.addView(syncfillView);
                    }
                } else {
                    //没有同步块子view
                    View addViewFillContent = addViewFillContent();
                    ImageView imageView = (ImageView) addViewFillContent.findViewById(R.id.dian_icon);
                    imageView.setVisibility(View.VISIBLE);
                    fillView.addView(addViewFillContent);
                }

                ll_empty.addView(fillView);
                View view1 = new View(this);
                view1.setBackgroundColor(Color.parseColor("#ffffff"));
                ll_empty.addView(view1);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view1.getLayoutParams();
                lp.height = DensityUtil.dip2px(this, 15);
                if (j == current.asyncProcessTestList.size() - 1) {
                    ll_empty.removeView(view1);
                }

            }
        }
    }

    private View addViewEmpty() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.process_empty, null);
        return view;
    }

    private LinearLayout addViewFill() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.process_fill, null);
        return view;
    }

    private View addViewFillContent() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.testjj, null);
        //process_fil_contentl
        return view;
    }
}
