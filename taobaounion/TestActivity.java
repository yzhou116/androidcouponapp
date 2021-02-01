package com.cvte.taobaounion;

import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cvte.taobaounion.model.domain.SearchContent;
import com.cvte.taobaounion.model.domain.SearchRecommend;
import com.cvte.taobaounion.presenter.ISearchPresenter;
import com.cvte.taobaounion.ui.custom.TextFloatLayout;
import com.cvte.taobaounion.utils.PresenterManager;
import com.cvte.taobaounion.view.ISearchViewCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends AppCompatActivity implements ISearchViewCallBack {


    public TextFloatLayout textFloatLayout;

    public ISearchPresenter searchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
/**
 List<String >  list = new ArrayList<>();
 list.add("毛衣");
 list.add("地闹");
 list.add("内存条");
 list.add("硬盘");
 list.add("苹果手机");
 list.add("安卓手机");
 list.add("肥宅快乐税");
 list.add("毛衣");
 textFloatLayout.setText(list);
 */

    }

    private void initView() {
        textFloatLayout = this.findViewById(R.id.search_history_view);
        searchPresenter = PresenterManager.getInstance().getSearchPresenter();

        searchPresenter.registerViewCallback(this);

        //获取搜索推荐此
        searchPresenter.getRecommendWord();

        //   searchPresenter.doSearch("键盘");

        searchPresenter.getHistory();

    }

    @Override
    public void onHistoryLoad(List<String> list) {

    }

    @Override
    public void onHistoryDeleted() {

    }

    @Override
    public void onSearchSuccess(SearchContent searchContent) {

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