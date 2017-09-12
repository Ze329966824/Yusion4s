package com.yusion.shanghai.yusion4s.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ice on 2017/8/14.
 */

public class ProgressDataFetcher implements DataFetcher<InputStream> {

    private String url;
    private Call progressCall;
    private InputStream stream;
    private boolean isCancelled;
    private StatusImageRel imageView;

    public ProgressDataFetcher(String model, StatusImageRel imageView) {
        this.url = model;
        this.imageView = imageView;
    }


    //https://yusiontech-test.oss-cn-hangzhou.aliyuncs.com/CUSTOMER/17621066549/lender/income_proof/1504590096064.png?OSSAccessKeyId=STS.FjMDmxBirW7gP2xx1EGKGJEZG&security-token=CAIS9AF1q6Ft5B2yfSjIp6n4D9fMr7ZT4PWMUhTJnDEQS8RrpYDxpTz2IHxOfnBtAekbsPo%2FlWBZ6PYclqN6U4cATkjFYM1stgqOV84uINivgde8yJBZor%2FHcDHhJnyW9cvWZPqDP7G5U%2FyxalfCuzZuyL%2FhD1uLVECkNpv74vwOLK5gPG%2BCYCFBGc1dKyZ7tcYeLgGxD%2Fu2NQPwiWeiZygB%2BCgE0Dwjt%2Fnun5LCsUOP0AGrkdV4%2FdqhfsKWCOB3J4p6XtuP2%2Bh7S7HMyiY46WIRqP0s1fEcoGqZ4IHMUwgPs0ucUOPM6phoNxQ8aaUmCzu4ZDBEbRgTGoABdh8%2BXP9NKGnbuxVcDjwfhyA%2B8cw1LvitXE4Ht66awO7HVGtNKo39RPBGvwHt3fuiON8jEwcyxM10cH5J53oNJYcyP878AkpB3DsRFzcMzIp9hOv3I2iPbuG6RH6BJLqkvLPcj5W9ek751DPp3w%2FgxHOzjJ9q09enbY8zOgEscvM%3D&Expires=1505196180&Signature=bn%2BPrrvDNVMSNvEASji5ob8%2B1s8%3D
    ///storage/emulated/0/Pictures/1505112719048.jpg
    @Override
    public InputStream loadData(Priority priority) throws Exception {
//        if (!url.startsWith("http")) {
//            File file = new File(url);
//            stream = new FileInputStream(file);
//            Log.e("TAG", "loadData: ");
//            BufferedSource buffer = Okio.buffer(Okio.source(file));
//            stream = (InputStream) buffer;
//        } else {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new ProgressInterceptor(imageView));
        try {
            progressCall = client.newCall(request);
            Response response = progressCall.execute();
            if (isCancelled) {
                return null;
            }
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            stream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        }
        return stream;
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                stream = null;
            }
        }
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    @Override
    public String getId() {
        return url;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}