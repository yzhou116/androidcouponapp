package com.cvte.taobaounion.presenter;

import com.cvte.taobaounion.base.IBasePresenter;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.view.ISelectPageCallback;

public interface ISelectedPresenter extends IBasePresenter<ISelectPageCallback> {
    //获取分类
    void getCategory();
    /*
    根绝分类获取内容
     */

    void getContentByCategory(SelectPageCategory.DataBean item);

    void reloadContent();


}
