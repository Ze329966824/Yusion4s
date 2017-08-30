package com.yusion.shanghai.yusion4s.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.utils.DensityUtil;
public class TitleBar extends ViewGroup {

    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15;
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 50;

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private TextView mLeftTextTv;
    private TextView mRightTextTv;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    private boolean mImmersive;

    public TextView getmLeftTextTv() {
        return mLeftTextTv;
    }

    public void setmLeftTextTv(TextView mLeftTextTv) {
        this.mLeftTextTv = mLeftTextTv;
    }

    private int mScreenWidth;
    private int mStatusBarHeight;
    private int mActionPadding;
    private int mOutPadding;
    private int mHeight;

    public interface OnRightViewClickListener {
        void onRightViewClick(View v);
    }

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        mActionPadding = DensityUtil.dip2px(context, 5);
        mOutPadding = DensityUtil.dip2px(context, 8);
        mHeight = DensityUtil.dip2px(context, DEFAULT_TITLE_BAR_HEIGHT);
        initView(context);
    }

    private void initView(Context context) {
        mLeftTextTv = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mRightTextTv = new TextView(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftTextTv.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        mLeftTextTv.setSingleLine();
        mLeftTextTv.setGravity(Gravity.CENTER_VERTICAL);
        mLeftTextTv.setPadding(mOutPadding + mActionPadding, 0, mOutPadding, 0);

        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);

        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);

        mSubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mRightTextTv.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        mRightTextTv.setSingleLine();
        mRightTextTv.setGravity(Gravity.CENTER_VERTICAL);
        mRightTextTv.setPadding(mOutPadding, 0, mOutPadding, 0);

        addView(mLeftTextTv, layoutParams);
        addView(mCenterLayout);
        addView(mRightTextTv, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public TitleBar setRightImageResource(int resId) {
        mRightTextTv.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public TitleBar setRightClickListener(OnClickListener l) {
        mRightTextTv.setOnClickListener(l);
        return this;
    }

    public TitleBar setRightText(CharSequence title) {
        mRightTextTv.setText(title);
        return this;
    }

    public TitleBar setRightTextSize(float size) {
        mRightTextTv.setTextSize(size);
        return this;
    }

    public TitleBar setRightTextColor(int color) {
        mRightTextTv.setTextColor(color);
        return this;
    }

    public TitleBar setRightVisible(boolean visible) {
        mRightTextTv.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBar setLeftImageResource(int resId) {
        mLeftTextTv.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public TitleBar setLeftClickListener(OnClickListener l) {
        mLeftTextTv.setOnClickListener(l);
        return this;
    }

    public TitleBar setLeftText(CharSequence title) {
        mLeftTextTv.setText(title);
        return this;
    }

    public TitleBar setLeftTextSize(float size) {
        mLeftTextTv.setTextSize(size);
        return this;
    }

    public TitleBar setLeftTextColor(int color) {
        mLeftTextTv.setTextColor(color);
        return this;
    }

    public TitleBar setLeftVisible(boolean visible) {
        mLeftTextTv.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBar setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
        return this;
    }

    private TitleBar setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
        return this;
    }

    public TitleBar setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
        return this;
    }

    public TitleBar setTitleColor(int resid) {
        mCenterText.setTextColor(resid);
        return this;
    }

    public TitleBar setTitleSize(float size) {
        mCenterText.setTextSize(size);
        return this;
    }

    public TitleBar setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
        return this;
    }

    public TitleBar setSubTitleColor(int resid) {
        mSubTitleText.setTextColor(resid);
        return this;
    }

    public TitleBar setSubTitleSize(float size) {
        mSubTitleText.setTextSize(size);
        return this;
    }

    public TitleBar setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
        return this;
    }

    public TitleBar setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
        return this;
    }

    public TitleBar setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
        return this;
    }

    public TitleBar setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
        return this;
    }

    public TitleBar setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
        return this;
    }

    public TextView getRightTextTv() {
        return mRightTextTv;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftTextTv, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightTextTv, widthMeasureSpec, heightMeasureSpec);
        if (mLeftTextTv.getMeasuredWidth() > mRightTextTv.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftTextTv.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightTextTv.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftTextTv.layout(0, mStatusBarHeight, mLeftTextTv.getMeasuredWidth(), mLeftTextTv.getMeasuredHeight() + mStatusBarHeight);
        mRightTextTv.layout(mScreenWidth - mRightTextTv.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightTextTv.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftTextTv.getMeasuredWidth() > mRightTextTv.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftTextTv.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftTextTv.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightTextTv.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightTextTv.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
