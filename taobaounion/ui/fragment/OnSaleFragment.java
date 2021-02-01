package com.cvte.taobaounion.ui.fragment;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.base.BaseFragment;
import com.cvte.taobaounion.model.domain.OnSaleContent;
import com.cvte.taobaounion.view.IOnSalePageCallback;

import com.cvte.taobaounion.presenter.impl.OnSalePagePresenter;
import com.cvte.taobaounion.ui.adapter.OnSalePageAdapter;
import com.cvte.taobaounion.utils.PresenterManager;

import butterknife.BindView;


public class OnSaleFragment extends BaseFragment implements IOnSalePageCallback {
    @BindView(R.id.on_sale_list)
    public RecyclerView onSaleList;
    public OnSalePageAdapter onSalePageAdapter;
    public OnSalePagePresenter onSalePagePresenter;

    @Override
    protected void initPresenter() {

        /**
         这里是有个PresenterManager类里面返回各个fragment中presenter的单例
         */
        onSalePagePresenter = PresenterManager.getInstance().getOnSalePagePresenter();
        onSalePagePresenter.registerViewCallback(this);
        onSalePagePresenter.getContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onSalePagePresenter.unregisterViewCallback(this);
    }

    @Override
    protected int getRootVireResId() {
        /**

         */
        Log.d("OnSaleFragment", "fragment_red_packet is created");
        return R.layout.fragment_red_packet;

    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        onSalePageAdapter = new OnSalePageAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        onSaleList.setLayoutManager(gridLayoutManager);
        onSaleList.setAdapter(onSalePageAdapter);
    }

    @Override
    public void onContentLoadSuccess(OnSaleContent content) {
        //数据回来：
        // Log.d("OnSaleFragment", "OnSaleContent" + content );
        onSalePageAdapter.setData(content);
    }


    @Override
    public void onMoreloaded(OnSaleContent content) {

    }

    @Override
    public void onMoreLoadError() {

    }

    @Override
    public void onMoreLoadEmpty() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
