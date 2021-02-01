package com.cvte.taobaounion.presenter.impl;

import android.util.Log;

import com.cvte.taobaounion.model.Api;
import com.cvte.taobaounion.model.domain.OnSaleContent;
import com.cvte.taobaounion.view.IOnSalePageCallback;
import com.cvte.taobaounion.presenter.IOnSalePagePresenter;

import com.cvte.taobaounion.utils.RetrofitManager;
import com.cvte.taobaounion.utils.UrlUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSalePagePresenter implements IOnSalePagePresenter {
    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSalePageCallback onSalePageCallback = null;
    public Api api;

    public OnSalePagePresenter() {
        /**
         * 这边的话也是返回retrofit的单例
         */

        Log.d("OnSalePagePresenter", "onSalePagePresenter is created");
    }

    @Override
    public void getContent() {
        //获取特惠内容：
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        api = retrofit.create(Api.class);
        /**
         主要问题是这个为什么我的代码根本就不进去，里面的log根本就打不出来。
         */
        String onsalePageUrl = UrlUtils.getOnsalePageUrl(mCurrentPage);
        Call<OnSaleContent> task = api.getOnSalePageContent(onsalePageUrl);
        task.enqueue(new Callback<OnSaleContent>() {
            @Override
            public void onResponse(Call<OnSaleContent> call, Response<OnSaleContent> response) {
                int code = response.code();
                Log.d("OnSalePagePresenter", "getContent---->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSaleContent body = response.body();
                    //     onSuccess(body);
                    onSalePageCallback.onContentLoadSuccess(body);
                    Log.d("OnSalePagePresenter", "OnSaleContent---->" + body);
                } else {
                    if (onSalePageCallback != null) {
                        onSalePageCallback.onNetworkError();
                    }
                }
            }

            @Override
            public void onFailure(Call<OnSaleContent> call, Throwable t) {
                if (onSalePageCallback != null) {
                    onSalePageCallback.onNetworkError();
                }

            }
        });
    }

    /*
    private void onSuccess(OnSaleContent body) {
        if (onSalePageCallback!=null) {
            try{
                int size = body.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                Log.d("onSuccess" ,"onSuccess --->" + size);
                if (size==0) {
                    onEmppty();
                }else{
                    onSalePageCallback.onContentLoadSuccess(body);
                }
            }catch (Exception e){
                e.printStackTrace();
                onEmppty();
            }
        }
    }

     */
    private void onEmppty() {
        Log.d("onEmppty", "onEmppty");
        if (onSalePageCallback != null) {
            onSalePageCallback.onEmpty();
        }
    }

    @Override
    public void reLoad() {
        this.getContent();

    }

    @Override
    public void LoadMore() {
        mCurrentPage++;
        String url = UrlUtils.getOnsalePageUrl(mCurrentPage);
        Call<OnSaleContent> task = api.getOnSalePageContent(url);

        task.enqueue(new Callback<OnSaleContent>() {
            @Override
            public void onResponse(Call<OnSaleContent> call, Response<OnSaleContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSaleContent body = response.body();

                    onMoreLoaded(body);
                } else {
                    onLoadMoreError();

                }
            }

            @Override
            public void onFailure(Call<OnSaleContent> call, Throwable t) {
                onLoadMoreError();
            }
        });
    }


    private void onLoadMoreError() {
        mCurrentPage--;
        onSalePageCallback.onMoreLoadError();

    }

    private void onMoreLoaded(OnSaleContent body) {

        if (onSalePageCallback != null) {
            try {
                int size = body.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if (size == 0) {
                    onEmppty();
                } else {
                    //      onSalePageCallback.onContentLoadSuccess(body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmppty();
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSalePageCallback callback) {
        this.onSalePageCallback = callback;

    }

    @Override
    public void unregisterViewCallback(IOnSalePageCallback callback) {
        this.onSalePageCallback = null;
    }
}
