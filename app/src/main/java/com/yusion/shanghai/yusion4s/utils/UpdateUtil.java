package com.yusion.shanghai.yusion4s.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yusion.shanghai.yusion4s.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


/**
 * Created by ice on 17/3/22.
 * ice is a big cow?
 */

public class UpdateUtil {

    private static UpdateDialog mUpdateDialog;

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "未能获取到版本信息";
        }
    }

    public static boolean checkUpdate(Context context, String serVersion) {
        if (getVersion(context).equals(serVersion)) {
            Toast.makeText(context, "本地版本和服务器版本一致，无需更新", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static void showUpdateDialog(Context context, String message, boolean isForce, String url) {
        if (mUpdateDialog == null) {
            mUpdateDialog = new UpdateDialog(context, message, isForce, url);
        }
        mUpdateDialog.show();
    }

    private static class UpdateDialog implements View.OnClickListener {

        private String mUrl;//apk地址
        private Context mContext;
        private Dialog mDialog;

        private TextView content;
        private RelativeLayout close;
        private LinearLayout ok;
        private View mDialogView;

        UpdateDialog(Context context, String message, boolean isForce, String url) {
            mContext = context;
            mUrl = url;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mDialogView = inflater.inflate(R.layout.dialog_update, null);

            content = (TextView) mDialogView.findViewById(R.id.update_content);
            content.setText(message);

            close = (RelativeLayout) mDialogView.findViewById(R.id.update_close);
            close.setOnClickListener(this);

            ok = (LinearLayout) mDialogView.findViewById(R.id.update_ok);
            ok.setOnClickListener(this);

            if (isForce) {
                close.setVisibility(View.GONE);
            }

            mDialog = new Dialog(mContext, R.style.MyDialogStyle);
            mDialog.setContentView(mDialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        void show() {
            if (mDialog != null) {
                mDialog.show();
            }
        }

        void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        @Override
        public void onClick(View v) {
            dismiss();
            int i = v.getId();
            if (i == R.id.update_ok) {
                //                toLocalUpdate();
                toWebUpdate();
            }
        }

        private void toWebUpdate() {
            Intent intent =new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUrl));
            mContext.startActivity(intent);
        }

        private void toLocalUpdate() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                final ProgressDialog progressDialog = new ProgressDialog(mContext);    //进度条，在下载的时候实时更新进度，提高用户友好度
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("正在下载");
                progressDialog.setMessage("请稍候...");
                progressDialog.setProgress(0);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                final String apkName = String.format(Locale.CHINA, "WangDai%s.apk", getVersion(mContext));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(mUrl);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.connect();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            progressDialog.setMax(httpURLConnection.getContentLength());

                            FileOutputStream fileOutputStream = null;
                            if (inputStream != null) {
                                File file = new File(Environment.getExternalStorageDirectory(), apkName);
                                fileOutputStream = new FileOutputStream(file);
                                byte[] buf = new byte[1024];
                                int bytes;
                                int progress = 0;
                                while ((bytes = inputStream.read(buf)) != -1) {
                                    fileOutputStream.write(buf, 0, bytes);
                                    progress += bytes;
                                    progressDialog.setProgress(progress);
                                }
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                            progressDialog.cancel();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), apkName)), "application/vnd.android.package-archive");
                            mContext.startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                Toast.makeText(mContext, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

