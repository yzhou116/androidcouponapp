package com.cvte.taobaounion.view;

import com.cvte.taobaounion.base.IBaseCallback;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.model.domain.SelectedContent;

public interface ISelectPageCallback extends IBaseCallback {

    void onCategoryLoad(SelectPageCategory selectPageCategory);

    void onContentLoad(SelectedContent selectedContent);

}
