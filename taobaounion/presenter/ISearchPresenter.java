package com.cvte.taobaounion.presenter;

import com.cvte.taobaounion.base.IBasePresenter;
import com.cvte.taobaounion.view.ISearchViewCallBack;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallBack> {
    /**
     * 获取搜索历史
     */
    void getHistory();

    void deleteHistory();

    void doSearch(String keyWord);

    void Research();

    void LoadMore();


    void getRecommendWord();

}
