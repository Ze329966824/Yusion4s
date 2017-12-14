package com.yusion.shanghai.yusion4s.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

/**
 * Created by ice on 2017/8/29.
 */
//禁止用户输入空格
public class NoEmptyEditText extends android.support.v7.widget.AppCompatEditText {
    public NoEmptyEditText(Context context) {
        super(context);
        init();
    }

    public NoEmptyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoEmptyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals(" ") || source.toString().contentEquals("\n"))
                return "";
            else
                return null;
        }});
    }
}
