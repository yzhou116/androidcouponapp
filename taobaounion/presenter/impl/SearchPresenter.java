package com.cvte.taobaounion.presenter.impl;

import android.util.Log;

import com.cvte.taobaounion.model.Api;
import com.cvte.taobaounion.model.TicketPramas;
import com.cvte.taobaounion.model.domain.Categories;
import com.cvte.taobaounion.model.domain.Histories;
import com.cvte.taobaounion.model.domain.HomePagerContent;
import com.cvte.taobaounion.model.domain.OnSaleContent;
import com.cvte.taobaounion.model.domain.SearchContent;
import com.cvte.taobaounion.model.domain.SearchRecommend;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.model.domain.SelectedContent;
import com.cvte.taobaounion.model.domain.TicketResult;
import com.cvte.taobaounion.presenter.ISearchPresenter;
import com.cvte.taobaounion.utils.JsonCacheUtil;
import com.cvte.taobaounion.utils.RetrofitManager;
import com.cvte.taobaounion.view.ISearchViewCallBack;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {


    public Api mApi;
    private ISearchViewCallBack mSearchViewCallback = null;
    private int mCurrentPage = 0;
    private String mSearchKeyWord = "";
    public JsonCacheUtil mJsonCacehUtil;


    public SearchPresenter() {
        RetrofitManager instance = RetrofitManager.getInstance();
        Retrofit retrofit = instance.getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacehUtil = JsonCacheUtil.getInstance();


    }

    @Override
    public void getHistory() {
        Histories histories = mJsonCacehUtil.getValue(KEY_HISTORY, Histories.class);
        mSearchViewCallback.onHistoryLoad(histories.getHistories());

    }

    @Override
    public void deleteHistory() {
        mJsonCacehUtil.delCache(KEY_HISTORY);


    }

    public static final String KEY_HISTORY = "key_history";

    private void saveHistory(String history) {


        Histories histories = mJsonCacehUtil.getValue(KEY_HISTORY, Histories.class);
        List<String> historyList = null;
        if (histories != null && histories.getHistories() != null) {
            historyList = histories.getHistories();
            if (historyList.contains(history)) {
                historyList.remove(history);
            }
        }
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        if (histories == null) {
            histories = new Histories();


        }
        histories.setHistories(historyList);

        if (historyList.size() > 10) {
            historyList = historyList.subList(0, 10);
        }
        historyList.add(history);

        mJsonCacehUtil.saveCache(KEY_HISTORY, histories);

    }


    @Override
    public void doSearch(String keyWord) {

        this.mSearchKeyWord = keyWord;
        this.saveHistory(keyWord);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
        Call<SearchContent> task = mApi.doSearch(mCurrentPage, keyWord);
        task.enqueue(new Callback<SearchContent>() {
            @Override
            public void onResponse(Call<SearchContent> call, Response<SearchContent> response) {
                int code = response.code();
                Log.d("SearchPresenter", "doSearch--->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();

                }
            }

            @Override
            public void onFailure(Call<SearchContent> call, Throwable t) {
                onError();
            }
        });

    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onNetworkError();
        }

    }

    private void handleSearchResult(SearchContent body) {
        if (mSearchViewCallback != null) {
            if (isResultNull(body)) {
                //数据为空：
                mSearchViewCallback.onEmpty();
            } else {
                mSearchViewCallback.onSearchSuccess(body);


            }
        }

    }

    private boolean isResultNull(SearchContent body) {
        try {
            return body == null ||
                    body.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void Research() {
        if (mSearchKeyWord == "") {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {

            this.doSearch(mSearchKeyWord);
        }


    }

    @Override
    public void LoadMore() {
        mCurrentPage++;
        if (mSearchKeyWord == null) {
            mSearchViewCallback.onEmpty();
        } else {
            doSearchMore();
        }

    }

    private void doSearchMore() {
        Call<SearchContent> task = mApi.doSearch(mCurrentPage, mSearchKeyWord);
        task.enqueue(new Callback<SearchContent>() {
            @Override
            public void onResponse(Call<SearchContent> call, Response<SearchContent> response) {
                int code = response.code();
                Log.d("SearchPresenter", "doSearch--->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoadMoreError();

                }
            }

            @Override
            public void onFailure(Call<SearchContent> call, Throwable t) {
                t.printStackTrace();
                onLoadMoreError();
            }
        });


    }

    private void handleMoreSearchResult(SearchContent body) {
        if (mSearchViewCallback != null) {
            if (isResultNull(body)) {
                //数据为空：
                mSearchViewCallback.onMoreLoadEmpty();
            } else {
                mSearchViewCallback.onMoreLoad(body);
            }
        }

    }

    private void onLoadMoreError() {
        mCurrentPage--;
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onMoreLoadError();
        }

    }

    @Override
    public void getRecommendWord() {
        Call<SearchRecommend> task = mApi.getRecommendWord();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                Log.d("SearchPresenter", "getRecommendWord---->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //处理结果：
                    if (mSearchViewCallback != null) {
                        List<SearchRecommend.DataBean> result = response.body().getData();
                        mSearchViewCallback.getRecommendWordLoaded(result);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    @Override
    public void registerViewCallback(ISearchViewCallBack callback) {
        this.mSearchViewCallback = callback;

    }

    @Override
    public void unregisterViewCallback(ISearchViewCallBack callback) {
        this.mSearchViewCallback = null;


    }
}
