package com.yusion.shanghai.yusion4s.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.retrofit.service.AuthService;
import com.yusion.shanghai.yusion4s.retrofit.service.ConfigService;
import com.yusion.shanghai.yusion4s.retrofit.service.DlrService;
import com.yusion.shanghai.yusion4s.retrofit.service.OcrService;
import com.yusion.shanghai.yusion4s.retrofit.service.OrderService;
import com.yusion.shanghai.yusion4s.retrofit.service.UploadService;
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
    /**
     * 每个模块需要的retrofit对象不尽相同,通过传入serverUrl可以创建一个新实例
     */
    public static Retrofit createRetrofit(String serverUrl) {
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .serializeNulls()//null值也进行序列化并上传至服务器
                        .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())//null值序列化为""
                        .create()))
                .build();
    }


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

    public static UploadService getUploadService() {
        return retrofit.create(UploadService.class);
    }

    public static OcrService getOcrService() {
        return retrofit.create(OcrService.class);
    }

    public static AuthService getAuthService() {
        return retrofit.create(AuthService.class);
    }

    public static ConfigService getConfigService() {
        return retrofit.create(ConfigService.class);
    }

    public static OrderService getOrderService() {
        return retrofit.create(OrderService.class);
    }

    public static DlrService getDlrService() {
        return retrofit.create(DlrService.class);
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

    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }
    private static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

}
