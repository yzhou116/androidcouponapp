package com.cvte.taobaounion.view;

import com.cvte.taobaounion.base.IBaseCallback;
import com.cvte.taobaounion.model.domain.SearchContent;
import com.cvte.taobaounion.model.domain.SearchRecommend;

import java.util.List;

public interface ISearchViewCallBack extends IBaseCallback {

    void onHistoryLoad(List<String> list);


    void onHistoryDeleted();

    void onSearchSuccess(SearchContent searchContent);

    void onMoreLoad(SearchContent searchContent);


    void onMoreLoadError();

    void onMoreLoadEmpty();

    void getRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWord);


}
