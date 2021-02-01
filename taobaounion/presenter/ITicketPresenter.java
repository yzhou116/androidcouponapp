package com.cvte.taobaounion.presenter;

import com.cvte.taobaounion.base.IBasePresenter;
import com.cvte.taobaounion.view.ITicketPageCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPageCallback> {
    /**
     * 获取优惠券
     *
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title, String url, String cover);

}
