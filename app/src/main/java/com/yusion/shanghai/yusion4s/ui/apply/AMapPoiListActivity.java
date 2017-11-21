package com.yusion.shanghai.yusion4s.ui.apply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.amap.PoiResp;
import com.yusion.shanghai.yusion4s.retrofit.api.AMapApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.widget.NoEmptyEditText;

import java.util.ArrayList;
import java.util.List;



/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class AMapPoiListActivity extends BaseActivity implements View.OnClickListener {

    private NoEmptyEditText mPoiKeywords;
    private RecyclerView mPoiList;
    private PoiListAdapter mPoiListAdapter;
    private String mAMapKey = "d7464e59edd1dbd39414d8a46cecb93c";
    private LinearLayout mNoAddress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ReqBean reqBean = (ReqBean) msg.obj;
                    getData(reqBean.keywords, reqBean.city);
                    break;
                case 1:
                    over(msg.obj.toString());
                    break;
            }
        }
    };
    private TextView mPoiCity;
    private String mCity;
    private Intent mIntent;
    private TextView mSure;
    private ImageView mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap_poi_list);
        initView();
        initData();
        initTitleBar(this, "居住地址");
    }

    private void initData() {
        mIntent = getIntent();

        mCity = mIntent.getStringExtra("city");
        mPoiCity.setText(mCity);
        getData(mIntent.getStringExtra("keywords"), mCity);
    }

    private void initView() {
        mPoiKeywords = (NoEmptyEditText) findViewById(R.id.poi_keywords);
        mPoiList = (RecyclerView) findViewById(R.id.poi_list);
        mPoiListAdapter = new PoiListAdapter(this, mHandler);
        mPoiList.setAdapter(mPoiListAdapter);
        mPoiKeywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.removeMessages(0);
                if (s.length() == 0) {
                    return;
                }
                Message message = mHandler.obtainMessage(0, new ReqBean(s.toString(), mCity));
                mHandler.sendMessageDelayed(message, 200);
            }
        });
        mNoAddress = (LinearLayout) findViewById(R.id.poi_no_address);
        mPoiCity = (TextView) findViewById(R.id.poi_city);

        mSure = (TextView) findViewById(R.id.poi_sure);
        mDelete = (ImageView) findViewById(R.id.poi_delete);

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPoiKeywords.setText("");
            }
        });

    }

    private void getData(String keywords, String city) {
        AMapApi.getPoiResp(this, mAMapKey, keywords, city, new OnItemDataCallBack<PoiResp>() {
            @Override
            public void onItemDataCallBack(PoiResp data) {
                List<PoiResp.PoisBean> pois = data.pois;
                if (pois.size() == 0) {
                    mNoAddress.setVisibility(View.VISIBLE);
                } else {
                    mNoAddress.setVisibility(View.GONE);
                }
                mPoiListAdapter.changeAll(pois);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.poi_submit:
                over(mPoiKeywords.getText().toString());
                break;
            case R.id.poi_sure:
                over(mPoiKeywords.getText().toString());
                break;
        }
    }

    /**
     * 选择地址结束 回到上级页面
     *
     * @param address
     */
    private void over(String address) {
        mIntent.putExtra("result", address.trim());
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private class ReqBean {
        String keywords;
        String city;

        ReqBean(String keywords, String city) {
            this.keywords = keywords;
            this.city = city;
        }
    }

    class PoiListAdapter extends RecyclerView.Adapter<PoiListAdapter.VH> implements View.OnClickListener {

        private final LayoutInflater mLayoutInflater;
        private final Handler mHandler;
        private List<PoiResp.PoisBean> mDataList;

        PoiListAdapter(Context context, Handler handler) {
            mDataList = new ArrayList<>();
            mLayoutInflater = LayoutInflater.from(context);
            mHandler = handler;
        }

        private void changeAll(List<PoiResp.PoisBean> newDataList) {
            int oldSize = mDataList.size();
            mDataList.clear();
            notifyItemRangeRemoved(0, oldSize);
            mDataList.addAll(newDataList);
            notifyItemRangeInserted(0, newDataList.size());
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.amap_poi_item, parent, false));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            PoiResp.PoisBean poisBean = mDataList.get(position);
            holder.itemView.setOnClickListener(this);
            holder.mItemTitle.setText(poisBean.name);
            holder.mItemText.setText(poisBean.address.toString());
        }

        @Override
        public int getItemCount() {
            int size = mDataList.size();
            return size;
        }

        @Override
        public void onClick(View v) {
            String address = ((TextView) v.findViewById(R.id.item_title)).getText().toString();
            mHandler.sendMessage(mHandler.obtainMessage(1, address));
        }

        class VH extends RecyclerView.ViewHolder {
            private final TextView mItemTitle;
            private final TextView mItemText;

            VH(View itemView) {
                super(itemView);
                mItemTitle = ((TextView) itemView.findViewById(R.id.item_title));
                mItemText = ((TextView) itemView.findViewById(R.id.item_text));
            }
        }
    }
}
