package com.yusion.shanghai.yusion4s.car_select.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetModelResp;

import java.util.List;


public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {
    protected Context mContext;
    protected List<GetModelResp> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ModelAdapter(Context mContext, List<GetModelResp> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<GetModelResp> getDatas() {
        return mDatas;
    }

    public ModelAdapter setDatas(List<GetModelResp> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public ModelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_model, parent, false));
    }

    @Override
    public void onBindViewHolder(final ModelAdapter.ViewHolder holder, final int position) {
        final GetModelResp modelResp = mDatas.get(position);
        holder.tvCity.setText(modelResp.model_name);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v,modelResp);
            }
        });
//        GlideApp.with(mContext).load(R.mipmap.ic_launcher).circleCrop().into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v,GetModelResp modelResp);
    }
}
