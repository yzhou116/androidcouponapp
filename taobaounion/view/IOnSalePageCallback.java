package com.cvte.taobaounion.view;

import com.cvte.taobaounion.base.IBaseCallback;
import com.cvte.taobaounion.model.domain.OnSaleContent;

public interface IOnSalePageCallback extends IBaseCallback {

    void onContentLoadSuccess(OnSaleContent content);

    void onMoreloaded(OnSaleContent content);

    void onMoreLoadError();

    void onMoreLoadEmpty();


}
