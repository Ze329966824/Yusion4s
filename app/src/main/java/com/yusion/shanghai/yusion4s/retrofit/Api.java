package com.yusion.shanghai.yusion4s.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.retrofit.service.OcrService;
import com.yusion.shanghai.yusion4s.settings.Settings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ice on 2017/8/3.
 */

public class Api {
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request realRequest = request.newBuilder()
                                .method(request.method(), request.body())
                                .addHeader("authentication", String.format(Locale.CHINA, "token %s", TextUtils.isEmpty(Yusion4sApp.TOKEN) ? Settings.TEST_TOKEN : Yusion4sApp.TOKEN))
                                .build();
//                        logRequestInfo(realRequest);
                        Response response = chain.proceed(realRequest);
                        logResponseInfo(response);
                        return response;
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.SERVER_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static OcrService getOcrService() {
        return retrofit.create(OcrService.class);
    }


    private static void logRequestInfo(Request request) {
        Log.e("API", "\n");
        Log.e("API", "\n******** log request start ******** ");
        Log.e("API", "url: " + request.url());
        Log.e("API", "method: " + request.method());
        Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            Log.e("API", headers.name(i) + " : " + headers.value(i));
        }

        //如果是post请求还需打印参数
        String method = request.method();
        if ("POST".equals(method)) {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String paramsStr = buffer.readString(Charset.forName("UTF-8")).replaceAll("\"", "\\\"");
            Log.e("API", "requestParameters: " + paramsStr);
        }

        Log.e("API", "******** log request end ********\n");
        Log.e("API", "\n");
    }

    private static void logResponseInfo(Response response) {
        logRequestInfo(response.request());
//        try {
//            // TODO: 2017/8/3 需要解决
//            Log.e("API", "responseBody: " + response.body().string());//流文件只能取一次
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
