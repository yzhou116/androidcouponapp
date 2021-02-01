package com.cvte.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.base.BaseFragment;
import com.cvte.taobaounion.model.domain.Categories;
import com.cvte.taobaounion.model.domain.HomePagerContent;
import com.cvte.taobaounion.presenter.ICategoryPagerPresenter;
import com.cvte.taobaounion.presenter.ITicketPresenter;
import com.cvte.taobaounion.presenter.TicketPresenterImpl;
import com.cvte.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.cvte.taobaounion.ui.activity.TicketActivity;
import com.cvte.taobaounion.ui.adapter.HomePagerAdapter;
import com.cvte.taobaounion.ui.adapter.HomepageContentAdapter;
import com.cvte.taobaounion.ui.adapter.LooperPagerAdapter;
import com.cvte.taobaounion.ui.custom.TbNestedScrollView;
import com.cvte.taobaounion.utils.Constant;
import com.cvte.taobaounion.utils.LogUtils;
import com.cvte.taobaounion.utils.PresenterManager;
import com.cvte.taobaounion.utils.SizeUtils;
import com.cvte.taobaounion.utils.ToastUtils;
import com.cvte.taobaounion.view.ICategoryCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.security.PublicKey;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;

/**
 * Created by user on 2020/10/23.
 */

public class HomePagerFragment extends BaseFragment implements ICategoryCallback, HomepageContentAdapter.onListItemClickListner, LooperPagerAdapter.onPicClickListner {

    public static final String TAG = "HomePagerFragment";
    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public ViewPager looperPage;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homePagerHeaderContainer;
    @BindView(R.id.home_page_refresh)
    public TwinklingRefreshLayout homepageRefresh;


    public HomepageContentAdapter mHomepageContentAdapter;
    public LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_HOME_PAGE_TITLE, category.getTitle());
        bundle.putInt(Constant.KEY_HOME_PAGE_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootVireResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        mHomepageContentAdapter = new HomepageContentAdapter();

        mContentList.setAdapter(mHomepageContentAdapter);

        /*Viewpager创建轮播图适配器*/
        mLooperPagerAdapter = new LooperPagerAdapter();
        looperPage.setAdapter(mLooperPagerAdapter);
        //设置refresh相关内容：
        //    homepageRefresh.setEnableRefresh(false);
        homepageRefresh.setEnableRefresh(false);
        homepageRefresh.setEnableLoadmore(true);


    }

    @Override
    protected void initViewListener() {
        mHomepageContentAdapter.setonListItemClickListner(this);
        /*
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight = homePagerHeaderContainer.getMeasuredHeight();
                homePagerNestedView.setHeaderHeight(headerHeight);
                /*动态设置recyclerView 的高度，来解决NestedScrollView 嵌套RecyclerView
                导致RecyclerView适配器一直为每个item创建onCreateViewHolder { newInnerHolder(itemView) },导致内存过大的问题
                int height = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = height;
                mContentList.setLayoutParams(layoutParams);

                //高度只需要改变一次,去除监听
                if (height != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

         */


        looperPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                /**
                 * 这边出现bugPostion回%0， 0回出现报错。

                 */
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }

                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPosition);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLooperPagerAdapter.setonPicClickListner(this);
        homepageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //加载更多内容
                if (mCategoryPagerPresenter != null) {
                    mCategoryPagerPresenter.loadMore(mMaterialId);

                }
                homepageRefresh.finishLoadmore();
            }
        });
    }

    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void loadData() {
        Bundle argument = getArguments();
        String title = argument.getString(Constant.KEY_HOME_PAGE_TITLE);
        mMaterialId = argument.getInt(Constant.KEY_HOME_PAGE_MATERIAL_ID);
        //Todo:加载数据
        LogUtils.d(TAG, "title--> " + title);
        LogUtils.d(TAG, "materialId--> " + mMaterialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }

        if (currentCategoryTitle != null) {
            currentCategoryTitle.setText(title);
        }
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        mCategoryPagerPresenter.registerViewCallback(this);
    }


    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //todo:更新UI
        mHomepageContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {

        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {

        setUpState(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {

    }

    @Override
    public void onLoadMoreEmpty() {

    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //如果有数据就添加到适配器的底部：

        mHomepageContentAdapter.addData(contents);
        if (homepageRefresh != null) {
            homepageRefresh.finishLoadmore();
        }
        ToastUtils.showToast("加载了" + contents.size() + "数据");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {

        mLooperPagerAdapter.setData(contents);
        //添加轮播图上面的小点
        looperPointContainer.removeAllViews();
        //GradientDrawable selectDrawable = (GradientDrawable)getContext().getDrawable(R.drawable.shape_indicator_point);
        //GradientDrawable normalDrawable = (GradientDrawable)getContext().getDrawable(R.drawable.shape_indicator_point);
        //normalDrawable.setColor(getContext().getColor(R.color.colorWhite));

        //设置在中间可以左右滑动，但是取无限轮播的第一个
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetPosition = (Integer.MAX_VALUE / 2) - dx;
        looperPage.setCurrentItem(targetPosition);

        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }

            looperPointContainer.addView(point);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    private void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onClick(HomePagerContent.DataBean dataBean) {
        //列表内容：
        handlerItemClick(dataBean);

    }

    private void handlerItemClick(HomePagerContent.DataBean dataBean) {
        //拿到ticketpresenter去加载数据
        String title = dataBean.getTitle();
        String url = dataBean.getCoupon_click_url();
        if (TextUtils.isEmpty(url)) {
            url = dataBean.getClick_url();
        }
        String cover = dataBean.getPict_url();
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    public void ItemClick(HomePagerContent.DataBean item) {
        //轮播图点击：
        handlerItemClick(item);

    }
}
