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
import com.yusion.shanghai.yusion4s.bean.dlr.GetBrandResp;

import java.util.List;


public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    protected Context mContext;
    protected List<GetBrandResp> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BrandAdapter(Context mContext, List<GetBrandResp> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<GetBrandResp> getDatas() {
        return mDatas;
    }

    public BrandAdapter setDatas(List<GetBrandResp> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(final BrandAdapter.ViewHolder holder, final int position) {
        final GetBrandResp brandResp = mDatas.get(position);
        holder.name.setText(brandResp.brand_name);
        if (brandResp.has_select_by_user) {
            holder.itemView.setBackgroundColor(Color.parseColor("#d1d1d1"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setOnClickListener(v -> {
            for (GetBrandResp resp : mDatas) {
                resp.has_select_by_user = false;
            }
            brandResp.has_select_by_user = true;
            notifyDataSetChanged();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, brandResp);
            }
        });
//        GlideApp.with(mContext).load(brandResp.brand_image_url).circleCrop().into(holder.icon);
        GlideApp.with(mContext).load(brandResp.brand_image_url).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, GetBrandResp brandResp);
    }
}
