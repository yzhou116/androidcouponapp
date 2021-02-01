package com.cvte.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.base.BaseFragment;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.model.domain.SelectedContent;
import com.cvte.taobaounion.presenter.ISelectedPresenter;
import com.cvte.taobaounion.ui.adapter.SelectContentAdapter;
import com.cvte.taobaounion.ui.adapter.SelectLeftAdapter;

import com.cvte.taobaounion.utils.PresenterManager;
import com.cvte.taobaounion.utils.SizeUtils;
import com.cvte.taobaounion.view.ISelectPageCallback;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindAnim;
import butterknife.BindView;


public class SelectFragment extends BaseFragment implements ISelectPageCallback, SelectLeftAdapter.onLeftItemclickListener {

    @BindView(R.id.select_title_rec)
    public RecyclerView leftList;


    @BindView(R.id.select_right_content)
    public RecyclerView rightList;


    private static final String TAG = "SelectFragment";
    public ISelectedPresenter selectPagePresenter;
    public SelectLeftAdapter selectLeftAdapter;
    public SelectContentAdapter selectContentAdapter;


    @Override
    protected void initPresenter() {
        super.initPresenter();
        selectPagePresenter = PresenterManager.getInstance().getSelectPagePresenter();
        selectPagePresenter.registerViewCallback(this);
        selectPagePresenter.getCategory();
    }
/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        selectPagePresenter.unregisterViewCallback(this);
    }

 */

    @Override
    protected int getRootVireResId() {
        return R.layout.fragment_select;
    }

    @Override
    protected void initView(View rootView) {

        setUpState(State.SUCCESS);
        leftList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectLeftAdapter = new SelectLeftAdapter();
        leftList.setAdapter(selectLeftAdapter);


        rightList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectContentAdapter = new SelectContentAdapter();
        rightList.setAdapter(selectContentAdapter);


        /*

        rightList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topbot = SizeUtils.dip2px(getContext(), 4);
                int rightleft = SizeUtils.dip2px(getContext(), 6);
                outRect.top=topbot;
                outRect.bottom=topbot;
                outRect.left=rightleft;
                outRect.right=rightleft;

            }
        });

         */
    }

    @Override
    protected void initViewListener() {
        super.initViewListener();
        selectLeftAdapter.setonLeftItemClickListener(this);
    }

    @Override
    public void onCategoryLoad(SelectPageCategory selectPageCategory) {
        setUpState(State.SUCCESS);
        selectLeftAdapter.setData(selectPageCategory);
        Log.d(TAG, "onCategoryLoad----->" + selectPageCategory);
        //TODO:更新UI；
        List<SelectPageCategory.DataBean> data = selectPageCategory.getData();
    }

    @Override
    public void onContentLoad(SelectedContent selectedContent) {
        Log.d(TAG, "selectedContent --->" + selectedContent);
        selectContentAdapter.setdata(selectedContent);
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onItemClick(SelectPageCategory.DataBean item) {
        //点击左边分类：
        selectPagePresenter.getContentByCategory(item);

    }
}
