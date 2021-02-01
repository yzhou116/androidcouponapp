package com.cvte.taobaounion.ui.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cvte.taobaounion.R;
import com.cvte.taobaounion.base.BaseFragment;
import com.cvte.taobaounion.model.domain.SearchContent;
import com.cvte.taobaounion.model.domain.SearchRecommend;
import com.cvte.taobaounion.presenter.ISearchPresenter;
import com.cvte.taobaounion.ui.adapter.SelectContentAdapter;
import com.cvte.taobaounion.ui.adapter.SelectLeftAdapter;
import com.cvte.taobaounion.ui.custom.TextFloatLayout;
import com.cvte.taobaounion.utils.PresenterManager;
import com.cvte.taobaounion.view.ISearchViewCallBack;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by user on 2020/10/21.
 */

public class SearchFragment extends BaseFragment implements ISearchViewCallBack {
    @BindView(R.id.search_result)
    public TextFloatLayout textFloatLayout;

    public ISearchPresenter searchPresenter;


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        View itemView = inflater.inflate(R.layout.fragment_search_layout, container, false);

        return itemView;

    }

    @Override
    protected void initPresenter() {
        searchPresenter = PresenterManager.getInstance().getSearchPresenter();

        searchPresenter.registerViewCallback(this);

        //获取搜索推荐此
        searchPresenter.getRecommendWord();

        //   searchPresenter.doSearch("键盘");

        searchPresenter.getHistory();


    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        searchPresenter.unregisterViewCallback(this);
    }

    @Override
    protected int getRootVireResId() {
        return R.layout.fragment_search;
    }


    @Override
    public void onHistoryLoad(List<String> list) {
        Log.d("SearchFragment", " onHistoryLoad --->" + list);

    }

    @Override
    public void onHistoryDeleted() {


    }

    @Override
    public void onSearchSuccess(SearchContent searchContent) {
        Log.d("SearchFragment", "onSearchSuccess-----> " + searchContent);

    }

    @Override
    public void onMoreLoad(SearchContent searchContent) {

    }

    @Override
    public void onMoreLoadError() {

    }

    @Override
    public void onMoreLoadEmpty() {

    }

    @Override
    public void getRecommendWordLoaded(List<SearchRecommend.DataBean> recommendWord) {

        Log.d("SearchFragment", "recommendWord ----->" + recommendWord.size());

        List<String> list = new ArrayList<>();
        for (SearchRecommend.DataBean dataBean : recommendWord) {
            list.add(dataBean.getKeyword());
        }
        textFloatLayout.setText(list);
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
