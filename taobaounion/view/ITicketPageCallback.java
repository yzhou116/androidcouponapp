package com.cvte.taobaounion.view;

import com.cvte.taobaounion.base.IBaseCallback;
import com.cvte.taobaounion.model.domain.TicketResult;

public interface ITicketPageCallback extends IBaseCallback {
    //加载结果：
    void onTicketLoaded(String title, TicketResult ticketResult);
}
