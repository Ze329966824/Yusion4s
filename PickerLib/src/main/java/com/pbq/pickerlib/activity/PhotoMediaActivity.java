package com.pbq.pickerlib.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.pbq.pickerlib.R;
import com.pbq.pickerlib.adapter.PhotoMediaAdapter;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.pbq.pickerlib.luban.Luban;
import com.pbq.pickerlib.luban.OnCompressListener;
import com.pbq.pickerlib.util.PhoneInfoUtil;
import com.pbq.pickerlib.video.CustomCameraActivity;
import com.pbq.pickerlib.view.ImageFolderPopWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by pengbangqin on 16-08-21.
 * 图片选择和视频选择
 */
public class PhotoMediaActivity extends AppCompatActivity {
    //拍摄视频上传
    public static final int REQUEST_TAKE_VIDEO = 0;
    /**
     * 在视频选择中录像的请求码
     */
    public static final int REQUEST_CODE_CAMERA = 1;
    /**
     * 在图片选择中拍照的请求码
     */
    public static final int REQUEST_CODE_VEDIO = 2;
    /**
     * 在图片选择点击后请求码
     */
    public static final int REQUEST_CODE_IMAGE_SWITCHER = 3;
    /**
     * 显示系统图片的视图
     */
    GridView gvPhotos;
    /**
     * 定义图片和视频路径的实体
     */
    PhotoVideoDir currentDir;
    /**
     * 定义一个弹出popupwindow变量名
     */
    ImageFolderPopWindow popDir;
    /**
     * 最外层布局的变量
     */
    View lyTopBar;
    /**
     * 定义一个TextView的成员变量
     */
    TextView tvTitle;
    /**
     * 定义一个Button的成员变量
     */
    Button btnNext;
    int maxPicSize;
    /**
     * 通过键值对存储图片或视频的路径
     * String 为包含所有的文件名称
     */
    private HashMap<String, PhotoVideoDir> dirMap = new HashMap<String, PhotoVideoDir>();
    /**
     * 记录图片的最大选择
     */
    private int maxCount = 9;
    private File cameraFile;
    private ArrayList<String> selectedFath = new ArrayList<>();
    /**
     * 上传类型
     */
    private PhotoVideoDir.Type loadType = PhotoVideoDir.Type.IMAGE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo_media_activity);
        initData();
        init();
        maxCount = getIntent().getIntExtra("maxCount", 9);
        QupaiHttpFinal.getInstance().initOkHttpFinal();
    }

    /**
     * 初始化控件
     */
    private void init() {
        gvPhotos = (GridView) findViewById(R.id.gv_photos);
        tvTitle = (TextView) findViewById(R.id.tv_top_bar_title);
        btnNext = (Button) findViewById(R.id.btn_next);
        lyTopBar = findViewById(R.id.ly_top_bar);
        //如果是图片类型，显示图片列表，否则显示视频列表
        if (isImageType()) {
            loadImagesList();
        }
        if (isVideoType()) {
            loadVideosList();
        }
        //传入到ImageFolderPopWindow构造方法中 loadType 加载的类型（图片或视频）
        popDir = new ImageFolderPopWindow(this, PhoneInfoUtil.getScreenWidth(this), PhoneInfoUtil.getScreenHeight(this) * 3 / 5);
        //设置外部可触摸
        popDir.setOutsideTouchable(true);
        popDir.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        popDir.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvTitle.setSelected(false);
            }
        });
        //下拉框的点击事件
        popDir.setOnPopClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出路径Tag
                PhotoVideoDir dir = (PhotoVideoDir) v.getTag();
                currentDir = dir;
                tvTitle.setText(dir.dirName);
                loadImages(currentDir);
                popDir.dismiss();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (getIntent() != null && getIntent().hasExtra("loadType")) {
            loadType = PhotoVideoDir.Type.valueOf(getIntent().getStringExtra("loadType"));
        } else {
            Toast.makeText(this, "请传入loadType", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 判断类型为图像
     */
    private boolean isImageType() {
        return loadType == PhotoVideoDir.Type.IMAGE;
    }

    /**
     * 判断类型为视频
     */
    private boolean isVideoType() {
        return loadType == PhotoVideoDir.Type.VIDEO;
    }


    /**
     * 开启一个子线程加载下拉框图片列表
     */
    private void loadImagesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询到图片的地址
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE},
                        "" + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                while (cursor.moveToNext()) {
                    //获取到图片地址
                    String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageFile = new File(filePath);
                    PhotoVideoDir dir = addToDir(imageFile);
                    // 文件中图片的长度
                    if (dir.files.size() > maxPicSize) {
                        maxPicSize = dir.files.size();
                        currentDir = dir;
                    }
                    if (selectedFath.contains(filePath)) {
                        dir.selectedFiles.add(filePath);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //加载图片列表
                        if (currentDir == null) {
                            currentDir = new PhotoVideoDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
                            currentDir.dirName = "Pictures";
                            loadImages(currentDir);
                            Toast.makeText(PhotoMediaActivity.this, "图片列表为空", Toast.LENGTH_SHORT).show();
                        } else {
                            loadImages(currentDir);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 开启一个子线程加载下拉框视频列表
     */
    private void loadVideosList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media._ID},
                        null, null,
                        MediaStore.Images.Media.DATE_MODIFIED);
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                    File imageFile = new File(filePath);
                    PhotoVideoDir dir = addToDir(imageFile);
                    //dir.ids.add(id+"");
                    dir.setType(PhotoVideoDir.Type.VIDEO);
                    if (dir.files.size() > maxPicSize) {
                        maxPicSize = dir.files.size();
                        currentDir = dir;
                    }
                    if (selectedFath.contains(filePath)) {
                        dir.selectedFiles.add(filePath);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentDir == null) {
                            currentDir = new PhotoVideoDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath());
                            currentDir.dirName = "Movies";
                            loadVideoImages(currentDir);
                            Toast.makeText(PhotoMediaActivity.this, "视频列表为空", Toast.LENGTH_SHORT).show();
                        } else {
                            loadVideoImages(currentDir);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 添加图片或视频地址
     *
     * @param imageFile
     */
    private PhotoVideoDir addToDir(File imageFile) {
        PhotoVideoDir imageDir;
        File parentDirFile = imageFile.getParentFile();
        String parentFilePath = parentDirFile.getPath();
        if (!dirMap.containsKey(parentFilePath)) {
            imageDir = new PhotoVideoDir(parentFilePath);
            imageDir.dirName = parentDirFile.getName();
            dirMap.put(parentFilePath, imageDir);
            imageDir.firstPath = imageFile.getPath();
            imageDir.addFile(imageFile.toString());
        } else {
            imageDir = dirMap.get(parentFilePath);
            imageDir.addFile(imageFile.toString());
        }
        return imageDir;
    }

    /**
     * 加载文件夹的图片路径
     *
     * @param imageDir 图片路径实体
     */
    private void loadImages(final PhotoVideoDir imageDir) {
        PhotoMediaAdapter adapter = new PhotoMediaAdapter(PhotoMediaActivity.this, imageDir, loadType);
        gvPhotos.setAdapter(adapter);
        //选择图片事件
        adapter.setOnItemCheckdedChangedListener(new PhotoMediaAdapter.onItemCheckedChangedListener() {
            @Override
            public void onItemCheckChanged(CompoundButton chBox, boolean isCheced, PhotoVideoDir imageDir, String path) {
                //判断是否可选，如果选择大于9张，则不能再选了，否则可以选择
                if (isCheced) {
                    if (getSelectedPictureCont() >= maxCount) {
                        Toast.makeText(PhotoMediaActivity.this, "不能选择超过" + maxCount, Toast.LENGTH_SHORT).show();
                        chBox.setChecked(false);
                        imageDir.selectedFiles.remove(path);
                    } else {
                        imageDir.selectedFiles.add(path);
                    }
                } else {
                    imageDir.selectedFiles.remove(path);
                }
                updateNext();
            }

            @Override
            public void onTakePicture(PhotoVideoDir imageDir) {
                //在图片选择中拍照
                takePicture(imageDir);
            }

            @Override
            public void onShowPicture(String path) {
                //显示点击的具体图片
                showPreviewImage(path);
            }
        });
    }

    /**
     * 加载视频
     *
     * @param imageDir
     */
    private void loadVideoImages(final PhotoVideoDir imageDir) {
        PhotoMediaAdapter adapter = new PhotoMediaAdapter(PhotoMediaActivity.this, imageDir, loadType);
        gvPhotos.setAdapter(adapter);

        //视频选择事件
        adapter.setOnItemCheckdedChangedListener(new PhotoMediaAdapter.onItemCheckedChangedListener() {

            @Override
            public void onItemCheckChanged(CompoundButton chBox, boolean isCheced, PhotoVideoDir imageDir, String path) {
                //判断是否可选，如果选择大于3张，则不能再选了，否则可以选择
                if (isCheced) {
                    if (getSelectedPictureCont() >= maxCount) {
                        Toast.makeText(PhotoMediaActivity.this, "不能选择超过" + maxCount, Toast.LENGTH_SHORT).show();
                        chBox.setChecked(false);
                        imageDir.selectedFiles.remove(path);
                    } else {
                        imageDir.selectedFiles.add(path);
                    }
                } else {
                    imageDir.selectedFiles.remove(path);
                }
                updateNext();
            }

            @Override
            public void onTakePicture(PhotoVideoDir imageDir) {
                //在录像选择中录像
                takeVideo(imageDir);
            }

            @Override
            public void onShowPicture(String path) {
                //显示具体的图片
//                showPreviewImage(path);
            }
        });
    }


    /**
     * 获取图片的选择路径
     *
     * @return 路径集合
     */
    public ArrayList<String> getSelectedPicture() {
        ArrayList<String> paths = new ArrayList<String>();
        for (String name : dirMap.keySet()) {
            paths.addAll(dirMap.get(name).selectedFiles);
        }
        return paths;
    }

    /**
     * 获取现在的图片数
     *
     * @return 选择数
     */
    public int getSelectedPictureCont() {
        int count = 0;
        for (String name : dirMap.keySet()) {
            count += dirMap.get(name).selectedFiles.size();
        }

        return count;
    }

    /**
     * 在图片选择中拍照
     *
     * @param imageDir
     */
    public void takePicture(PhotoVideoDir imageDir) {
        if (getSelectedPictureCont() >= maxCount) {
            Toast.makeText(this, "拍照前被选中照片张数不能大于" + maxCount, Toast.LENGTH_LONG).show();
            return;
        }
        cameraFile = new File(imageDir.dirPath, "yusion4s-" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 在录像选择中录像
     *
     * @param imageDir
     */
    public void takeVideo(PhotoVideoDir imageDir) {
        cameraFile = new File(imageDir.dirPath, "yusion4s-" + System.currentTimeMillis() + ".mp4");
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        startActivityForResult(intent, REQUEST_CODE_CAMERA);
        startActivityForResult(new Intent(this, CustomCameraActivity.class), REQUEST_TAKE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        //在相册选择中拍照返回事件
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (cameraFile != null && cameraFile.exists()) {

                File source = new File(cameraFile.getAbsolutePath());
                if (source.length() <= (500 << 10)) {
                    //小于500kb不需要压缩
                    updateGalleray(cameraFile.getPath());
                    currentDir.selectedFiles.add(cameraFile.getPath());
                    currentDir.files.add(0, cameraFile.getPath());
                    loadImages(currentDir);
                    updateNext();
                }else {
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("正在压缩图片...");
                    dialog.show();
                    Luban.with(this).load(cameraFile).ignoreBy(500).setTargetDir(cameraFile.getParent()).setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            Toast.makeText(PhotoMediaActivity.this, "图片压缩成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (file.getAbsolutePath().equals(cameraFile.getAbsolutePath())) {
                                //图片太小没有压缩
                                updateGalleray(cameraFile.getPath());
                                currentDir.selectedFiles.add(cameraFile.getPath());
                                currentDir.files.add(0, cameraFile.getPath());
                                loadImages(currentDir);
                                updateNext();
                            } else {
                                //替换图片
                                try {
                                    FileOutputStream outputStream = new FileOutputStream(cameraFile);
                                    FileInputStream inputStream = new FileInputStream(file);
                                    byte[] bytes = new byte[inputStream.available()];
                                    inputStream.read(bytes);
                                    outputStream.write(bytes);
                                    inputStream.close();
                                    outputStream.close();
                                    file.delete();
//
                                    updateGalleray(cameraFile.getPath());
                                    currentDir.selectedFiles.add(cameraFile.getPath());
                                    currentDir.files.add(0, cameraFile.getPath());
                                    loadImages(currentDir);
                                    updateNext();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(PhotoMediaActivity.this, "图片压缩失败", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            updateGalleray(cameraFile.getPath());
                            currentDir.selectedFiles.add(cameraFile.getPath());
                            currentDir.files.add(0, cameraFile.getPath());
                            loadImages(currentDir);
                            updateNext();
                        }
                    }).launch();
                }


//                updateGalleray(cameraFile.getPath());
//                currentDir.selectedFiles.add(cameraFile.getPath());
//                currentDir.files.add(0, cameraFile.getPath());
//                loadImages(currentDir);
//                updateNext();
            }
        }
        if (requestCode == REQUEST_TAKE_VIDEO) {
            String video_path = data.getStringExtra("video_path");
            cameraFile = new File(video_path);

            if (!dirMap.containsKey(cameraFile.getParentFile().getPath())) {
                File parentDirFile = cameraFile.getParentFile();
                PhotoVideoDir imageDir = new PhotoVideoDir(parentDirFile.getPath());
                imageDir.dirName = parentDirFile.getName();
                dirMap.put(parentDirFile.getPath(), imageDir);
                currentDir = imageDir;
                imageDir.firstPath = cameraFile.getPath();
//                imageDir.addFile(cameraFile.toString());
            }

            if (cameraFile.exists()) {
                updateGalleray(cameraFile.getPath());
                currentDir.selectedFiles.add(cameraFile.getPath());
                currentDir.files.add(0, cameraFile.getPath());
                loadVideoImages(currentDir);
                updateNext();
            }
        }
        //在视频选择中录像返回事件
        if (requestCode == REQUEST_CODE_VEDIO) {
            if (cameraFile != null && cameraFile.exists()) {
                updateGalleray(cameraFile.getPath());
                currentDir.selectedFiles.add(cameraFile.getPath());
                currentDir.files.add(0, cameraFile.getPath());
                loadVideosList();
                updateNext();
            }
        }
        //在图片选择点击后返回事件
        if (requestCode == REQUEST_CODE_IMAGE_SWITCHER) {
            String[] paths = data.getStringArrayExtra("pickerPaths");
            for (int i = 0; i < paths.length; i++) {
                currentDir.selectedFiles.add(paths[i]);
            }
            loadImages(currentDir);
            updateNext();
        }
    }

    /**
     * 更新下一步
     */
    public void updateNext() {
        //如果选择了图片,btnNext可以选择，记录选择的数量，设置字体为白色。否则不能选择，设置字体为黑色
        if (getSelectedPictureCont() > 0) {
            btnNext.setSelected(true);
            btnNext.setText("下一步(" + getSelectedPictureCont() + ")");
            btnNext.setTextColor(Color.WHITE);
        } else {
            btnNext.setSelected(false);
            btnNext.setText("下一步");
            btnNext.setTextColor(Color.BLACK);
        }
    }

    /**
     * 发广播更新图册
     *
     * @param path
     */
    public void updateGalleray(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * 下一步按钮
     *
     * @param view
     */
    public void goNext(View view) {
        if (getSelectedPictureCont() != 0) {
            Intent intent = new Intent();
            intent.putExtra("files", getSelectedPicture());
            //返回成功给DemoActivity界面
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 获取文件的大小
     *
     * @param path
     * @return
     */
    private String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    /**
     * 取消按钮
     *
     * @param view
     */
    public void goBack(View view) {
        finish();
    }

    /**
     * 选择按钮
     *
     * @param view
     */
    public void popImageDir(View view) {
        if (popDir.isShowing()) {
            popDir.dismiss();
            view.setSelected(false);
        } else {
            popDir.popWindow(dirMap, lyTopBar);
            view.setSelected(true);
        }
    }

    /**
     * 显示预览图片
     *
     * @param path
     */
    private void showPreviewImage(String path) {
        PhotoVideoDir dir = currentDir;
        Bundle b = new Bundle();
        //选择的文件路径
        b.putStringArrayList("paths", (ArrayList<String>) dir.getFiles());
        //当前的路径
        b.putString("currentPath", path);
        //剩余选择数
        b.putInt("otherCount", getSelectedPictureCont() - dir.selectedFiles.size());
        //已选择
        b.putStringArray("selectedPaths", dir.selectedFiles.toArray(new String[]{}));
        //最大能够选择多少
        b.putInt("maxCount", maxCount);
        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra("params", b);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_SWITCHER);
    }
}
