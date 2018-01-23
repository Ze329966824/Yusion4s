package com.yusion.shanghai.yusion4s.base;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.event.MainActivityEvent;
import com.yusion.shanghai.yusion4s.settings.Settings;
import com.yusion.shanghai.yusion4s.ubt.UBT;
import com.yusion.shanghai.yusion4s.ui.MainActivity;
import com.yusion.shanghai.yusion4s.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion4s.ui.main.SettingsActivity;
import com.yusion.shanghai.yusion4s.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion4s.utils.AppUtils;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String WX_APP_ID = "wxd581c152982fefe4";
    public IWXAPI api;
    protected Yusion4sApp myApp;
    public int WIDTH;
    public int HEIGHT;
    public String pushStr;


    public String title;
    public String content;
    public String app_id;
    public String vehicle_cond;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager.addActivity(this);
        WIDTH = this.getWindowManager().getDefaultDisplay().getWidth();
        HEIGHT = this.getWindowManager().getDefaultDisplay().getHeight();
        myApp = ((Yusion4sApp) getApplication());
        if (!Settings.isOnline) {
            //Toast.makeText(this, getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }

        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        api.registerApp(WX_APP_ID);
//        PgyCrashManager.register(this);
        //UBT.bind(BaseActivity.this);
//        UBT.bind(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        } else {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);
    }

    public TitleBar initTitleBar(final Activity activity, String title) {
        TitleBar titleBar = (TitleBar) activity.findViewById(R.id.title_bar);
        titleBar.setLeftClickListener(view -> onBackPressed());
        titleBar.setImmersive(false);
        titleBar.setTitle(title);
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setRightTextColor(Color.BLACK);
        titleBar.setLeftImageResource(R.mipmap.title_back_arrow);
        titleBar.setBackgroundResource(R.color.white);
        titleBar.setTitleColor(activity.getResources().getColor(R.color.black));
        titleBar.setDividerColor(activity.getResources().getColor(R.color.separate_line_color));
        return titleBar;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
//        PgyFeedbackShakeManager.register(this);
        if (getClass().getSimpleName().equals(LaunchActivity.class.getSimpleName())) {
            UBT.addAppEvent(this, "app_start");
        }
        if (!Yusion4sApp.isForeground) {
            Yusion4sApp.isForeground = true;
            UBT.addAppEvent(this, "app_awake");
        }
        UBT.addPageEvent(this, "page_show", "activity", getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();

//        PgyFeedbackShakeManager.unregister();
        MobclickAgent.onPause(this);
        MobclickAgent.onPause(this);
        if (getClass().getSimpleName().equals(SettingsActivity.class.getSimpleName())) {
            SettingsActivity settingsActivity = (SettingsActivity) this;
            if (settingsActivity.finishByLoginOut) {
                return;
            }
        }

        UBT.addPageEvent(this, "page_hidden", "activity", getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
        //destroy后显示最新列表
//        for (AppCompatActivity activity : ActivityManager.list) {
//            Log.e("TAG2222", "onDestroy: " + activity.getClass().getSimpleName());
//        }
//        Log.e("TAG2222", "onDestroy: ---------");
//        adb shell dumpsys activity | grep com.yusion.shanghai.yusion4s
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtils.isAppOnForeground()) {
            Yusion4sApp.isForeground = false;
            UBT.addAppEvent(this, "app_pause");
        }
    }

    /**
     * 并没有杀死进程
     */
    public void reOpenApp() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        UBT.addAppEvent(this, "app_pause");
    }


    private View contentView;
    private PopupWindow mPopWindow;
    private SoundPool soundPool;


    public void initPopupWindow() {
        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();

        lp.gravity = Gravity.TOP;//设置对话框置顶显示


        win.setAttributes(lp);


        contentView = LayoutInflater.from(this).inflate(R.layout.layout_msg_push, null);
        TextView titleTv = contentView.findViewById(R.id.msg_push_title);
        TextView typeTv = contentView.findViewById(R.id.msg_push_car_type);
        TextView msgTv = contentView.findViewById(R.id.msg_push_order_message);

        titleTv.setText(title);
        typeTv.setText(vehicle_cond);
        msgTv.setText(content);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        //加载deep 音频文件
        soundPool.load(this, R.raw.push_sound, 1);
        Log.e("TAG", "initPopupWindow: " + app_id);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            int lastX = 0;
            int lastY = 0;
            double x = 0;
            double y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                DisplayMetrics dm = getResources().getDisplayMetrics();
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        lastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
                        lastY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        //event.getRawX();获得移动的位置
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        float scaleY = v.getScaleY();
                        int height = v.getHeight();
                        int width = v.getWidth();

                        if (Math.abs(dy) > (height / 2)) {

                            mPopWindow.dismiss();
                            timer.onFinish();
                            v.scrollTo(0, 0);
                            v.setAlpha(1);

                        } else {

                            v.scrollBy(-dx, 0);

                            float abs_x = Math.abs(v.getScrollX());
//                                Log.e("TAG", "onTouch: " + abs_x); //0 -> 600+
                            v.setAlpha(1.0f - 1.0f / width * abs_x);

                        }


                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        double sqrt = Math.sqrt(Math.abs((x - event.getX()) * (x - event.getX()) + (y - event.getY()) * (y - event.getY())));
                        if ( sqrt < 10){
                            Intent i = new Intent(BaseActivity.this, OrderDetailActivity.class);
                            i.putExtra("app_id", app_id);
                            startActivity(i);
                            mPopWindow.dismiss();
                            timer.onFinish();
                        }



                        int i = screenWidth / 3;
                        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", v.getAlpha(), 1.0f);
                        final int scrollX = v.getScrollX();
                        animator.addUpdateListener(animation -> {
                            float fraction = animation.getAnimatedFraction();
                            v.setScrollX((int) (scrollX * (1.0f - fraction)));
                            Log.e("TAG", "onAnimationUpdate: " + fraction);
                        });
                        animator.start();

                        if (Math.abs(v.getScrollX()) > i) {
                            v.setAlpha(0);
                            v.postDelayed(() -> {
                                mPopWindow.dismiss();
                                timer.onFinish();
                                v.scrollTo(0, 0);
                                v.setAlpha(1);
                            }, 1);
                        } else {
                            v.scrollTo(0, 0);
                            v.setAlpha(1);
                        }
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

    }
    CountDownTimer timer = new CountDownTimer(5000, 10) {

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            mPopWindow.dismiss();

        }
    };
    public void showPopupWindow() {



        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            timer.onFinish();
        }
        contentView.setVisibility(View.VISIBLE);
        mPopWindow = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPopWindow.setFocusable(false);

        mPopWindow.setHeight(getResources().getDimensionPixelOffset(R.dimen.y400));
        mPopWindow.setOutsideTouchable(false);


        contentView.postDelayed(() -> {
//            mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0, (int) getResources().getDimension(R.dimen.y50));
            mPopWindow.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0, 0);
            if (ActivityManager.getActivity() instanceof MainActivity){
                EventBus.getDefault().post(MainActivityEvent.showReadicon);
            }
            timer.start();
        }, 1000);


        //播放deep
        soundPool.play(1, 1, 1, 0, 0, 1);

    }


    private void initJpush() throws JSONException {

        JSONObject jo = new JSONObject(pushStr);
        title = jo.optString("title");
        content = jo.optString("content");
        app_id = jo.optString("app_id");


    }
}
