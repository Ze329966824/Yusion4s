package com.yusion.shanghai.yusion4s.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion4s.bean.order.SearchClientResp;
import com.yusion.shanghai.yusion4s.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ui.order.OrderCreateActivity;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OcrUtil;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.PreviewImgUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 1.只在 创建用户 功能使用
 * 2.目前照片是用户拍摄，不存在服务器拉取，但需要暴露接口
 * 3.拍摄完照片后不必上传服务器，需要向其他页面返回图片fid
 * 4.身份证正面+身份证反面+驾驶证三种图片
 * 5.通过{@link #imgUrl} 是否为空判断有没有图片
 */
public class SingleImgUploadForCreateUserActivity extends BaseActivity {
    private String imgUrl = "";
    public Button delete_image_btn;
    private OcrResp.ShowapiResBodyBean mOcrResp;
    public TitleBar titleBar;
    public Intent getIntent;
    private boolean isEditing = false;
    private TextView mEditTv;
    private StatusImageRel statusImageRel;
    private boolean hasChoose;

    //label页进入
    private SearchClientResp searchResp;

    private IntentData intentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        //getIntent.getBooleanExtra("needUploadImgFidToServer", true);
        //getIntent.getBooleanExtra("needLoadImgFromServer", false);
        //getIntent.getBooleanExtra("isCanEdit", true);
        getIntent = getIntent();
        intentData = new IntentData();
        intentData.needUploadImgFidToServer = getIntent.getBooleanExtra("needUploadImgFidToServer", true);
        intentData.needLoadImgFromServer = getIntent.getBooleanExtra("needLoadImgFromServer", false);
        intentData.type = getIntent.getStringExtra("type");
        intentData.role = getIntent.getStringExtra("role");
        intentData.clt_id = getIntent.getStringExtra("clt_id");
        intentData.imgObjectKey = getIntent.getStringExtra("objectKey");

//        intentData.role = "lender";
//        intentData.type = "id_card_back";

        initView();
        initData();
    }

    private void initData() {
        if (!TextUtils.isEmpty(getIntent.getStringExtra("imgUrl"))) {
            imgUrl = getIntent.getStringExtra("imgUrl");
            GlideUtil.loadLocalImg(this, statusImageRel, new File(imgUrl));
        }
        onImageCountChange();
    }

    private void initView() {
        LinearLayout template = findViewById(R.id.document_template_lin);
        TextView textView = findViewById(R.id.smalltitle);
        String title;
        switch (intentData.type) {
            case "id_card_front":
                title = "身份证国徽面";
                break;
            case "id_card_back":
                title = "身份证人像面";
                break;
            case "driving_lic":
                title = "驾驶证影像件";
                break;
            default:
                title = intentData.type;
        }
        textView.setText(title);

        LayoutInflater inflater = LayoutInflater.from(this);
        switch (intentData.type) {
            case "id_card_front": //身份证国徽面
                template.addView(inflater.inflate(R.layout.template_id_front, template, false));
                break;
            case "id_card_back": //身份证人像面
                template.addView(inflater.inflate(R.layout.template_id_back, template, false));
                mOcrResp = ((OcrResp.ShowapiResBodyBean) getIntent.getSerializableExtra("ocrResp"));
                break;
            case "driving_lic": //驾驶证
                template.addView(inflater.inflate(R.layout.template_driving_lic, template, false));
                break;
        }

        createBottomDialog();

        statusImageRel = findViewById(R.id.statusImageRel);
        statusImageRel.sourceImg.setImageResource(R.mipmap.camera_document);
        statusImageRel.sourceImg.setOnClickListener(v -> {
            if (isEditing) {
                if (hasChoose) {
                    hasChoose = false;
                    statusImageRel.cbImg.setImageResource(R.mipmap.choose_icon);
                } else {
                    hasChoose = true;
                    statusImageRel.cbImg.setImageResource(R.mipmap.surechoose_icon);
                }
                onChooseChange(hasChoose);
            } else {
                if (!TextUtils.isEmpty(imgUrl)) {
                    mBottomDialog.show();
                } else {
                    takePhoto();
                }
            }
        });

        delete_image_btn = findViewById(R.id.image_del_btn);
        delete_image_btn.setOnClickListener(v -> {
            if (hasChoose) {
                onImgDel();
            }
        });

        titleBar = initTitleInfo();
        mEditTv = titleBar.getRightTextTv();
        titleBar.setRightClickListener(v -> {
            isEditing = !isEditing;
            if (isEditing) {
                //由未编辑状态点击至编辑状态
                statusImageRel.cbImg.setVisibility(View.VISIBLE);
                mEditTv.setText("取消");
                delete_image_btn.setVisibility(View.VISIBLE);
                delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));
                hasChoose = false;
            } else {
                mEditTv.setText("编辑");
                delete_image_btn.setVisibility(View.GONE);
                statusImageRel.cbImg.setVisibility(View.GONE);
                statusImageRel.cbImg.setImageResource(R.mipmap.choose_icon);
            }
        });
    }

    private void onImgDel() {
        Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
        imgUrl = "";
        intentData.imgObjectKey = "";
        onImageCountChange();
        isEditing = false;
        statusImageRel.sourceImg.setImageResource(R.mipmap.camera_document);
        delete_image_btn.setVisibility(View.GONE);
        statusImageRel.cbImg.setVisibility(View.GONE);
    }

    private Dialog mBottomDialog;

    private void createBottomDialog() {
        View bottomLayout = LayoutInflater.from(this).inflate(R.layout.document_bottom_dialog, null);
        //预览
        TextView tv1 = bottomLayout.findViewById(R.id.tv1);
        //重新拍摄
        TextView tv2 = bottomLayout.findViewById(R.id.tv2);
        //取消
        TextView tv3 = bottomLayout.findViewById(R.id.tv3);
        tv1.setOnClickListener(v -> {
            ArrayList<String> showImgUrls = new ArrayList<>();
            showImgUrls.add(imgUrl);
            PreviewImgUtil.showImg(this, showImgUrls);
            overridePendingTransition(R.anim.center_zoom_in, R.anim.stay);

            if (mBottomDialog.isShowing()) {
                mBottomDialog.dismiss();
            }
        });
        tv2.setOnClickListener(v -> {
            takePhoto();
            if (mBottomDialog.isShowing()) {
                mBottomDialog.dismiss();
            }
        });
        tv3.setOnClickListener(v -> {
            if (mBottomDialog.isShowing()) {
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog = new Dialog(this, R.style.MyDialogStyle);
        mBottomDialog.setContentView(bottomLayout);
        mBottomDialog.setCanceledOnTouchOutside(false);
        mBottomDialog.getWindow().setWindowAnimations(R.style.dialogAnimationStyle);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Window dialogWindow = mBottomDialog.getWindow();
        dialogWindow.getDecorView().setBackgroundResource(android.R.color.transparent);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.BOTTOM);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = metrics.widthPixels - DensityUtil.dip2px(this, 15) * 2;
        dialogWindow.setAttributes(lp);
    }

    private void takePhoto() {
        Intent i = new Intent(SingleImgUploadForCreateUserActivity.this, PhotoMediaActivity.class);
        i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
        i.putExtra("maxCount", 1);
        switch (intentData.type) {
            case "id_card_front": {
                startActivityForResult(i, 3000);
                break;
            }
            case "id_card_back": {
                startActivityForResult(i, 3001);
                break;
            }
            case "driving_lic": {
                startActivityForResult(i, 100);
                break;
            }
        }
    }

    private void onImageCountChange() {
        if (!TextUtils.isEmpty(imgUrl)) {
            mEditTv.setEnabled(true);
            mEditTv.setTextColor(Color.parseColor("#ffffff"));
        } else {
            mEditTv.setEnabled(false);
            mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
        }
        titleBar.setRightText("编辑");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            List<String> files = data.getStringArrayListExtra("files");
            String localPath = files.get(0);

            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            if (intentData.type.equals(Constants.FileLabelType.ID_BACK)) {
                OcrUtil.requestOcr(this, localPath, new OSSObjectKeyBean(intentData.role, intentData.type, ".png"), "id_card", (ocrResp, objectKey) -> {

                            mOcrResp = new OcrResp.ShowapiResBodyBean();

                            if (ocrResp == null) {
                                Toast.makeText(this, "识别失败", Toast.LENGTH_LONG).show();
                            } else if (ocrResp.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp.showapi_res_body.name)) {
                                Toast.makeText(this, "某些信息未识别成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "识别成功", Toast.LENGTH_LONG).show();
                                mOcrResp = ocrResp.showapi_res_body;

                                search(mOcrResp.idNo);

                            }

                            onUploadOssSuccess(localPath, dialog, objectKey);

                        }, (throwable, s) -> {
                            Toast.makeText(myApp, "ocr识别失败", Toast.LENGTH_SHORT).show();

                            onUploadOssFailure(dialog);
                        }
                );
            } else {
                OssUtil.uploadOss(this, false, localPath, new OSSObjectKeyBean(intentData.role, intentData.type, ".png"), objectKey -> {
                    onUploadOssSuccess(localPath, dialog, objectKey);
                }, data1 -> onUploadOssFailure(dialog));
            }

        }

    }

    private void search(String idNo) {
        OrderApi.searchClientExist(SingleImgUploadForCreateUserActivity.this, idNo, data -> {
            if (data != null && data.size() > 0) {
                searchResp = data.get(0);
                if (searchResp.auth_credit.lender.commited.equals("1")) {
                    PopupDialogUtil.relevanceInfoDialog(
                            SingleImgUploadForCreateUserActivity.this, "系统检测到当前客户为已注册用户，可直接关联。", searchResp.clt_nm, searchResp.mobile, idNo, dialog -> {
                                dialog.dismiss();
                                Intent intent = getIntent();
                                intent.setClass(SingleImgUploadForCreateUserActivity.this, OrderCreateActivity.class);
                                checkAuthCreditExist(intent, searchResp);
                                intent.putExtra("enable", true);
                                intent.putExtra("name", searchResp.clt_nm);
                                intent.putExtra("mobile", searchResp.mobile);
                                intent.putExtra("sfz", searchResp.id_no);
                                intent.putExtra("why_come", "create_user");
                                startActivity(intent);
                                finish();

                            });
                }
            }
        });
    }

    void onUploadOssFailure(Dialog dialog) {
        if (isFinishing()) {
            return;
        }

        Toast.makeText(SingleImgUploadForCreateUserActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void onUploadOssSuccess(String localPath, Dialog dialog, String objectKey) {
        if (isFinishing()) {
            return;
        }

        imgUrl = localPath;
        intentData.imgObjectKey = objectKey;
        runOnUiThread(() -> {
            onImageCountChange();
            //加载图片
            GlideUtil.loadLocalImg(SingleImgUploadForCreateUserActivity.this, statusImageRel, new File(imgUrl));
        });

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        getIntent.putExtra("objectKey", intentData.imgObjectKey);
        getIntent.putExtra("type", intentData.type);
        getIntent.putExtra("ocrResp", mOcrResp);
        getIntent.putExtra("imgUrl", imgUrl);
        setResult(RESULT_OK, getIntent);
        finish();
    }

    private TitleBar initTitleInfo() {
        String title;
        switch (intentData.type) {
            case "id_card_front":
                title = "身份证国徽面";
                break;
            case "id_card_back":
                title = "身份证人像面";
                break;
            case "driving_lic":
                title = "驾驶证影像件";
                break;
            default:
                title = intentData.type;
        }
        return initTitleBar(this, title).setLeftClickListener(v -> onBack()).setRightText("编辑").setRightTextColor(R.color.black).setRightTextSize(16);
    }

    public void onChooseChange(boolean hasChoose) {
        if (!hasChoose) {
            statusImageRel.cbImg.setImageResource(R.mipmap.choose_icon);
            delete_image_btn.setEnabled(false);
            delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));
        } else {
            statusImageRel.cbImg.setImageResource(R.mipmap.surechoose_icon);
            delete_image_btn.setEnabled(true);
            delete_image_btn.setTextColor(Color.parseColor("#ff3f00"));
        }
    }

    private void checkAuthCreditExist(Intent intent, SearchClientResp item) {
        if (item.auth_credit.lender != null) {//如果不等于空
            intent.putExtra("isHasLender", "1");
            intent.putExtra("lender_clt_id", item.auth_credit.lender.clt_id);
            intent.putExtra("lender", item.auth_credit.lender.auth_credit_img_count);
        } else {
            intent.putExtra("isHasLender", "2");
        }
        if (item.auth_credit.lender_sp != null) {//如果不等于空
            intent.putExtra("isHasLender_sp", "1");
            intent.putExtra("lender_sp_clt_id", item.auth_credit.lender_sp.clt_id);
            intent.putExtra("lender_sp", item.auth_credit.lender_sp.auth_credit_img_count);
        } else {
            intent.putExtra("isHasLender_sp", "2");
        }

        if (item.auth_credit.guarantor != null) {//如果不等于空
            intent.putExtra("isGuarantor", "1");
            intent.putExtra("guarantor_clt_id", item.auth_credit.guarantor.clt_id);
            intent.putExtra("guarantor", item.auth_credit.guarantor.auth_credit_img_count);
        } else {
            intent.putExtra("isGuarantor", "2");
        }

        if (item.auth_credit.guarantor_sp != null) {//如果不等于空
            intent.putExtra("isGuarantor_sp", "1");
            intent.putExtra("guarantor_sp_clt_id", item.auth_credit.guarantor_sp.clt_id);
            intent.putExtra("guarantor_sp", item.auth_credit.guarantor_sp.auth_credit_img_count);

        } else {
            intent.putExtra("isGuarantor_sp", "2");
        }

    }

    class IntentData {
        public boolean needUploadImgFidToServer;
        public boolean needLoadImgFromServer;
        public String type;
        public String role;
        public String clt_id;
        public String imgObjectKey;
    }
}

