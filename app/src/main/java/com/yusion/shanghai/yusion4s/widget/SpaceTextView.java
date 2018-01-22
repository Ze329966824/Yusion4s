package com.yusion.shanghai.yusion4s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion4s.R;

/**
 * Created by ice on 2018/1/12.
 */

public class SpaceTextView extends LinearLayout {

    private float textSize;
    private int textColor;
    private String text;

    public SpaceTextView(Context context) {
        this(context, null);
    }

    public SpaceTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpaceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SpaceTextView);
        textSize = typedArray.getDimension(R.styleable.SpaceTextView_textSize, 30);
        textColor = typedArray.getColor(R.styleable.SpaceTextView_textColor, 0);
        String xmlText = typedArray.getString(R.styleable.SpaceTextView_text);
        if (!TextUtils.isEmpty(xmlText)) {
            text = xmlText.replaceAll(" ", "");
        }
        typedArray.recycle();


        if (text != null && text.length() > 1) {
            //两个以上字才有用
            for (int i = 0; i < text.length(); i++) {
                addTextView(String.valueOf(text.charAt(i)));
                addSpaceView();
            }
            removeViewAt(getChildCount() - 1);
        }
    }

    private void addTextView(String c) {
        addView(createTextView(c));
    }

    public TextView createTextView(String c) {
        TextView textView = new TextView(getContext());
        textView.setText(c);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        textView.setTextColor(textColor);
        return textView;
    }

    public View addSpaceView() {
        View view = new View(getContext());
        addView(view);
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        lp.weight = 1;
        return view;
    }


}
