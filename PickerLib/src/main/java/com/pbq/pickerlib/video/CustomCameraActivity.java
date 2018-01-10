package com.pbq.pickerlib.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.MediaInfo;
import com.pbq.pickerlib.R;
import com.qu.preview.callback.OnFrameCallBack;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class CustomCameraActivity extends AppCompatActivity {

    private AliyunIRecorder recorder;

    private boolean isRecording = false;
    private boolean hasRecorded = false;

    private int maxDuration = 5 * 1000;//最多5分钟
    private boolean isOpenFailed = false;
    public static final int RESOLUTION_360P = 0;
    public static final int RESOLUTION_480P = 1;
    public static final int RESOLUTION_540P = 2;
    public static final int RESOLUTION_720P = 3;
    private int mResolutionMode = RESOLUTION_540P;
    private ImageView recordBtn;
    private TextView recordTimeTv;
    private AliyunIClipManager clipManager;
    private RecordTimelineView recordTimelineView;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private MediaScannerConnection mediaScanner;
    private ProgressDialog initDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);

        initDialog = new ProgressDialog(this);
        initDialog.setMessage("正在初始化摄像头...");
        initDialog.setCancelable(false);
        initDialog.show();

        QupaiHttpFinal.getInstance().initOkHttpFinal();
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("QuCore");
        MyGlSurfaceView sv = (MyGlSurfaceView) findViewById(R.id.preview);
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() >= 2) {
                    scaleGestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });
        recordBtn = ((ImageView) findViewById(R.id.record_btn));
        recordTimeTv = ((TextView) findViewById(R.id.record_time));
        recordTimelineView = (RecordTimelineView) findViewById(R.id.record_timeline);
        recordTimelineView.setColor(Color.parseColor("#FF06B7A3"), R.color.colorPrimary, Color.parseColor("#b3000000"), Color.parseColor("#D8D8D8"));
        recordTimelineView.setMaxDuration(maxDuration);

        recorder = AliyunRecorderCreator.getRecorderInstance(this);
        recorder.setDisplayView(sv);
        recorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int i, int i1, Camera.CameraInfo cameraInfo) {
            }

            @Override
            public void openFailed() {
                Toast.makeText(CustomCameraActivity.this, "请打开摄像机权限", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        clipManager = recorder.getClipManager();
        clipManager.setMaxDuration(maxDuration);
        recorder.setVideoQuality(VideoQuality.EPD);
        recorder.setMediaInfo(createMediaInfo());
        recorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(boolean b, long l) {
                Log.e("TAG", "onComplete() called with: b = [" + b + "], l = [" + l + "]");
            }

            @Override
            public void onFinish(String outputPath) {
                Log.e("TAG", "onFinish() called with: outputPath = [" + outputPath + "]");
                mediaScanner.scanFile(outputPath, "video/mp4");
            }

            @Override
            public void onProgress(final long duration) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordTimelineView.setDuration((int) duration);
                        int time = (int) (duration) / 1000;
                        int min = time / 60;
                        int sec = time % 60;
                        recordTimeTv.setText(String.format(Locale.CHINA, "%02d:%02d", min, sec));
                    }
                });
            }

            @Override
            public void onMaxDuration() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                Toast.makeText(CustomCameraActivity.this, "拍摄已达到支持的最长时间", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onMaxDuration() called");
                hasRecorded = true;
                isRecording = false;
                stopRecording();
                recordBtn.setImageResource(R.mipmap.use_record);
            }

            @Override
            public void onError(int i) {
                Log.e("TAG", "onError() called with: i = [" + i + "]");
            }

            @Override
            public void onInitReady() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordBtn.setVisibility(View.VISIBLE);
                        initDialog.dismiss();
                    }
                });
            }

            @Override
            public void onPictureBack(Bitmap bitmap) {
                Log.e("TAG", "onPictureBack() called with: bitmap = [" + bitmap + "]");
            }

            @Override
            public void onPictureDataBack(byte[] bytes) {
                Log.e("TAG", "onPictureDataBack() called with: bytes = [" + bytes + "]");
            }
        });

        mediaScanner = new MediaScannerConnection(this, null);
        mediaScanner.connect();
        initData();
    }

    private void initData() {
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording && !hasRecorded) {
                    //开始录制
                    isRecording = true;
                    startRecording();
                    recordBtn.setImageResource(R.mipmap.stop_record);
                } else if (!hasRecorded) {
                    hasRecorded = true;
                    isRecording = false;
                    stopRecording();
                    recordBtn.setImageResource(R.mipmap.use_record);
                } else {
                    onRecordingComplete();
                }
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {

            private float scaleFactor;
            private float lastScaleFactor;

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float factorOffset = detector.getScaleFactor() - lastScaleFactor;
                scaleFactor += factorOffset;
                lastScaleFactor = detector.getScaleFactor();
                if (scaleFactor < 0) {
                    scaleFactor = 0;
                }
                if (scaleFactor > 1) {
                    scaleFactor = 1;
                }
                recorder.setZoom(scaleFactor);
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                lastScaleFactor = detector.getScaleFactor();
                return true;
            }
        });
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.e("TAG", "onDown() called with: e = [" + e + "]");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.e("TAG", "onShowPress() called with: e = [" + e + "]");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.e("TAG", "onSingleTapUp() called with: e = [" + e + "]");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e("TAG", "onScroll() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.e("TAG", "onLongPress() called with: e = [" + e + "]");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e("TAG", "onFling() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
                return false;
            }
        });
    }

    private void onRecordingComplete() {
        isRecording = false;
        Intent intent = new Intent();
        List<String> pathList = clipManager.getVideoPathList();
        intent.putExtra("video_path", pathList.get(0));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void stopRecording() {
        recorder.stopRecording();
        recorder.finishRecording();
    }

    private void startRecording() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "YusionTech");
        if (!dir.exists()) {
            dir.mkdir();
        }
        recorder.setOutputPath(dir + File.separator);
        recorder.startRecording();
    }


    @Override
    public void onBackPressed() {
        if (isRecording) {
            Toast.makeText(this, "正在录制视频，无法退出该页面", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private int[] getResolution() {
        int[] resolution = new int[2];
        int width = 0;
        int height = 0;
        switch (mResolutionMode) {
            case RESOLUTION_360P:
                width = 360;
                break;
            case RESOLUTION_480P:
                width = 480;
                break;
            case RESOLUTION_540P:
                width = 540;
                break;
            case RESOLUTION_720P:
                width = 720;
                break;
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        height = metrics.heightPixels * width / metrics.widthPixels;
        Log.e("TAG", String.format(Locale.CHINA, "getResolution: width = %-4d , height = %-4d", width, height));
        resolution[0] = width;
        resolution[1] = height;
        return resolution;
    }

    private MediaInfo createMediaInfo() {
        int[] resolution = getResolution();
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setVideoWidth(resolution[0]);
        mediaInfo.setVideoHeight(resolution[1]);
        mediaInfo.setHWAutoSize(true);
        return mediaInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaScanner.disconnect();
        recorder.destroy();
        AliyunRecorderCreator.destroyRecorderInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recorder.setCamera(CameraType.BACK);
        recorder.startPreview();

        Field aliyunCameraField = null;
        try {
            aliyunCameraField = recorder.getClass().getDeclaredField("aliyunCamera");
            aliyunCameraField.setAccessible(true);
            Object aliyunCamera = aliyunCameraField.get(recorder);
            Field cameraProxyField = aliyunCamera.getClass().getDeclaredField("cameraProxy");
            cameraProxyField.setAccessible(true);
            final Object cameraProxy = cameraProxyField.get(aliyunCamera);

            Field cameraField = cameraProxy.getClass().getDeclaredField("camera");
            cameraField.setAccessible(true);
            final Camera[] camera = {(Camera) cameraField.get(cameraProxy)};
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (camera[0] == null) {
                        try {
                            Thread.sleep(100);
                            Field cameraField = cameraProxy.getClass().getDeclaredField("camera");
                            cameraField.setAccessible(true);
                            camera[0] = (Camera) cameraField.get(cameraProxy);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }

                    final Field currentParamsField;
                    try {
                        currentParamsField = cameraProxy.getClass().getDeclaredField("currentParams");
                        currentParamsField.setAccessible(true);
                        final Camera.Parameters currentParams = (Camera.Parameters) currentParamsField.get(cameraProxy);
                        currentParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//设置focusMode
                        camera[0].setParameters(currentParams);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
