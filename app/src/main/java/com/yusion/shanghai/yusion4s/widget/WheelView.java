package com.yusion.shanghai.yusion4s.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WheelView extends ScrollView {
    private static final int OFF_SET_DEFAULT = 1;
    private int mOffset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    private List<Object> mItems;
    private Context mContext;
    private LinearLayout mContentView;
    private int mDisplayItemCount; // 每页显示的数量
    private int mWidth;
    private OnSelectedListener mOnSelectedListener;
    private int mSelectedIndex = 1;//包含最前的offset
    private int itemHeight = 0;
    private int[] mSelectedAreaBorder; //两根横线的Y坐标
    private Paint mPaint;

    public interface OnSelectedListener {
        void onSelected(int selectedIndex, String item);
    }

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private int initialY;
    private Runnable scrollerTask;
    private int newCheck = 50;

    private void initData() {
        mDisplayItemCount = mOffset * 2 + 1;
        mContentView.removeAllViews();
        for (Object item : mItems) {
            mContentView.addView(createView(item.toString()));
        }
    }

    private TextView createView(String item) {
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(4);
        tv.setPadding(padding, padding, padding, padding);
        if (itemHeight == 0) {
            itemHeight = getViewMeasuredHeight(tv);
            mContentView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * mDisplayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * mDisplayItemCount));
        }
        return tv;
    }

    private void init(Context context) {
        mContext = context;
        setVerticalScrollBarEnabled(false);

        mContentView = new LinearLayout(context);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        addView(mContentView);

        scrollerTask = new Runnable() {
            public void run() {
                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        mSelectedIndex = divided + mOffset;
                        if (mOnSelectedListener != null) {
                            mOnSelectedListener.onSelected(mSelectedIndex, mItems.get(mSelectedIndex).toString());
                        }
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    mSelectedIndex = divided + mOffset + 1;
                                    if (mOnSelectedListener != null) {
                                        mOnSelectedListener.onSelected(mSelectedIndex, mItems.get(mSelectedIndex).toString());
                                    }
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    mSelectedIndex = divided + mOffset;
                                    if (mOnSelectedListener != null) {
                                        mOnSelectedListener.onSelected(mSelectedIndex, mItems.get(mSelectedIndex).toString());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void startScrollerTask() {
        initialY = getScrollY();
        postDelayed(scrollerTask, newCheck);
    }

    private int[] obtainSelectedAreaBorder() {
        if (mSelectedAreaBorder == null) {
            mSelectedAreaBorder = new int[2];
            mSelectedAreaBorder[0] = itemHeight * mOffset;
            mSelectedAreaBorder[1] = itemHeight * (mOffset + 1);
        }
        return mSelectedAreaBorder;
    }

    //添加分割线
    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (mWidth == 0) {
            mWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        }
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(Color.parseColor("#E1E3E6"));
            mPaint.setStrokeWidth((float) 3);
        }
        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(0, obtainSelectedAreaBorder()[0], mWidth, obtainSelectedAreaBorder()[0], mPaint);
                canvas.drawLine(0, obtainSelectedAreaBorder()[1], mWidth, obtainSelectedAreaBorder()[1], mPaint);
            }

            @Override
            public void setAlpha(int alpha) {
            }

            @Override
            public void setColorFilter(ColorFilter cf) {
            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        super.setBackgroundDrawable(background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        setBackgroundDrawable(null);
    }

    public void setSelection(final int position) {
        mSelectedIndex = position + mOffset;
        post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, position * itemHeight);
                if (mOnSelectedListener != null) {
                    mOnSelectedListener.onSelected(mSelectedIndex, mItems.get(mSelectedIndex).toString());
                }
            }
        });
    }

    public Object getSelectedItem() {
        return mItems.get(mSelectedIndex);
    }

    public int getSelectedIndex() {
        return mSelectedIndex - mOffset;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

    private List<Object> getItems() {
        return mItems;
    }

    public <T> void setItems(List<T> list) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.clear();
        mItems.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < mOffset; i++) {
            mItems.add(0, "");
            mItems.add("");
        }

        initData();
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public OnSelectedListener getOnSelectedListener() {
        return mOnSelectedListener;
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }
}