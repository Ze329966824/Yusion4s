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
import com.yusion.shanghai.yusion4s.bean.dlr.GetStoreList;

import java.util.List;


public class DlrStoreAdapter extends RecyclerView.Adapter<DlrStoreAdapter.ViewHolder> {
    protected Context mContext;
    protected List<GetStoreList> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DlrStoreAdapter(Context mContext, List<GetStoreList> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<GetStoreList> getDatas() {
        return mDatas;
    }

    public DlrStoreAdapter setDatas(List<GetStoreList> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public DlrStoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(final DlrStoreAdapter.ViewHolder holder, final int position) {
        final GetStoreList trixResp = mDatas.get(position);
        holder.tvCity.setText(trixResp.dlr_nm);
        if (trixResp.has_select_by_user) {
            holder.itemView.setBackgroundColor(Color.parseColor("#d1d1d1"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setOnClickListener(v -> {
            for (GetStoreList resp : mDatas) {
                resp.has_select_by_user = false;
            }
            trixResp.has_select_by_user = true;
            notifyDataSetChanged();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, trixResp);
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
        ImageView avatar;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            avatar = itemView.findViewById(R.id.ivAvatar);
            avatar.setVisibility(View.GONE);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, GetStoreList trixResp);
    }
}
