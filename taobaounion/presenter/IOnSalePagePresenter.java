package com.cvte.taobaounion.presenter;

import com.cvte.taobaounion.base.IBasePresenter;
import com.cvte.taobaounion.view.IOnSalePageCallback;

public interface IOnSalePagePresenter extends IBasePresenter<IOnSalePageCallback> {
    void getContent();

    void reLoad();

    void LoadMore();
}
