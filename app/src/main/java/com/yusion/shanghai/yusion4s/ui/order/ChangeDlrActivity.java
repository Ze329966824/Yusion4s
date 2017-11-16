package com.yusion.shanghai.yusion4s.ui.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;
import com.yusion.shanghai.yusion4s.retrofit.api.DlrApi;

import org.jcodec.codecs.common.biari.MConst;

import java.util.ArrayList;

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
        adapter = new ChangeDlrAdapter();
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
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}