package com.cvte.taobaounion.presenter.impl;

import com.cvte.taobaounion.model.Api;
import com.cvte.taobaounion.model.domain.HomePagerContent;
import com.cvte.taobaounion.presenter.ICategoryPagerPresenter;
import com.cvte.taobaounion.utils.LogUtils;
import com.cvte.taobaounion.utils.RetrofitManager;
import com.cvte.taobaounion.utils.UrlUtils;
import com.cvte.taobaounion.view.ICategoryCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by user on 2020/10/26.
 */

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    public int currentPage;


    private Map<Integer, Integer> pagesInfo = new HashMap<>();
    private static final int DEFAULT_PAGE = 1;
    private static final String TAG = "CategoryPagerPresenterImpl";
    private List<ICategoryCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryCallback callback) {
        callbacks.remove(callback);
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        /*根据分类ID加载内容*/

        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            pagesInfo.put(categoryId, DEFAULT_PAGE);
            targetPage = DEFAULT_PAGE;
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                //        LogUtils.d(TAG,"response code-->"+code);
                if (code == HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    //            LogUtils.d(TAG,"pagerContent --> "+pagerContent);
                    //数据回调给UI
                    handleHomePagerContentResult(pagerContent, categoryId);
                } else {
                    //TODO:
                    handleHomeNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(TAG, "onFailure--> " + t.toString());
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(TAG, "homePagerUrl --> " + homePagerUrl);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePagerContent(homePagerUrl);
    }

    private void handleHomeNetworkError(int categoryId) {
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePagerContentResult(HomePagerContent pagerContent, int categoryId) {
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (ICategoryCallback callback : callbacks) {
            if (pagerContent == null || pagerContent.getData().size() == 0) {
                if (callback.getCategoryId() == categoryId) {
                    callback.onEmpty();
                }

            } else {
                if (callback.getCategoryId() == categoryId) {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        //加载数据：
        currentPage = pagesInfo.get(categoryId);
        currentPage++;
        if (currentPage == 0) {
            currentPage = 1;

        }
        currentPage++;
        Call<HomePagerContent> task = createTask(categoryId, currentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    handerLoadmoreResult(result, categoryId);

                } else {
                    handerloadmoreErro(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //请求失败
                LogUtils.d(TAG, t.toString());
                handerloadmoreErro(categoryId);

            }
        });

    }

    private void handerLoadmoreResult(HomePagerContent result, int categoryId) {
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreLoaded(result.getData());
                }
            }

        }
    }

    private void handerloadmoreErro(int categoryId) {
        currentPage--;
        pagesInfo.put(categoryId, currentPage);
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }
}
