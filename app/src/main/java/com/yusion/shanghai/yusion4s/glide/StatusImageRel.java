package com.yusion.shanghai.yusion4s.glide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.widget.DonutProgress;


/**
 * Created by ice on 2017/9/11.
 */

public class StatusImageRel extends RelativeLayout {
    public DonutProgress progressPro;
    public GlideImageView sourceImg;
    public ImageView cbImg;

    public StatusImageRel(Context context) {
        this(context, null);
    }

    public StatusImageRel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusImageRel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.status_img_rel, this, true);
        progressPro = ((DonutProgress) contentView.findViewById(R.id.status_img_loading_progress));
        cbImg = ((ImageView) contentView.findViewById(R.id.status_img_cb_img));
        sourceImg = ((GlideImageView) contentView.findViewById(R.id.status_img_source_img));
    }

    public DonutProgress getProgressPro() {
        return progressPro;
    }

    public GlideImageView getSourceImg() {
        return sourceImg;
    }
}
