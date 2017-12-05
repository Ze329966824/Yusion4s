package com.yusion.shanghai.yusion4s.car_select.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.car_select.EngCapBean;

import java.util.List;

/**
 * Created by ice on 2017/12/5.
 */

public class EngGvAdapter extends RecyclerView.Adapter<EngGvAdapter.VH> {


    private List<EngCapBean> list;
    private Context context;
    private final LayoutInflater inflater;

    public EngGvAdapter(Context context, List<EngCapBean> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_eng_cap, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.engTv.setText(list.get(position).eng_cap);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        public TextView engTv;

        public VH(View itemView) {
            super(itemView);
            engTv = itemView.findViewById(R.id.eng_tv);
        }
    }

}
