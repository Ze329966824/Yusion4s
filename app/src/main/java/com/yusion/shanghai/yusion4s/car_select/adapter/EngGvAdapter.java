package com.yusion.shanghai.yusion4s.car_select.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

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
    protected OnItemCheckedChangeListener onItemCheckedChangeListener;

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener onItemCheckedChangeListener) {
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

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
        EngCapBean engCapBean = list.get(position);
        holder.engTv.setText(engCapBean.eng_cap);
        if (engCapBean.has_selected) {
            holder.engTv.setChecked(true);
        }else {
            holder.engTv.setChecked(false);
        }
        holder.engTv.setOnCheckedChangeListener((buttonView, isChecked) -> {
            engCapBean.has_selected = isChecked;
            if (onItemCheckedChangeListener!=null) {
                onItemCheckedChangeListener.onItemCheckedChange(buttonView,isChecked, engCapBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        public CheckBox engTv;

        public VH(View itemView) {
            super(itemView);
            engTv = itemView.findViewById(R.id.eng_tv);
        }
    }

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChange(View v, boolean isChecked, EngCapBean engCapBean);
    }

}
