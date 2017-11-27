package com.yusion.shanghai.yusion4s.ui.yusion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.yusion.shanghai.yusion4s.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion4s.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion4s.glide.StatusImageRel;
import com.yusion.shanghai.yusion4s.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion4s.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion4s.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion4s.settings.Constants;
import com.yusion.shanghai.yusion4s.ui.CommitActivity;
import com.yusion.shanghai.yusion4s.ui.upload.PreviewActivity;
import com.yusion.shanghai.yusion4s.utils.DensityUtil;
import com.yusion.shanghai.yusion4s.utils.GlideUtil;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;
import com.yusion.shanghai.yusion4s.utils.OcrUtil;
import com.yusion.shanghai.yusion4s.utils.OssUtil;
import com.yusion.shanghai.yusion4s.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion4s.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DocumentActivity extends BaseActivity {
    public Button ancher;
    public ImageView choose_icon;
    public Button delete_image_btn;
    private String mType;//id_card_front  id_card_back  driving_lic  auth_credit
    private String mRole;//lender lender_sp guarantor guarantor_sp
    private String mImgObjectKey = "";
    private OcrResp.ShowapiResBodyBean mOcrResp;
    private String imgUrl = "";
    public TitleBar titleBar;
    public Intent mGetIntent;
    private boolean isEditing = false;
    private TextView mEditTv;
    private StatusImageRel statusImageRel;
    private boolean hasChoose;
    private boolean needUploadFidToServer;
    private TextView errorTv;
    private LinearLayout errorLin;

    //label页进入
    private String clt_id;
    private UploadImgItemBean uploadImgItemBean;
    private String imgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        mGetIntent = getIntent();
        mType = mGetIntent.getStringExtra("type");
        mRole = mGetIntent.getStringExtra("role");
        clt_id = mGetIntent.getStringExtra("clt_id");
        mImgObjectKey = mGetIntent.getStringExtra("objectKey");

        needUploadFidToServer = mGetIntent.getBooleanExtra("needUploadFidToServer", true);
        initView();
        initData();
    }

    private void initData() {
        if (needUploadFidToServer) {
            ListImgsReq req = new ListImgsReq();
            req.clt_id = clt_id;
            req.label = mType;
            UploadApi.listImgs(this, req, resp -> {
                if (resp != null) {
                    if (resp.has_err) {
                        errorLin.setVisibility(View.VISIBLE);
                        errorTv.setText("您提交的资料有误：" + resp.error);
                    } else {
                        errorLin.setVisibility(View.GONE);
                    }
                    if (resp.list.size() > 0) {
                        this.uploadImgItemBean = resp.list.get(0);
                        imgUrl = this.uploadImgItemBean.s_url;
                        imgId = this.uploadImgItemBean.id;
                        GlideUtil.loadImg(this, statusImageRel, imgUrl);
                    }
                }
                onImageCountChange();
            });
        } else {
            if (!TextUtils.isEmpty(mGetIntent.getStringExtra("imgUrl"))) {
                imgUrl = mGetIntent.getStringExtra("imgUrl");
                GlideUtil.loadLocalImg(this, statusImageRel, new File(imgUrl));
            }
            onImageCountChange();
        }
    }

    private void initView() {
        LinearLayout template = (LinearLayout) findViewById(R.id.document_template_lin);
        TextView textView = (TextView) findViewById(R.id.smalltitle);
        String title = "";
        switch (mType) {
            case "auth_credit":
                title = "征信授权书";
                break;
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
                title = mType;
        }
        textView.setText(title);
        LayoutInflater inflater = LayoutInflater.from(this);

        switch (mType) {
            case "id_card_front": //身份证国徽面
                template.addView(inflater.inflate(R.layout.template_id_front, template, false));
                break;
            case "id_card_back": //身份证人像面
                template.addView(inflater.inflate(R.layout.template_id_back, template, false));
                mOcrResp = ((OcrResp.ShowapiResBodyBean) mGetIntent.getSerializableExtra("ocrResp"));
                break;
            case "driving_lic": //驾驶证
                template.addView(inflater.inflate(R.layout.template_driving_lic, template, false));
                break;
        }

        createBottomDialog();

        ancher = (Button) findViewById(R.id.ancher);
        statusImageRel = (StatusImageRel) findViewById(R.id.statusImageRel);
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
                if (hasImg()) {
                    if (!mBottomDialog.isShowing()) {
                        mBottomDialog.show();
                    }
                } else {
                    takePhoto();
                }
            }
        });
        delete_image_btn = (Button) findViewById(R.id.image_del_btn);
        delete_image_btn.setOnClickListener(v -> {
            if (!hasChoose) {
                return;
            }
            if (needUploadFidToServer) {
                List<String> relDelImgIdList = new ArrayList<>();
                relDelImgIdList.add(imgId);
                DelImgsReq req = new DelImgsReq();
                req.id = relDelImgIdList;
                req.clt_id = clt_id;
                UploadApi.delImgs(DocumentActivity.this, req, new OnCodeAndMsgCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        if (code == 0) {
                            onImgDel();
                        }
                    }
                });

            } else {
                onImgDel();
            }
        });

        titleBar = initTitleInfo();
        mEditTv = titleBar.getRightTextTv();
        titleBar.setRightClickListener(v -> {
            isEditing = !isEditing;
            if (isEditing) {
                //显示icon布局
                statusImageRel.cbImg.setVisibility(View.VISIBLE);
                hasChoose = false;
                mEditTv.setText("取消");
                delete_image_btn.setVisibility(View.VISIBLE);
                delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));
            } else {
                mEditTv.setText("编辑");
                delete_image_btn.setVisibility(View.GONE);
                statusImageRel.cbImg.setVisibility(View.GONE);
                statusImageRel.cbImg.setImageResource(R.mipmap.choose_icon);
            }
        });

        if (needUploadFidToServer) {
            errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
            errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        }
    }

    private void onImgDel() {
        Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
        imgUrl = "";
        mImgObjectKey = "";
        onImageCountChange();
        isEditing = false;
        statusImageRel.sourceImg.setImageResource(R.mipmap.camera_document);
        delete_image_btn.setVisibility(View.GONE);
        statusImageRel.cbImg.setVisibility(View.GONE);
    }

    private Dialog mBottomDialog;

    private void createBottomDialog() {
        View bottomLayout = LayoutInflater.from(this).inflate(R.layout.document_bottom_dialog, null);
        TextView tv1 = ((TextView) bottomLayout.findViewById(R.id.tv1));
        TextView tv2 = ((TextView) bottomLayout.findViewById(R.id.tv2));
        TextView tv3 = ((TextView) bottomLayout.findViewById(R.id.tv3));
        tv1.setOnClickListener(v -> {
            Intent intent = new Intent(DocumentActivity.this, PreviewActivity.class);
            if (uploadImgItemBean != null && !TextUtils.isEmpty(uploadImgItemBean.raw_url)) {
                intent.putExtra("PreviewImg", uploadImgItemBean.raw_url);
            } else {
                intent.putExtra("PreviewImg", imgUrl);
            }

            ActivityOptionsCompat compat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(DocumentActivity.this, ancher, "shareNames");
            ActivityCompat.startActivity(DocumentActivity.this, intent, compat.toBundle());
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
        if (mBottomDialog == null) {
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
    }

    private void takePhoto() {
        Intent i = new Intent(DocumentActivity.this, PhotoMediaActivity.class);
        i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());//加载类型
        i.putExtra("maxCount", 1);//加载类型
        switch (mType) {
            case "id_card_front": {
                startActivityForResult(i, 3000);
                break;
            }
            case "id_card_back": {
                //人像面
                startActivityForResult(i, 3001);
                break;
            }
            case "auth_credit": {
                startActivityForResult(i, 3002);
                break;
            }
            case "driving_lic": {
                startActivityForResult(i, 100);
                break;
            }
        }
    }

    private boolean hasImg() {
        return !TextUtils.isEmpty(imgUrl);
    }

    private void onImageCountChange() {
        if (hasImg()) {
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
            if (needUploadFidToServer) {
                upLoadImg(dialog, localPath);
            } else if (mType.equals(Constants.FileLabelType.ID_BACK)) {
                OcrUtil.requestOcr(this, localPath, new OSSObjectKeyBean(mRole, mType, ".png"), "id_card", (ocrResp, objectKey) -> {
                            mOcrResp = new OcrResp.ShowapiResBodyBean();
                            if (ocrResp == null) {
                                Toast.makeText(this, "识别失败", Toast.LENGTH_LONG).show();
                            } else if (ocrResp.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp.showapi_res_body.name)) {
                                Toast.makeText(this, "识别失败", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "识别成功", Toast.LENGTH_LONG).show();
                                mOcrResp = ocrResp.showapi_res_body;
                                // 搜索是否存在该用户
                                search(mOcrResp.idNo);
                            }
                            onUploadOssSuccess(localPath, dialog, objectKey);
                        }, (throwable, s) -> {
                            Toast.makeText(myApp, "ocr识别失败", Toast.LENGTH_SHORT).show();
                            onUploadOssFailure(dialog);
                        }
                );
            } else {
                OssUtil.uploadOss(this, false, localPath, new OSSObjectKeyBean(mRole, mType, ".png"), objectKey -> {
                    onUploadOssSuccess(localPath, dialog, objectKey);
                }, data1 -> onUploadOssFailure(dialog));
            }
        }

    }

    private void search(String idNo) {
        OrderApi.searchClientExist(DocumentActivity.this, idNo, new OnItemDataCallBack<List<SearchClientResp>>() {
            @Override
            public void onItemDataCallBack(List<SearchClientResp> data) {
                if (data != null && data.size() == 1) {
                    PopupDialogUtil.relevanceInfoDialog(
                            DocumentActivity.this, "系统检测到当前客户为已注册用户，可直接关联。", data.get(0).clt_nm, data.get(0).mobile, data.get(0).id_no, dialog -> {
                                Intent intent = new Intent(DocumentActivity.this, CommitActivity.class);
                                intent.putExtra("why_commit", "create_user");
                                intent.putExtra("clt_nm",data.get(0).clt_nm);
                                intent.putExtra("mobile",data.get(0).mobile);
                                intent.putExtra("id_no",data.get(0).id_no);
                                startActivity(intent);
                            });
                }
            }
        });
    }

    private void upLoadImg(final Dialog dialog, String imagePath) {
        OssUtil.uploadOss(this, false, imagePath, new OSSObjectKeyBean(mRole, mType, ".png"), objectKey -> {
            List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
            fileUrlBean.clt_id = clt_id;
            fileUrlBean.label = mType;
            fileUrlBean.file_id = objectKey;
            uploadFileUrlBeanList.add(fileUrlBean);
            UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
            uploadFilesUrlReq.files = uploadFileUrlBeanList;
            uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "");
            uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "");
            UploadApi.uploadFileUrl(this, uploadFilesUrlReq, data -> {
                imgId = data.get(0);
                onUploadOssSuccess(imagePath, dialog, objectKey);
            });
        }, data -> {
            Toast.makeText(DocumentActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();
            onUploadOssFailure(dialog);
        });
    }

    void onUploadOssFailure(Dialog dialog) {
        if (isFinishing()) {
            return;
        }
        Toast.makeText(DocumentActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void onUploadOssSuccess(String localPath, Dialog dialog, String objectKey) {
        if (isFinishing()) {
            return;
        }
        imgUrl = localPath;
        mImgObjectKey = objectKey;
        runOnUiThread(() -> {
            onImageCountChange();
            GlideUtil.loadLocalImg(DocumentActivity.this, statusImageRel, new File(imgUrl));
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
        mGetIntent.putExtra("objectKey", mImgObjectKey);
        mGetIntent.putExtra("type", mType);
        mGetIntent.putExtra("ocrResp", mOcrResp);
        mGetIntent.putExtra("imgUrl", imgUrl);
        setResult(RESULT_OK, mGetIntent);
        finish();
    }

    private TitleBar initTitleInfo() {
        String title = "";
        switch (mType) {
            case "auth_credit":
                title = "征信授权书";
                break;
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
                title = mType;
        }
        return initTitleBar(this, title).setLeftClickListener(v -> onBack()).setRightText("编辑").setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);
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
}

