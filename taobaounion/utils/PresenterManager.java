package com.cvte.taobaounion.utils;

import com.cvte.taobaounion.presenter.ICategoryPagerPresenter;
import com.cvte.taobaounion.presenter.IHomePresenter;
import com.cvte.taobaounion.presenter.ISearchPresenter;
import com.cvte.taobaounion.presenter.ISelectedPresenter;
import com.cvte.taobaounion.presenter.ITicketPresenter;

import com.cvte.taobaounion.presenter.TicketPresenterImpl;
import com.cvte.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.cvte.taobaounion.presenter.impl.HomePresenterImpl;
import com.cvte.taobaounion.presenter.impl.OnSalePagePresenter;
import com.cvte.taobaounion.presenter.impl.SearchPresenter;
import com.cvte.taobaounion.presenter.impl.SelectPagePresenterImpl;

public class PresenterManager {
    private static final PresenterManager sInstance = new PresenterManager();
    public ICategoryPagerPresenter categoryPagerPresenter;
    public IHomePresenter homePresenter;
    public ITicketPresenter ticketPresenter;
    public ISelectedPresenter selectPagePresenter;
    public OnSalePagePresenter onSalePagePresenter;
    public ISearchPresenter searchPresenter;


    public static PresenterManager getInstance() {
        return sInstance;
    }

    public OnSalePagePresenter getOnSalePagePresenter() {
        return onSalePagePresenter;
    }

    public PresenterManager() {
        categoryPagerPresenter = new CategoryPagerPresenterImpl();
        homePresenter = new HomePresenterImpl();
        ticketPresenter = new TicketPresenterImpl();
        selectPagePresenter = new SelectPagePresenterImpl();
        onSalePagePresenter = new OnSalePagePresenter();
        searchPresenter = new SearchPresenter();
    }

    public ISearchPresenter getSearchPresenter() {
        return searchPresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return ticketPresenter;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return categoryPagerPresenter;
    }

    public void setCategoryPagerPresenter(CategoryPagerPresenterImpl categoryPagerPresenter) {
        this.categoryPagerPresenter = categoryPagerPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }

    public ISelectedPresenter getSelectPagePresenter() {
        return selectPagePresenter;
    }
}
