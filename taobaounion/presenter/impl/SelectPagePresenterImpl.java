package com.cvte.taobaounion.presenter.impl;

import android.util.Log;

import com.cvte.taobaounion.model.Api;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.model.domain.SelectedContent;
import com.cvte.taobaounion.presenter.ISelectedPresenter;
import com.cvte.taobaounion.utils.RetrofitManager;
import com.cvte.taobaounion.utils.UrlUtils;
import com.cvte.taobaounion.view.ISelectPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectPagePresenterImpl implements ISelectedPresenter {

    private static final String TAG = "SelectPagePresenterImpl";
    private ISelectPageCallback mViewCallback = null;
    public Api api;
    private SelectPageCategory.DataBean mCurrentCategoryID = null;

    public SelectPagePresenterImpl() {

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);

    }

    @Override
    public void getCategory() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<SelectPageCategory> task = api.getSelectPageCategory();

        task.enqueue(new Callback<SelectPageCategory>() {
            @Override
            public void onResponse(Call<SelectPageCategory> call, Response<SelectPageCategory> response) {
                int code = response.code();
                Log.d(TAG, "response.code---->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectPageCategory body = response.body();
                    //TODO:通知UI更新：
                    if (mViewCallback != null) {
                        mViewCallback.onCategoryLoad(body);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectPageCategory> call, Throwable t) {
                onLoadError();
            }
        });
    }

    private void onLoadError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }

    @Override
    public void getContentByCategory(SelectPageCategory.DataBean item) {
        this.mCurrentCategoryID = item;
        if (mCurrentCategoryID != null) {
            int favorites_id = item.getFavorites_id();
            String targetUrl = UrlUtils.getSelectPageContentUrl(favorites_id);
            Call<SelectedContent> task = api.getSelectContent(targetUrl);
            task.enqueue(new Callback<SelectedContent>() {
                @Override
                public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                    int code = response.code();
                    if (code == HttpURLConnection.HTTP_OK) {
                        Log.d(TAG, "response.code---->" + code);
                        SelectedContent body = response.body();
                        if (mViewCallback != null) {
                            mViewCallback.onContentLoad(body);
                        }
                    } else {
                        onLoadError();
                    }
                }

                @Override
                public void onFailure(Call<SelectedContent> call, Throwable t) {
                    onLoadError();
                }
            });
        }
    }

    @Override
    public void reloadContent() {
        if (mCurrentCategoryID != null) {
            this.getContentByCategory(mCurrentCategoryID);
        }
    }

    @Override
    public void registerViewCallback(ISelectPageCallback callback) {
        this.mViewCallback = callback;

    }

    @Override
    public void unregisterViewCallback(ISelectPageCallback callback) {
        this.mViewCallback = null;

    }
}
