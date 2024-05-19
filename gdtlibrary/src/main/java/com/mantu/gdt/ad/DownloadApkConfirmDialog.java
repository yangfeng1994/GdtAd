package com.mantu.gdt.ad;

import static com.mantu.gdt.ad.DownloadApkConfirmDialogWebView.getScreenHeight;
import static com.mantu.gdt.ad.DownloadApkConfirmDialogWebView.getScreenWidth;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qq.e.comm.compliance.DownloadConfirmCallBack;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 * created by timfeng 2020/8/21
 */
public class DownloadApkConfirmDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "ConfirmDialog";
    private Context context;
    private int orientation;
    private DownloadConfirmCallBack callBack;
    private TextView mTVTitle, mTVDesc, mTVVersion, mTVSize, mTVUpdate, mTVPrivate, mTVContent;
    private ImageView close;
    private Button confirm;

    private ViewGroup contentHolder;
    private ProgressBar loadingBar;
    private Button reloadButton;

    private String url;

    private static final String RELOAD_TEXT = "重新加载";
    private static final String LOAD_ERROR_TEXT = "抱歉，应用信息获取失败";
    private RoundedImageView mRIVIcon;

    public DownloadApkConfirmDialog(Context context, String infoUrl,
                                    DownloadConfirmCallBack callBack) {
        super(context, R.style.DownloadConfirmDialogFullScreen);//需要全屏显示，同时显示非窗口蒙版
        this.context = context;
        this.callBack = callBack;
        this.url = infoUrl;
        orientation = context.getResources().getConfiguration().orientation;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        setContentView(R.layout.download_confirm_dialog_1);
        View root = findViewById(R.id.download_confirm_root);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            root.setBackgroundResource(R.drawable.download_confirm_background_portrait);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            root.setBackgroundResource(R.drawable.download_confirm_background_landscape);
        }
        close = findViewById(R.id.download_confirm_close);
        close.setOnClickListener(this);
        reloadButton = findViewById(R.id.download_confirm_reload_button);
        reloadButton.setOnClickListener(this);
        confirm = findViewById(R.id.download_confirm_confirm);
        confirm.setOnClickListener(this);
        loadingBar = findViewById(R.id.download_confirm_progress_bar);
        contentHolder = findViewById(R.id.download_confirm_content);
        mRIVIcon = findViewById(R.id.mRIVIcon);
        mTVTitle = findViewById(R.id.mTVTitle);
        mTVDesc = findViewById(R.id.mTVDesc);
        mTVVersion = findViewById(R.id.mTVVersion);
        mTVSize = findViewById(R.id.mTVSize);
        mTVUpdate = findViewById(R.id.mTVUpdate);
        mTVPrivate = findViewById(R.id.mTVPrivate);
        mTVContent = findViewById(R.id.mTVContent);
        mTVPrivate.setOnClickListener(v -> {
            String url = (String) v.getTag();
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("title", "隐私条款");
            context.startActivity(intent);
        });
        TextPaint paint = mTVPrivate.getPaint();
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);  //下划线
        paint.isAntiAlias();
        paint.setAntiAlias(true);  //抗锯齿
    }

    @Override
    public void show() {
        super.show();
        try {
            loadUrl(url);
        } catch (Exception e) {
            Log.e(TAG, "load error url:" + url, e);
        }
    }

    public void setInstallTip() {
        confirm.setText("立即安装");
    }

    private void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            loadingBar.setVisibility(View.GONE);
            contentHolder.setVisibility(View.GONE);
            reloadButton.setVisibility(View.VISIBLE);
            reloadButton.setText(LOAD_ERROR_TEXT);
            reloadButton.setEnabled(false);
            return;
        }
        new NetworkRequestAsyncTask() {
            @Override
            protected void onPostExecute(String str) {
                loadingBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);
                contentHolder.setVisibility(View.VISIBLE);

                DownloadConfirmHelper.ApkInfo apkInfo = DownloadConfirmHelper.getAppInfoFromJson(str);
                if (apkInfo == null) {
                    loadingBar.setVisibility(View.GONE);
                    reloadButton.setVisibility(View.VISIBLE);
                    contentHolder.setVisibility(View.GONE);
                    return;
                }
                onInitApkData(apkInfo);
                loadingBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.GONE);
                contentHolder.setVisibility(View.VISIBLE);
            }
        }.execute(url);
    }

    private void onInitApkData(DownloadConfirmHelper.ApkInfo apkInfo) {
        Glide.with(context).load(apkInfo.iconUrl).into(mRIVIcon);
        mTVTitle.setText(apkInfo.appName);
        mTVDesc.setText("开发者：" + apkInfo.authorName);
        mTVVersion.setText(apkInfo.versionName);
        mTVSize.setText(readableFileSize(apkInfo.fileSize));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mTVUpdate.setText("更新时间:" + sdf.format(new Date(apkInfo.apkPublishTime)));
        mTVPrivate.setText("查看隐私条款");
        mTVPrivate.setTag(apkInfo.privacyAgreementUrl);
        for (String i : apkInfo.permissions) {
            mTVContent.append("\t" + i + "\n");
        }
    }

    @Override
    protected void onStart() {
        int height = getScreenHeight(context);
        int width = getScreenWidth(context);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) (height * 0.6);
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.windowAnimations = R.style.DownloadConfirmDialogAnimationUp;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = (int) (width * 0.5);
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.RIGHT;
            layoutParams.windowAnimations = R.style.DownloadConfirmDialogAnimationRight;
        }
        //弹窗外区域蒙版50%透明度
        layoutParams.dimAmount = 0.5f;

        //resume后动画会重复，在显示出来后重置无动画
        window.setAttributes(layoutParams);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                try {
                    Window window = getWindow();
                    window.setWindowAnimations(0);
                } catch (Throwable t) {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == close) {
            dismiss();
        } else if (v == confirm) {
            String text = confirm.getText().toString();
            switch (text) {
                case "立即下载":
                    if (callBack != null) {
                        callBack.onConfirm();
                    }
                    confirm.setText("取消下载");
                    break;
                case "立即安装":
                    if (callBack != null) {
                        callBack.onConfirm();
                    }
                    break;
                default:
                    if (callBack != null) {
                        callBack.onCancel();
                    }
                    dismiss();
                    break;
            }

        } else if (v == reloadButton) {
            loadUrl(url);
        }

    }

    @Override
    public void cancel() {
        super.cancel();
        if (callBack != null) {
            callBack.onCancel();
        }
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
