package com.yusion.shanghai.yusion4s.car_select.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;

import java.util.List;

/**
 * Created by aa on 2018/1/3.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    protected Context mContext;
    protected List<String> mDatas;
    protected LayoutInflater mInflater;
    protected onItemCLickListener onItemClickListener;
    protected onItemChooseCLickListener onItemChooseClickListener;
    private EditText searchEt;

    public void setOnItemClickListener(onItemCLickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChooseClickListener(onItemChooseCLickListener onItemChooseClickListener) {
        this.onItemChooseClickListener = onItemChooseClickListener;
    }

    public HistoryAdapter(Context Context, List<String> Datas, EditText editText) {
        mContext = Context;
        mDatas = Datas;
        searchEt = editText;
        mInflater = LayoutInflater.from(mContext);
        Log.e("TAG", mDatas.size() + "");
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        String s = mDatas.get(position);
        holder.tv.setText(s);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(position);
                notifyDataSetChanged();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, mDatas);
                }
                //重新保存sp
                String history = "";
                for (int i = 0; i < mDatas.size(); i++) {
                    history = mDatas.get(i) + "#" + history;
                }
                SharedPrefsUtil.getInstance(mContext).putValue("history", history);
            }
        });
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将内容之余输入框
                if (onItemChooseClickListener != null) {
                    onItemChooseClickListener.onChooseClick(v, holder.tv.getText().toString());
                }
//                searchEt.setText(holder.tv.getText().toString());
//                searchEt.setSelection(holder.tv.getText().toString().length());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView deleteImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.history_tv);
            deleteImg = itemView.findViewById(R.id.history_delete_img);
        }
    }

    public interface onItemCLickListener {
        void onItemClick(View v, List<String> mdates);
    }


    public interface onItemChooseCLickListener {
        void onChooseClick(View v, String etText);
    }

}
