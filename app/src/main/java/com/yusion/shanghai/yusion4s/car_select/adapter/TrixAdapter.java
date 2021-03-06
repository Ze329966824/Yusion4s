package com.yusion.shanghai.yusion4s.car_select.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetTrixResp;

import java.util.List;


public class TrixAdapter extends RecyclerView.Adapter<TrixAdapter.ViewHolder> {
    protected Context mContext;
    protected List<GetTrixResp> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TrixAdapter(Context mContext, List<GetTrixResp> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<GetTrixResp> getDatas() {
        return mDatas;
    }

    public TrixAdapter setDatas(List<GetTrixResp> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public TrixAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(final TrixAdapter.ViewHolder holder, final int position) {
        final GetTrixResp trixResp = mDatas.get(position);
        holder.tvCity.setText(trixResp.trix_name);
        if (trixResp.has_select_by_user) {
            holder.itemView.setBackgroundColor(Color.parseColor("#d1d1d1"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setOnClickListener(v -> {
            for (GetTrixResp resp : mDatas) {
                resp.has_select_by_user = false;
            }
            trixResp.has_select_by_user = true;
            notifyDataSetChanged();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v,trixResp);
            }
        });
//        GlideApp.with(mContext).load(R.mipmap.ic_launcher).circleCrop().into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        ImageView avatar;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.icon);
            avatar.setVisibility(View.GONE);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, GetTrixResp trixResp);
    }
}
