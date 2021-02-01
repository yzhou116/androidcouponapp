package com.cvte.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cvte.taobaounion.R;
import com.cvte.taobaounion.base.BaseActivity;
import com.cvte.taobaounion.model.domain.TicketResult;
import com.cvte.taobaounion.presenter.ITicketPresenter;
import com.cvte.taobaounion.ui.custom.LoadingView;
import com.cvte.taobaounion.utils.LogUtils;
import com.cvte.taobaounion.utils.PresenterManager;
import com.cvte.taobaounion.utils.UrlUtils;
import com.cvte.taobaounion.view.ITicketPageCallback;

import butterknife.BindView;

import static android.view.View.VISIBLE;

public class TicketActivity extends BaseActivity implements ITicketPageCallback {

    public ITicketPresenter ticketPresenter;
    @BindView(R.id.ticket_cover)
    public ImageView mCover;
    @BindView(R.id.ticket_back)
    public ImageView mBack;
    @BindView(R.id.ticket_code)
    public EditText mCode;
    @BindView(R.id.ticket_copy)
    public TextView mCopy;
    @BindView(R.id.loading_cover)
    public LoadingView loader;

    private boolean hasTaobao = false;


    private String TAG = "TicketActivity";

    @Override
    protected void initPresenter() {
        ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (ticketPresenter != null) {
            ticketPresenter.registerViewCallback(this);
        }
        //判断手机是否有淘宝：
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            hasTaobao = packageInfo != null;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            hasTaobao = false;
        }
        //根据有没有淘宝的bool值去修改ui：
        mCopy.setText(hasTaobao ? "可以领劵" : "复制淘口令");

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRescourceID() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult ticketResult) {
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }
        Log.d(TAG, "onTicketLoaded" + cover);
        String mcode = ticketResult.getData().getTbk_tpwd_create_response().getData().getModel();
        Log.d(TAG, "onTicketLoaded===>>" + mcode);
        String mResult = cutString(mcode);
        Log.d(TAG, "onTicketLoaded===>>" + mResult);

        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String result = UrlUtils.getTicketUrl(cover);
            Glide.with(this).load(result).into(mCover);
        }
        if (ticketResult != null && ticketResult.getData().getTbk_tpwd_create_response() != null) {
            mCode.setText(mResult);
        }

    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mCodeText = mCode.getText().toString().trim();
                ClipboardManager systemService = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData code = ClipData.newPlainText("sob_tao_bao_ticket_code", mCodeText);
                systemService.setPrimaryClip(code);

                if (hasTaobao) {
                    Intent taobao = new Intent();
                    //taobao.setAction("android.intent.action.MAIN");
                    // taobao.addCategory("android.intent.category.LAUNCHER");


                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobao.setComponent(componentName);
                    startActivity(taobao);
                } else {
                    Toast.makeText(TicketActivity.this, "已经复制口令 ：" + mCodeText, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private String cutString(String mcode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mcode.length(); i++) {
            if (mcode.charAt(i) == '￥') {
                sb.append(mcode.charAt(i));
                for (int j = i + 1; j < mcode.length(); j++) {
                    sb.append(mcode.charAt(j));
                    if (mcode.charAt(j) == '￥') {
                        i = mcode.length() - 1;
                        break;
                    }
                }
            }
        }
        return sb.toString();

    }


    @Override
    protected void release() {
        if (ticketPresenter != null) {
            ticketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onNetworkError() {
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoading() {
        if (loader != null) {
            loader.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onEmpty() {
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }

    }
}
