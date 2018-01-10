package com.pbq.pickerlib.video;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.CommonUtil;
import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIClipManager;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.effect.EffectFilter;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.recorder.MediaInfo;
import com.pbq.pickerlib.R;
import com.qu.preview.callback.OnFrameCallBack;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

//toEditor(); 完成按钮
public class AlibabaStandardActivity extends Activity implements View.OnClickListener, View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener {
    private static final int RATIO_MODE_3_4 = 0;
    private static final int RATIO_MODE_1_1 = 1;
    private static final int RATIO_MODE_9_16 = 2;
    public static final int RESOLUTION_360P = 0;
    public static final int RESOLUTION_480P = 1;
    public static final int RESOLUTION_540P = 2;
    public static final int RESOLUTION_720P = 3;
    private static final int BEAUTY_LEVEL = 80;
    private static final int TIMELINE_HEIGHT = 20;
    public static final String VIDEO_RESOLUTION = "video_resolution";
    public static final String MIN_DURATION = "min_duration";
    public static final String MAX_DURATION = "max_duration";
    public static final String VIDEO_QUALITY = "video_quality";
    public static final String GOP = "gop";
    private static final int MAX_SWITCH_VELOCITY = 2000;
    private static final float FADE_IN_START_ALPHA = 0.3f;
    private static final int FILTER_ANIMATION_DURATION = 1000;
    String[] eff_dirs;
    private int mResolutionMode;
    private int minDuration;
    private int maxDuration;
    private int rotation;
    private int recordRotation;
    private int filterIndex = 0;
    private VideoQuality videoQuality;
    private int mRatioMode = RATIO_MODE_3_4;
    private AliyunIRecorder mRecorder;
    private AliyunIClipManager mClipManager;
    private GLSurfaceView mGlSurfaceView;
    private boolean isBeautyOn = true;
    private boolean isSelected = false;
    private RecordTimelineView mRecordTimelineView;
    private ImageView mSwitchLightBtn;
    private ImageView mBackBtn;
    private ImageView mRecordBtn;
    //    private ImageView mDeleteBtn;
//    private ImageView mCompleteBtn;
//    private ImageView mIconDefault;
    private TextView mRecordTimeTxt, filterTxt;
    private FrameLayout mToolBar, mRecorderBar;
    private FlashType mFlashType = FlashType.OFF;
    private CameraType mCameraType = CameraType.BACK;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor;
    private float lastScaleFactor;
    private float exposureCompensationRatio = 0.5f;
    private boolean isOnMaxDuration;
    private boolean isOpenFailed;
    private OrientationDetector orientationDetector;
    private boolean isRecording = false;
    private long downTime;
    private boolean isRecordError;
    private MediaScannerConnection mMediaScanner;
    private FrameLayout mPreviewFrame;
    private ImageView mPlaytBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_alibaba_standard);
        initOritationDetector();
        getData();
        initView();
        initSDK();
        reSizePreview();
        mMediaScanner = new MediaScannerConnection(this, null);
        mMediaScanner.connect();
    }

    private void reSizePreview() {
        RelativeLayout.LayoutParams previewParams = null;
        RelativeLayout.LayoutParams timeLineParams = null;
        RelativeLayout.LayoutParams durationTxtParams = null;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        switch (mRatioMode) {
            case RATIO_MODE_1_1:
                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth);
                previewParams.addRule(RelativeLayout.BELOW, R.id.tools_bar);
//                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
//                timeLineParams.addRule(RelativeLayout.BELOW, R.id.preview);
//                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.record_timeline);
                mToolBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mRecorderBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mRecordTimelineView.setColor(Color.parseColor("#EF4B81"), Color.parseColor("#FF0000"), Color.parseColor("#FFFFFF"), Color.parseColor("#D8D8D8"));
                break;
            case RATIO_MODE_3_4:
                int barHeight = getVirtualBarHeigh();
                float ratio = (float) screenHeight / screenWidth;
                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 4 / 3);
                if (barHeight > 0 || ratio < (16f / 9.2f)) {
                    mToolBar.setBackgroundColor(Color.parseColor("#881B2133"));
                } else {
                    previewParams.addRule(RelativeLayout.BELOW, R.id.tools_bar);
                    mToolBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
//                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
//                timeLineParams.addRule(RelativeLayout.BELOW, R.id.preview);
//                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.record_timeline);
                mRecorderBar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mRecordTimelineView.setColor(Color.parseColor("#EF4B81"), Color.parseColor("#FF0000"), Color.parseColor("#FFFFFF"), Color.parseColor("#D8D8D8"));
                break;
            case RATIO_MODE_9_16:
                previewParams = new RelativeLayout.LayoutParams(screenWidth, screenWidth * 16 / 9);
                if (previewParams.height > screenHeight) {
                    previewParams.height = screenHeight;
                }
//                timeLineParams = new RelativeLayout.LayoutParams(screenWidth, TIMELINE_HEIGHT);
//                timeLineParams.addRule(RelativeLayout.ABOVE, R.id.record_layout);
//                durationTxtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                durationTxtParams.addRule(RelativeLayout.ABOVE, R.id.record_timeline);
                mToolBar.setBackgroundColor(Color.parseColor("#881B2133"));
                mRecorderBar.setBackgroundColor(Color.parseColor("#881B2133"));
                mRecordTimelineView.setColor(Color.parseColor("#EF4B81"), Color.parseColor("#FF0000"), Color.parseColor("#FFFFFF"), Color.parseColor("#00ffffff"));
                break;
        }
        if (previewParams != null) {
//            mGlSurfaceView.setLayoutParams(previewParams);
            mPreviewFrame.setLayoutParams(previewParams);
        }
        if (timeLineParams != null) {
            mRecordTimelineView.setLayoutParams(timeLineParams);
        }
        if (durationTxtParams != null) {
            mRecordTimeTxt.setLayoutParams(durationTxtParams);
        }
    }

    private void initOritationDetector() {
        orientationDetector = new OrientationDetector(getApplicationContext());
        orientationDetector.setOrientationChangedListener(new OrientationDetector.OrientationChangedListener() {
            @Override
            public void onOrientationChanged() {
                rotation = getPictureRotation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isRecording) {
            super.onBackPressed();
        }
    }

    private boolean hasStratRecord = false;

    private void initView() {
        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.preview);
        mPreviewFrame = (FrameLayout) findViewById(R.id.preview_frame);
        mGlSurfaceView.setOnTouchListener(this);
        mSwitchLightBtn = (ImageView) findViewById(R.id.switch_light);
        mPlaytBtn = (ImageView) findViewById(R.id.play_btn);
        mPlaytBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlibabaStandardActivity.this, "播放视频", Toast.LENGTH_SHORT).show();
            }
        });
        mSwitchLightBtn.setImageResource(R.mipmap.icon_light_dis);
        mSwitchLightBtn.setOnClickListener(this);
        mBackBtn = (ImageView) findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
        mRecordBtn = (ImageView) findViewById(R.id.record_btn);
        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenFailed) {
                    Toast.makeText(AlibabaStandardActivity.this, "请开启摄像头权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!hasStratRecord) {
                    hasStratRecord = true;
                    startRecording();
                    mRecordBtn.setImageResource(R.mipmap.stop_record);
                } else {
                    hasStratRecord = false;
                    stopRecording();
                }
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                downTime = System.currentTimeMillis();
//                if (!isRecording) {
//                    if (!checkIfStartRecording()) {
//                        return false;
//                    }
//                    mRecordBtn.setPressed(true);
//                    startRecording();
//                    mRecordBtn.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mRecordBtn.isPressed()) {
//                                mRecordBtn.setSelected(true);
//                                mRecordBtn.setHovered(false);
//                            }
//                        }
//                    }, 200);
//                    isRecording = true;
//                } else {
//                    stopRecording();
//                    isRecording = false;
//                }
//            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                long timeOffset = System.currentTimeMillis() - downTime;
//                mRecordBtn.setPressed(false);
//                if (timeOffset > 1000) {
//                    if (isRecording) {
//                        stopRecording();
//                        isRecording = false;
//                    }
//                } else {
//                    if (!isRecordError) {
//                        mRecordBtn.setSelected(false);
//                        mRecordBtn.setHovered(true);
//                    } else {
//                        isRecording = false;
//                    }
//                }
//            }
            }
        });
//        mRecordBtn.setOnTouchListener(this);
//        mDeleteBtn = (ImageView) findViewById(R.id.delete_btn);
//        mDeleteBtn.setOnClickListener(this);
//        mCompleteBtn = (ImageView) findViewById(R.id.complete_btn);
//        mCompleteBtn.setOnClickListener(this);
        mRecordTimelineView = (RecordTimelineView) findViewById(R.id.record_timeline);
        mRecordTimelineView.setColor(Color.parseColor("#EF4B81"), R.color.colorPrimary, Color.parseColor("#b3000000"), Color.parseColor("#D8D8D8"));
        mRecordTimeTxt = (TextView) findViewById(R.id.record_time);
        filterTxt = (TextView) findViewById(R.id.filter_txt);
//        mIconDefault = (ImageView) findViewById(R.id.icon_default);
        mToolBar = (FrameLayout) findViewById(R.id.tools_bar);
        mRecorderBar = (FrameLayout) findViewById(R.id.record_layout);
//        mIconDefault.setOnClickListener(this);
        scaleGestureDetector = new ScaleGestureDetector(this, this);
        gestureDetector = new GestureDetector(this, this);
    }

    private void initSDK() {
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("QuCore");
        mRecorder = AliyunRecorderCreator.getRecorderInstance(this);
        mRecorder.setDisplayView(mGlSurfaceView);
        mRecorder.setOnFrameCallback(new OnFrameCallBack() {
            @Override
            public void onFrameBack(byte[] bytes, int width, int height, Camera.CameraInfo info) {
                isOpenFailed = false;
            }

            @Override
            public void openFailed() {
                isOpenFailed = true;
            }
        });
        mClipManager = mRecorder.getClipManager();
        mClipManager.setMinDuration(minDuration);
        mClipManager.setMaxDuration(60000 * 5);
        mRecordTimelineView.setMaxDuration(mClipManager.getMaxDuration());
        mRecordTimelineView.setMinDuration(mClipManager.getMinDuration());
        int[] resolution = getResolution();
        MediaInfo info = new MediaInfo();
        info.setVideoWidth(resolution[0]);
        info.setVideoHeight(resolution[1]);
        info.setHWAutoSize(true);//硬编时自适应宽高为16的倍数
        mRecorder.setMediaInfo(info);
        mCameraType = mRecorder.getCameraCount() == 1 ? CameraType.BACK : mCameraType;
        mRecorder.setCamera(mCameraType);
        mRecorder.setBeautyLevel(BEAUTY_LEVEL);
        mRecorder.setBeautyStatus(isBeautyOn);
        mRecorder.setVideoQuality(VideoQuality.EPD);
        mRecorder.setRecordCallback(new RecordCallback() {
            @Override
            public void onComplete(boolean validClip, long clipDuration) {
                handleRecordCallback(validClip, clipDuration);
                if (isOnMaxDuration) {
                    isOnMaxDuration = false;
                    isRecording = false;
                    toEditor();
                }
            }

            @Override
            public void onFinish(String outputPath) {
                mMediaScanner.scanFile(outputPath, "video/mp4");
            }

            @Override
            public void onProgress(final long duration) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecordTimelineView.setDuration((int) duration);
                        int time = (int) (mClipManager.getDuration() + duration) / 1000;
                        int min = time / 60;
                        int sec = time % 60;
                        mRecordTimeTxt.setText(String.format("%1$02d:%2$02d", min, sec));
                        if (mRecordTimeTxt.getVisibility() != View.VISIBLE) {
                            mRecordTimeTxt.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }

            @Override
            public void onMaxDuration() {
                isOnMaxDuration = true;
            }

            @Override
            public void onError(int errorCode) {
                Log.e("record", "record eoor" + errorCode);
                isRecordError = true;
                handleRecordCallback(false, 0);
            }

            @Override
            public void onInitReady() {

            }

            @Override
            public void onPictureBack(Bitmap bitmap) {

            }

            @Override
            public void onPictureDataBack(byte[] data) {

            }
        });
//        mRecorder.setFocusMode(CameraParam.FOCUS_MODE_CONTINUE);
        mRecorder.setExposureCompensationRatio(exposureCompensationRatio);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，显示视图代码可以去掉
         */
        mGlSurfaceView.setVisibility(View.VISIBLE);
        mRecorder.startPreview();
        Field aliyunCameraField = null;
        try {
            aliyunCameraField = mRecorder.getClass().getDeclaredField("aliyunCamera");
            aliyunCameraField.setAccessible(true);
            Object aliyunCamera = aliyunCameraField.get(mRecorder);
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
                    Log.e("TAG", "camera is not null");

                    final Field currentParamsField;
                    try {
                        currentParamsField = cameraProxy.getClass().getDeclaredField("currentParams");
                        currentParamsField.setAccessible(true);
                        final Camera.Parameters currentParams = (Camera.Parameters) currentParamsField.get(cameraProxy);
                        currentParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//设置focusMode
                        camera[0].setParameters(currentParams);
                        Log.e("TAG", "DONE: ");
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
        if (orientationDetector != null && orientationDetector.canDetectOrientation()) {
            orientationDetector.enable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.stopPreview();
        if (isRecording) {
            mRecorder.cancelRecording();
            isRecording = false;
        }
        /**
         * 部分android4.4机型会出现跳转Activity gl为空的问题，如果不需要适配，隐藏视图代码可以去掉
         */
        mGlSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orientationDetector != null) {
            orientationDetector.disable();
        }
    }

    private void getData() {
        mResolutionMode = RESOLUTION_540P;
//        minDuration = 2000;
//        maxDuration = 30000;
        videoQuality = VideoQuality.EPD;
    }

    public int getVirtualBarHeigh() {
        int vh = 0;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
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
        switch (mRatioMode) {
            case RATIO_MODE_1_1:
                height = width;
                break;
            case RATIO_MODE_3_4:
                height = width * 4 / 3;
                break;
            case RATIO_MODE_9_16:
                height = width * 16 / 9;
                break;
        }
        resolution[0] = width;
        resolution[1] = height;
        return resolution;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecorder.destroy();
        if (orientationDetector != null) {
            orientationDetector.setOrientationChangedListener(null);
        }
        AliyunRecorderCreator.destroyRecorderInstance();
        mMediaScanner.disconnect();
    }

    @Override
    public void onClick(View v) {
        if (v == mSwitchLightBtn) {
            if (mFlashType == FlashType.OFF) {
                mFlashType = FlashType.AUTO;
            } else if (mFlashType == FlashType.AUTO) {
                mFlashType = FlashType.ON;
            } else if (mFlashType == FlashType.ON) {
                mFlashType = FlashType.OFF;
            }
            switch (mFlashType) {
                case AUTO:
                    v.setSelected(false);
                    v.setActivated(true);
                    break;
                case ON:
                    v.setSelected(true);
                    v.setActivated(false);
                    break;
                case OFF:
                    v.setSelected(true);
                    v.setActivated(true);
                    break;
            }
            mRecorder.setLight(mFlashType);
        } else if (v == mBackBtn) {
            onBackPressed();
        }
//        else if (v == mCompleteBtn) {
////            mRecorder.finishRecording();
////            mClipManager.deleteAllPart();
//            if (mClipManager.getDuration() >= mClipManager.getMinDuration()) {
//                toEditor();
//            }
//        }
//        else if (v == mDeleteBtn) {
//            if (!isSelected) {
//                mRecordTimelineView.selectLast();
//                mDeleteBtn.setActivated(true);
//                isSelected = true;
//            } else {
//                mRecordTimelineView.deleteLast();
//                mDeleteBtn.setActivated(false);
//                mClipManager.deletePart();
//                isSelected = false;
//                if (mClipManager.getDuration() == 0) {
//                    mIconDefault.setVisibility(View.VISIBLE);
//                    mCompleteBtn.setVisibility(View.GONE);
//                    mDeleteBtn.setVisibility(View.GONE);
//                }
//            }
//        }
//        else if (v == mIconDefault) {
//            try {
//                Intent intent = new Intent("com.duanqu.qupai.action.import");
//                startActivity(intent);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(this, "当前不包含导入模块", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private int getPictureRotation() {
        int orientation = orientationDetector.getOrientation();
        int rotation = 90;
        if ((orientation >= 45) && (orientation < 135)) {
            rotation = 180;
        }
        if ((orientation >= 135) && (orientation < 225)) {
            rotation = 270;
        }
        if ((orientation >= 225) && (orientation < 315)) {
            rotation = 0;
        }
        if (mCameraType == CameraType.FRONT) {
            if (rotation != 0) {
                rotation = 360 - rotation;
            }
        }
        return rotation;
    }

    private void toEditor() {
        Uri projectUri = mRecorder.finishRecordingForEdit();
        AliyunIClipManager mClipManager = mRecorder.getClipManager();
        List<String> tempFileList = mClipManager.getVideoPathList();
//        Intent intent = new Intent("com.duanqu.qupai.action.editor");
//        int[] resolutions = getResolution();
//        mVideoParam.setScaleMode(ScaleMode.LB);
//        mVideoParam.setOutputWidth(resolutions[0]);
//        mVideoParam.setOutputHeight(resolutions[1]);
//        intent.putExtra("video_param", mVideoParam);
//        intent.putExtra("project_json_path", projectUri.getPath());
//        intent.putExtra("temp_file_list", tempFileList.toArray());
//        startActivity(intent);
        Intent intent = new Intent();
        intent.putExtra("videos", (Serializable) tempFileList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void stopRecording() {
        mRecorder.stopRecording();
        handleRecordStop();
    }

    private void startRecording() {
        String videoPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + System.currentTimeMillis() + ".mp4";
        mRecorder.setOutputPath(videoPath);
        int tempRotation = getPictureRotation();
        if (tempRotation == 90 || tempRotation == 270) {
            recordRotation = (getPictureRotation() + 90) % 360;
            if (mCameraType == CameraType.BACK) {
                recordRotation += 180;
                recordRotation %= 360;
            }
        } else if (tempRotation == 0 || tempRotation == 180) {
            recordRotation = (getPictureRotation() + 270) % 360;
        }
        handleRecordStart();
        mRecorder.setRotation(recordRotation);
        isRecordError = false;
        mRecorder.startRecording();
        if (mFlashType == FlashType.ON && mCameraType == CameraType.BACK) {
            mRecorder.setLight(FlashType.TORCH);
        }
    }

    private boolean checkIfStartRecording() {
        if (mRecordBtn.isActivated()) {
            return false;
        }
        if (CommonUtil.SDFreeSize() < 50 * 1000 * 1000) {
            Toast.makeText(this, "剩余磁盘空间不足", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        if (v == mRecordBtn) {
//            if (isOpenFailed) {
//                Toast.makeText(this, "请开启摄像头权限", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                downTime = System.currentTimeMillis();
//                if (!isRecording) {
//                    if (!checkIfStartRecording()) {
//                        return false;
//                    }
//                    mRecordBtn.setPressed(true);
//                    startRecording();
//                    mRecordBtn.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mRecordBtn.isPressed()) {
//                                mRecordBtn.setSelected(true);
//                                mRecordBtn.setHovered(false);
//                            }
//                        }
//                    }, 200);
//                    isRecording = true;
//                } else {
//                    stopRecording();
//                    isRecording = false;
//                }
//            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                long timeOffset = System.currentTimeMillis() - downTime;
//                mRecordBtn.setPressed(false);
//                if (timeOffset > 1000) {
//                    if (isRecording) {
//                        stopRecording();
//                        isRecording = false;
//                    }
//                } else {
//                    if (!isRecordError) {
//                        mRecordBtn.setSelected(false);
//                        mRecordBtn.setHovered(true);
//                    } else {
//                        isRecording = false;
//                    }
//                }
//            }
//        } else
        if (v == mGlSurfaceView) {
            if (event.getPointerCount() >= 2) {
                scaleGestureDetector.onTouchEvent(event);
            } else if (event.getPointerCount() == 1) {
                gestureDetector.onTouchEvent(event);
            }


        }
        return true;
    }

    private void handleRecordStart() {
        isSelected = false;
        mRecordBtn.setActivated(true);
//        mIconDefault.setVisibility(View.GONE);
//        mCompleteBtn.setVisibility(View.VISIBLE);
//        mDeleteBtn.setVisibility(View.VISIBLE);
        mSwitchLightBtn.setEnabled(false);
//        mCompleteBtn.setEnabled(false);
//        mDeleteBtn.setEnabled(false);
//        mDeleteBtn.setActivated(false);
    }

    private void handleRecordStop() {
        if (mFlashType == FlashType.ON && mCameraType == CameraType.BACK) {
            mRecorder.setLight(FlashType.OFF);
        }
    }

    private void handleRecordCallback(final boolean validClip, final long clipDuration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mRecordBtn.setActivated(false);
//                mRecordBtn.setHovered(false);
//                mRecordBtn.setSelected(false);
                if (validClip) {
                    mRecordTimelineView.setDuration((int) clipDuration);
                    mRecordTimelineView.clipComplete();
//                    mPlaytBtn.setVisibility(View.VISIBLE);
//                    mPlaytBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                        }
//                    });
                    mRecordBtn.setImageResource(R.mipmap.use_record);
                    mRecordBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toEditor();
                        }
                    });
                } else {
                    mRecordTimelineView.setDuration(0);
                }
//                mRecordTimeTxt.setVisibility(View.GONE);
                mSwitchLightBtn.setEnabled(true);

//                mCompleteBtn.setEnabled(true);
//                mDeleteBtn.setEnabled(true);

            }
        });

    }

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
        mRecorder.setZoom(scaleFactor);
        return false;

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        lastScaleFactor = detector.getScaleFactor();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mRecorder.setFocus(null);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceX) > 20) {
            return false;
        }
        exposureCompensationRatio += (distanceY / mGlSurfaceView.getHeight());
        if (exposureCompensationRatio > 1) {
            exposureCompensationRatio = 1;
        }
        if (exposureCompensationRatio < 0) {
            exposureCompensationRatio = 0;
        }
        mRecorder.setExposureCompensationRatio(exposureCompensationRatio);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (resCopy.getVisibility() == View.VISIBLE) {
//            return true;
//        }
        if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) {
            return true;
        }
        if (mRecordBtn.isActivated()) {
            return true;
        }
        if (velocityX > MAX_SWITCH_VELOCITY) {
            filterIndex++;
            if (filterIndex >= eff_dirs.length) {
                filterIndex = 0;
            }
        } else if (velocityX < -MAX_SWITCH_VELOCITY) {
            filterIndex--;
            if (filterIndex < 0) {
                filterIndex = eff_dirs.length - 1;
            }
        } else {
            return true;
        }
//        if(!new File(eff_dirs[filterIndex]).exists()){
//            return false;
//        }
        EffectFilter effectFilter = new EffectFilter(eff_dirs[filterIndex]);
        mRecorder.applyFilter(effectFilter);
        showFilter(effectFilter.getName());
        return false;
    }

    private void showFilter(String name) {
        if (name == null || name.isEmpty()) {
            name = "原片";
        }
        filterTxt.animate().cancel();
        filterTxt.setText(name);
        filterTxt.setVisibility(View.VISIBLE);
        filterTxt.setAlpha(FADE_IN_START_ALPHA);
        txtFadeIn();
    }

    private void txtFadeIn() {
        filterTxt.animate().alpha(1).setDuration(FILTER_ANIMATION_DURATION / 2).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        txtFadeOut();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void txtFadeOut() {
        filterTxt.animate().alpha(0).setDuration(FILTER_ANIMATION_DURATION / 2).start();
        filterTxt.animate().setListener(null);
    }
}
