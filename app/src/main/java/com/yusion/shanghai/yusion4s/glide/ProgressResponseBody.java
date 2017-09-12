package com.yusion.shanghai.yusion4s.glide;


import android.util.Log;
import android.view.View;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by ice on 2017/8/14.
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private StatusImageRel imageView;

    public ProgressResponseBody(ResponseBody body, StatusImageRel imageView) {
        this.responseBody = body;
        this.imageView = imageView;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return responseBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            try {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                final long contentLength = responseBody.contentLength();
                StatusImageRel statusImageRel = (StatusImageRel) imageView;

                statusImageRel.getProgressPro().post(new Runnable() {
                    @Override
                    public void run() {
                        float v = totalBytesRead * 100f / contentLength;
                        statusImageRel.getProgressPro().setProgress(v);
                        if (v == 100) {
                            statusImageRel.getProgressPro().setVisibility(View.GONE);
                        } else {
                            statusImageRel.getProgressPro().setVisibility(View.VISIBLE);
                        }
                        Log.e("TAG", "totalBytesRead: " + totalBytesRead + "......." + "contentLength" + contentLength);
                    }
                });
                return bytesRead;
            }
        };
    }
}
