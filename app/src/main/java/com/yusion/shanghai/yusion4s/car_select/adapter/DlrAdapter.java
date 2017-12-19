package com.yusion.shanghai.yusion4s.car_select.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.bean.dlr.GetDlrListByTokenResp;

import java.util.List;

/**
 * Created by aa on 2017/12/12.
 */

public class DlrAdapter extends RecyclerView.Adapter<DlrAdapter.ViewHolder> {
    protected Context mContext;
    protected List<GetDlrListByTokenResp> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DlrAdapter(Context mContext, List<GetDlrListByTokenResp> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<GetDlrListByTokenResp> getDatas() {
        return mDatas;
    }

    public DlrAdapter setDatas(List<GetDlrListByTokenResp> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_dlr, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetDlrListByTokenResp getDlrListByTokenResp = mDatas.get(position);
        holder.tvCity.setText(getDlrListByTokenResp.dlr_nm);
        if (getDlrListByTokenResp.has_select_by_user) {
            holder.itemView.setBackgroundColor(Color.parseColor("#d1d1d1"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.itemView.setOnClickListener(v -> {
            for (GetDlrListByTokenResp resp : mDatas) {
                resp.has_select_by_user = false;
            }
            getDlrListByTokenResp.has_select_by_user = true;
            notifyDataSetChanged();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getDlrListByTokenResp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
      //  ImageView avatar;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
          //  avatar = itemView.findViewById(R.id.ivAvatar);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, GetDlrListByTokenResp getDlrListByTokenResp);
    }
}
