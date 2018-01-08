package com.yusion.shanghai.yusion4s.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgCenterFragment extends BaseFragment {


    public MsgCenterFragment() {
        // Required empty public constructor
    }

    public static MsgCenterFragment newInstance() {

        Bundle args = new Bundle();

        MsgCenterFragment fragment = new MsgCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_msg_center, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar(view, "提交成功");

    }
}
