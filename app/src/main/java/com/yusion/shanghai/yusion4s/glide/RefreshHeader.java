package com.yusion.shanghai.yusion4s.glide;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrUIHandler;
import com.chanven.lib.cptr.indicator.PtrIndicator;
import com.yusion.shanghai.yusion4s.R;

/**
 * Created by LX on 2017/12/29.
 */

public class RefreshHeader extends android.support.v7.widget.AppCompatImageView implements PtrUIHandler {
    private int ss;
    private Context context;

    public RefreshHeader(Context context) {
        super(context);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
//        animate().rotation(1800).setDuration(2000).start();
        setImageResource(R.drawable.refresh_header);
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
