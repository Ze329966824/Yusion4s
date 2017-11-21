package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;

import java.util.ArrayList;


public class ChangeDlrActivity extends AppCompatActivity {

    private ArrayList<String> dlrItems;
    private String drl_num;
    private RecyclerView changeDlr_rv;
    private ChangeDlrAdapter adapter;
    private ArrayList<String> testlists = new ArrayList<String>() {{
        add("测试门店1");
        add("测试门店2");
        add("测试门店3");
    }};

    private Intent intent;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    over(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };



    private void over(String msg) {
        for (int i = 0; i < dlrItems.size(); i++) {
            if (dlrItems.get(i).equals(msg)){
                drl_num = String.valueOf(i);
            }
        }


        intent.putExtra("dlr",msg);
        intent.putExtra("dlr_num",drl_num);
//        Log.e("TAG", "over: "+msg);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dealer);
        intent = getIntent();
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);
        cancel();
        showDlr();

    }

    private void cancel() {
        findViewById(R.id.cancel_drl_tv).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);
    }

    private void showDlr() {
        dlrItems = new ArrayList<String>();
        DlrApi.getDlrListByToken(this, resp -> {
            if (resp != null && !resp.isEmpty()) {
                for (GetDlrListByTokenResp item : resp) {
                    dlrItems.add(item.dlr_nm);
                }
                changeDlr_rv = (RecyclerView) findViewById(R.id.change_dlr_rv);

                adapter = new ChangeDlrAdapter(this, dlrItems,mHandler);

//                adapter = new ChangeDlrAdapter(this, testlists,mHandler);
                changeDlr_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                changeDlr_rv.setAdapter(adapter);
            }
        });


    }


    class ChangeDlrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;
        private ArrayList<String> items;
        private final Handler mHandler;


        ChangeDlrAdapter(Context context, ArrayList<String> list,Handler handler) {
            mContext = context;
            items = list;
            mHandler = handler;
        }

        @Override
        public DlrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DlrViewHolder(LayoutInflater.from(mContext).inflate(R.layout.dlr_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DlrViewHolder viewHolder = (DlrViewHolder) holder;
            String dlr = items.get(position);
            if (!TextUtils.isEmpty(dlr)) {
                viewHolder.dlr_tv.setText(dlr);

            }else {
                ((TextView) findViewById(R.id.item_list_dlr_tv)).setText("没！有！门！店！");

            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.sendMessage(mHandler.obtainMessage(0, dlr));

                }
            });
        }


        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        protected class DlrViewHolder extends RecyclerView.ViewHolder {
            private TextView dlr_tv;

            public DlrViewHolder(View itemView) {
                super(itemView);
                dlr_tv = (TextView) itemView.findViewById(R.id.item_list_dlr_tv);
            }
        }

    }
}