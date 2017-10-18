package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.bean.oss.GetOssTokenBean;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.retrofit.Api;
import com.yusion.shanghai.yusion4s.retrofit.api.OssApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ice on 2017/8/4.
 */

public class OssUtil {

    public static void uploadOss(final Context context, boolean showDialog, final String localPath, @NonNull OSSObjectKeyBean objectKeyBean, @NonNull final OnItemDataCallBack<String> onOssSuccessCallBack, final OnItemDataCallBack<Throwable> onFailureCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        if (showDialog) {
            dialog.show();
        }

        Map<String, String> body = new LinkedHashMap<>();
        body.put("duration_second", "1800");
        body.put("method", "put");
        body.put("timestamp", new Date().getTime() + "");
        body.put("signature", getSignature(body));
        OssApi.ossService.getOSSToken(body).enqueue(new Callback<GetOssTokenBean>() {
            @Override
            public void onResponse(Call<GetOssTokenBean> call, Response<GetOssTokenBean> response) {
                Log.e(Api.getTag(call.request()), "onResponse: " + response.body());
                GetOssTokenBean ossTokenBean = response.body();
                final String objectKey = getObjectKey(objectKeyBean.role, objectKeyBean.category, objectKeyBean.suffix);
                PutObjectRequest request = new PutObjectRequest(ossTokenBean.FidDetail.Bucket, objectKey, localPath);

                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossTokenBean.AccessKeyId, ossTokenBean.AccessKeySecret, ossTokenBean.SecurityToken);
                OSS oss = new OSSClient(context, ossTokenBean.FidDetail.Region, credentialProvider);
                SharedPrefsUtil.getInstance(context).putValue("region", ossTokenBean.FidDetail.Region);
                SharedPrefsUtil.getInstance(context).putValue("bucket", ossTokenBean.FidDetail.Bucket);

                oss.asyncPutObject(request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        if (showDialog) {
                            dialog.dismiss();
                        }
                        onOssSuccessCallBack.onItemDataCallBack(request.getObjectKey());
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        if (showDialog) {
                            dialog.dismiss();
                        }
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                            if (onFailureCallBack != null) {
                                onFailureCallBack.onItemDataCallBack(clientExcepion);
                            }
                        }
                        if (serviceException != null) {
                            // 服务异常
                            serviceException.printStackTrace();
                            if (onFailureCallBack != null) {
                                onFailureCallBack.onItemDataCallBack(serviceException);
                            }
                        }
                        String errorInfo = "onFailure() called with: request = [" + request + "], clientExcepion = [" + clientExcepion + "], serviceException = [" + serviceException + "]";
                        Sentry.capture(errorInfo);
                        Log.e("TAG", errorInfo);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetOssTokenBean> call, Throwable t) {
                if (showDialog) {
                    dialog.dismiss();
                }
                if (onFailureCallBack != null) {
                    onFailureCallBack.onItemDataCallBack(t);
                }
                String errorInfo = "onFailure() called with: call = [" + call + "], t = [" + t + "]";
                Sentry.capture(errorInfo);
                Log.e("TAG", errorInfo);
            }
        });
    }

//    public static void uploadOss(final Context context, Dialog dialog, final String localPath, @NonNull OSSObjectKeyBean objectKeyBean, @NonNull final OnItemDataCallBack<String> onSuccessCallBack, final OnItemDataCallBack<Throwable> onFailureCallBack) {
//        Map<String, String> body = new LinkedHashMap<>();
//        body.put("duration_second", "1800");
//        body.put("method", "put");
//        body.put("timestamp", new Date().getTime() + "");
//        body.put("signature", getSignature(body));
//        Call<GetOssTokenBean> call = OssApi.retrofit.create(OssService.class).getOSSToken(body);
//        dialog.setOnCancelListener(it -> call.cancel());
//        call.enqueue(new Callback<GetOssTokenBean>() {
//            @Override
//            public void onResponse(Call<GetOssTokenBean> call, Response<GetOssTokenBean> response) {
//                GetOssTokenBean ossTokenBean = response.body();
//                final String objectKey = getObjectKey(objectKeyBean.role, objectKeyBean.category, objectKeyBean.suffix);
//                PutObjectRequest request = new PutObjectRequest(ossTokenBean.FidDetail.Bucket, objectKey, localPath);
//
//                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossTokenBean.AccessKeyId, ossTokenBean.AccessKeySecret, ossTokenBean.SecurityToken);
//                OSS oss = new OSSClient(context, ossTokenBean.FidDetail.Region, credentialProvider);
//                SharedPrefsUtil.getInstance(context).putValue("region", ossTokenBean.FidDetail.Region);
//                SharedPrefsUtil.getInstance(context).putValue("bucket", ossTokenBean.FidDetail.Bucket);
//
////                request.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
////                    @Override
////                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
////                        Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
////                    }
////                });
//                oss.asyncPutObject(request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//                    @Override
//                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        onSuccessCallBack.onItemDataCallBack(request.getObjectKey());
//                    }
//
//                    @Override
//                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        if (clientExcepion != null) {
//                            // 本地异常如网络异常等
//                            clientExcepion.printStackTrace();
//                            if (onFailureCallBack != null) {
//                                onFailureCallBack.onItemDataCallBack(clientExcepion);
//                            }
//                        }
//                        if (serviceException != null) {
//                            // 服务异常
//                            serviceException.printStackTrace();
//                            if (onFailureCallBack != null) {
//                                onFailureCallBack.onItemDataCallBack(serviceException);
//                            }
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<GetOssTokenBean> call, Throwable t) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                t.printStackTrace();
//                if (onFailureCallBack != null) {
//                    onFailureCallBack.onItemDataCallBack(t);
//                }
//            }
//        });
//    }
//
//    public static void uploadOss(final Context context, boolean showDialog, final String localPath, @NonNull OSSObjectKeyBean objectKeyBean, @NonNull final OnItemDataCallBack<String> onSuccessCallBack, final OnItemDataCallBack<Throwable> onFailureCallBack) {
//        Dialog dialog = LoadingUtils.createLoadingDialog(context);
//        if (showDialog) {
//            dialog.show();
//        }
//        Map<String, String> body = new LinkedHashMap<>();
//        body.put("duration_second", "1800");
//        body.put("method", "put");
//        body.put("timestamp", new Date().getTime() + "");
//        body.put("signature", getSignature(body));
//        OssApi.retrofit.create(OssService.class).getOSSToken(body).enqueue(new Callback<GetOssTokenBean>() {
//            @Override
//            public void onResponse(Call<GetOssTokenBean> call, Response<GetOssTokenBean> response) {
//                GetOssTokenBean ossTokenBean = response.body();
//                final String objectKey = getObjectKey(objectKeyBean.role, objectKeyBean.category, objectKeyBean.suffix);
//                PutObjectRequest request = new PutObjectRequest(ossTokenBean.FidDetail.Bucket, objectKey, localPath);
//
//                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossTokenBean.AccessKeyId, ossTokenBean.AccessKeySecret, ossTokenBean.SecurityToken);
//                OSS oss = new OSSClient(context, ossTokenBean.FidDetail.Region, credentialProvider);
//                SharedPrefsUtil.getInstance(context).putValue("region", ossTokenBean.FidDetail.Region);
//                SharedPrefsUtil.getInstance(context).putValue("bucket", ossTokenBean.FidDetail.Bucket);
//
////                request.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
////                    @Override
////                    public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
////                        Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
////                    }
////                });
//                oss.asyncPutObject(request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//                    @Override
//                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                        if (showDialog) {
//                            dialog.dismiss();
//                        }
//                        onSuccessCallBack.onItemDataCallBack(request.getObjectKey());
//                    }
//
//                    @Override
//                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                        if (showDialog) {
//                            dialog.dismiss();
//                        }
//                        if (clientExcepion != null) {
//                            // 本地异常如网络异常等
//                            clientExcepion.printStackTrace();
//                            if (onFailureCallBack != null) {
//                                onFailureCallBack.onItemDataCallBack(clientExcepion);
//                            }
//                        }
//                        if (serviceException != null) {
//                            // 服务异常
//                            serviceException.printStackTrace();
//                            if (onFailureCallBack != null) {
//                                onFailureCallBack.onItemDataCallBack(serviceException);
//                            }
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<GetOssTokenBean> call, Throwable t) {
//                if (showDialog) {
//                    dialog.dismiss();
//                }
//                t.printStackTrace();
//                if (onFailureCallBack != null) {
//                    onFailureCallBack.onItemDataCallBack(t);
//                }
//            }
//        });
//    }

    /**
     * @param client
     * @param role
     * @param category
     * @param suffix   eg: .png .mp4
     * @return
     */
    public static String getObjectKey(String client, String account, String role, String category, String suffix) {
        return client + "/" + account + "/" + role + "/" + category + "/" + System.currentTimeMillis() + suffix;
    }

    public static String getObjectKey(String role, String category, String suffix) {
        return getObjectKey("CUSTOMER", Yusion4sApp.ACCOUNT, role, category, suffix);
    }

    public static String getSignature(Map<String, String> map) {
        StringBuilder urlReq = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            urlReq.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return stringToSign(urlReq.substring(0, urlReq.length() - 1));
    }

    public static String stringToSign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            String REGISTER_HMAC_KEY = "temp";
            SecretKeySpec secret = new SecretKeySpec(REGISTER_HMAC_KEY.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(secret);
            return Base64.encodeToString(mac.doFinal(data.getBytes()), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.e("HMAC", "Hash algorithm SHA-1 is not supported", e);
        } catch (UnsupportedEncodingException e) {
            Log.e("HMAC", "Encoding UTF-8 is not supported", e);
        } catch (InvalidKeyException e) {
            Log.e("HMAC", "Invalid key", e);
        }
        return "";
    }
}
