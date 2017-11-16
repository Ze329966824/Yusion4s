package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;

import java.util.ArrayList;

import static com.umeng.analytics.pro.x.R;

public class ChangeDlrActivity extends AppCompatActivity {

    private ArrayList<String> dlrItems;
    private RecyclerView changeDlr_rv;
    private ChangeDlrAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dealer);


        showDlr();
        initView();

    }

    private void initView() {
        changeDlr_rv = (RecyclerView) findViewById(R.id.change_dlr_rv);
    }

    private void showDlr() {
        DlrApi.getDlrListByToken(this, resp -> {
            if (resp != null && !resp.isEmpty()) {
                dlrItems = new ArrayList<String>();
                for (GetDlrListByTokenResp item : resp) {
                    dlrItems.add(item.dlr_nm);
                }
            }
        });
        adapter = new ChangeDlrAdapter(this,dlrItems);
        changeDlr_rv.setLayoutManager(new LinearLayoutManager(this));
        changeDlr_rv.setAdapter(adapter);



    }

    static class ChangeDlrAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private ArrayList<String> items;


        public ChangeDlrAdapter(Context context,ArrayList<String> list) {
            mContext = context;
            items =list;
        }

        @Override
        public DlrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DlrViewHolder(LayoutInflater.from(mContext).inflate(R.layout.dlr_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        protected class DlrViewHolder extends RecyclerView.ViewHolder {

            public DlrViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}